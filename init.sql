CREATE DATABASE IF NOT EXISTS persistent_login_db;
CREATE DATABASE IF NOT EXISTS java_hotel_booking_service_db;
CREATE DATABASE IF NOT EXISTS java_hotel_user_service_db;

CREATE TABLE persistent_login_db.persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) PRIMARY KEY,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL
);