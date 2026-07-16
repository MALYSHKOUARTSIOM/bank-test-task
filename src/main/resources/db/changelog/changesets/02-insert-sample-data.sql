--liquibase formatted sql

--changeset author:2
INSERT INTO client (full_name, phone_number, email, status) VALUES 
('Иванов Иван Иванович', '+375291112233', 'ivanov@example.com', 0),
('Петров Петр Петрович', '+375294445566', 'petrov@example.com', 1),
('Сидорова Анна Сергеевна', '+375297778899', 'sidorova@example.com', 2),
('John Doe', '+12025550143', 'john.doe@example.com', 0);

INSERT INTO payment_card (card_number, currency, card_type, client_id) VALUES 
('1111222233334444', 'BYN', 'Classic', 1),
('5555666677778888', 'EUR', 'Gold', 2),
('9999000011112222', 'USD', 'Platinum', 3),
('1234567890123456', 'USD', 'Classic', 4);
