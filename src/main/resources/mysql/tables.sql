CREATE TABLE IF NOT EXISTS account (
    email VARCHAR(45) NOT NULL,
    password VARCHAR(128) NOT NULL,

    PRIMARY KEY(email)
)ENGINE = InnoDB DEFAULT CHARSET = latin1;

