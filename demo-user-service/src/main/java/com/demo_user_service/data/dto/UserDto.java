package com.demo_user_service.data.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.common.validation.OnCreate;
import com.common.validation.OnUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    
    @Null(message = "ID має бути порожнім при реєстрації", groups = OnCreate.class)
    @NotNull(message = "ID є обов'язковим при оновленні", groups = OnUpdate.class)
    private Long id;
    
    @NotBlank(message = "Логін не може бути порожнім", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 32, message = "Логін не може перевищувати 32 символів", groups = {OnCreate.class, OnUpdate.class})
    private String login;
    
    // Роль зазвичай призначається бекендом (наприклад, ROLE_USER), тому її не валідуємо жорстко
    private String role;
    
    @NotBlank(message = "Пароль не може бути порожнім", groups = {OnCreate.class, OnUpdate.class})
    private String password;
    
    // Якщо email не є обов'язковим, @NotBlank можна прибрати, але @Email залишаємо для формату
    @Email(message = "Невірний формат електронної пошти", groups = {OnCreate.class, OnUpdate.class})
    private String email;
    
    @NotBlank(message = "Номер телефону не може бути порожнім", groups = {OnCreate.class, OnUpdate.class})
    @Pattern(
        regexp = "^380\\d{9}$", 
        message = "Номер телефону має бути у форматі 380XXXXXXXXX (рівно 12 символів)",
        groups = {OnCreate.class, OnUpdate.class}
    )
    private String phoneNumber;
    
    // Наступні поля контролюються системою. Ставимо @Null для OnCreate, 
    // щоб хитрий користувач не міг передати "trustLevel": 9999 при реєстрації.
    // @Null(message = "Рівень довіри встановлюється системою", groups = OnCreate.class)
    // private Integer trustLevel;
    
    // @Null(message = "Поле скасувань встановлюється системою", groups = OnCreate.class)
    // private Integer consecutiveCancellations;
    
    @Null(message = "Час реєстрації встановлюється системою", groups = OnCreate.class)
    private LocalDateTime registered;
}