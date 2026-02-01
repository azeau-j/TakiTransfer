CREATE TABLE takitransfer.transfer
(
    id         UUID PRIMARY KEY,
    key        VARCHAR UNIQUE NOT NULL,
    expire_at  TIMESTAMP,
    created_at TIMESTAMP      NOT NULL
);