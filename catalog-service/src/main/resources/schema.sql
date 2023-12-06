CREATE TABLE IF NOT EXISTS catalog (
    id BIGSERIAL PRIMARY KEY,
    catalog_name CHAR(255) NOT NULL,
    catalog_tax DECIMAL(65,2) NOT NULL
);
