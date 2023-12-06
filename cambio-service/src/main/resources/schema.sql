CREATE TABLE IF NOT EXISTS cambio (
    id BIGSERIAL PRIMARY KEY,
    from_currency CHAR(3) NOT NULL,
    to_currency CHAR(3) NOT NULL,
    conversion_factor DECIMAL(65,2) NOT NULL
);
