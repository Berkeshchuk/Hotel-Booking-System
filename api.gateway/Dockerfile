# Етап 1: Збірка (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Спочатку копіюємо і встановлюємо common бібліотеку в локальний репозиторій контейнера
COPY common ./common
RUN cd common && mvn clean install

# 2. Тепер копіюємо і збираємо сам мікросервіс (api.gateway)
COPY api.gateway ./api.gateway
RUN cd api.gateway && mvn clean package -DskipTests

# Етап 2: Запуск (Run)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Копіюємо зібраний jar файл з папки сервісу
COPY --from=build /app/api.gateway/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]