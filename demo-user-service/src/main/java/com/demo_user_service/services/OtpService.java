package com.demo_user_service.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    
    private static final int OTP_LENGTH = 6;
    private static final int OTP_TTL_MINUTES = 5;
    
    // Налаштування Rate Limit
    private static final int COOLDOWN_SECONDS = 60; // Заборона повторного запиту на 60 секунд
    private static final int MAX_REQUESTS_PER_DAY = 15; // Максимум 15 SMS на добу на один номер

    private final AdbSmsSenderService smsSenderService;
    private final Cache<String, String> otpCache;
    
    // Кеш для відстеження кулдауну (забороняє часті запити)
    private final Cache<String, Long> cooldownCache;
    
    // Кеш для відстеження денного ліміту
    private final Cache<String, Integer> dailyLimitCache;
    
    private final SecureRandom secureRandom;

    public OtpService(AdbSmsSenderService smsSenderService) {
        this.smsSenderService = smsSenderService;
        this.secureRandom = new SecureRandom();
        
        this.otpCache = Caffeine.newBuilder()
                .expireAfterWrite(OTP_TTL_MINUTES, TimeUnit.MINUTES)
                .maximumSize(50_000)
                .build();
                
        // Кеш кулдауну живе 60 секунд
        this.cooldownCache = Caffeine.newBuilder()
                .expireAfterWrite(COOLDOWN_SECONDS, TimeUnit.SECONDS)
                .maximumSize(50_000)
                .build();
                
        // Кеш денного ліміту живе 24 години
        this.dailyLimitCache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();
    }

    public void generateAndSendOtp(String phone) {
        // 1. Перевіряємо кулдаун (щоб не слали частіше ніж раз на хвилину)
        if (cooldownCache.getIfPresent(phone) != null) {
            throw new RuntimeException("Зачекайте 60 секунд перед наступним запитом.");
        }

        // 2. Перевіряємо денний ліміт
        Integer currentRequests = dailyLimitCache.getIfPresent(phone);
        if (currentRequests != null && currentRequests >= MAX_REQUESTS_PER_DAY) {
            log.warn("Перевищено денний ліміт SMS для номера: {}", phone);
            throw new RuntimeException("Перевищено ліміт запитів. Спробуйте завтра.");
        }

        // Генеруємо код
        String otpCode = generateCode();
        otpCache.put(phone, otpCode);
        
        // Оновлюємо ліміти
        cooldownCache.put(phone, System.currentTimeMillis());
        dailyLimitCache.put(phone, currentRequests == null ? 1 : currentRequests + 1);

        log.info("Згенеровано OTP для {}. Термін дії: {} хв.", phone, OTP_TTL_MINUTES);

        // Відправляємо SMS
        String message = String.format("Ваш код підтвердження Edemium: %s", otpCode);
        smsSenderService.sendSms(phone, message);
    }

    public boolean validateOtp(String phone, String code) {
        if (code == null || code.isBlank()) {
            return false;
        }

        String cachedCode = otpCache.getIfPresent(phone);
        
        if (cachedCode != null && cachedCode.equals(code)) {
            otpCache.invalidate(phone);
            // При успішному підтвердженні можна скинути кулдаун, якщо потрібно, 
            // але зазвичай це не обов'язково
            log.info("OTP успішно підтверджено для {}", phone);
            return true;
        }

        log.warn("Невдала спроба підтвердження OTP для {} (Невірний код або TTL вийшов)", phone);
        return false;
    }

    private String generateCode() {
        int max = (int) Math.pow(10, OTP_LENGTH) - 1;
        int code = secureRandom.nextInt(max);
        return String.format("%0" + OTP_LENGTH + "d", code);
    }
}