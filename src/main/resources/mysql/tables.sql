CREATE TABLE IF NOT EXISTS account (
    email VARCHAR(45) NOT NULL,
    password VARCHAR(128) NOT NULL,

    PRIMARY KEY(email)
)ENGINE = InnoDB DEFAULT CHARSET = latin1;


CREATE TABLE IF NOT EXISTS ricetta (
	id INT NOT NULL AUTO_INCREMENT,
    account_id VARCHAR(45) NOT NULL,
    nome VARCHAR(45) DEFAULT 'Nome ricetta',
    autore VARCHAR(45),
    tipo VARCHAR(45),
    attrezzatura_id INT NOT NULL,
    stile_id INT,
	abv INT,
    og INT,
    fg INT,
    ebc INT,
    ibu INT,
    ultima_modifica TIMESTAMP NOT NULL,
    
    PRIMARY KEY(id),
	CONSTRAINT fk_ricetta_account FOREIGN KEY (account_id) REFERENCES account(email)
)ENGINE = InnoDB DEFAULT CHARSET = latin1;
