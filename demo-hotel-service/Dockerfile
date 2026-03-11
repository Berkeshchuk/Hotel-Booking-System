# Етап 1: Збірка (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Спочатку копіюємо і встановлюємо common бібліотеку в локальний репозиторій контейнера
COPY common ./common
RUN cd common && mvn clean install

# 2. Тепер копіюємо і збираємо сам мікросервіс (demo-hotel-service)
COPY demo-hotel-service ./demo-hotel-service
RUN cd demo-hotel-service && mvn clean package -DskipTests

# Етап 2: Запуск (Run)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Копіюємо зібраний jar файл з папки сервісу
COPY --from=build /app/demo-hotel-service/target/*.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]