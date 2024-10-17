--liquibase formatter sql

--changeset Nika Avalishvili:1
CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255)
);

--changeset Nika Avalishvili:2
CREATE TABLE bank_account (
    id BIGSERIAL PRIMARY KEY,
    iban VARCHAR(16) NOT NULL UNIQUE,
    balance DECIMAL(19, 2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    customer_id BIGINT,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);

