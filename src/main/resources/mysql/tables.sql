USE gianmaria_saggini;
CREATE TABLE IF NOT EXISTS account (
    email VARCHAR(45) NOT NULL,
    password VARCHAR(128) NOT NULL,

    PRIMARY KEY(email)
)ENGINE = InnoDB DEFAULT CHARSET = latin1 COLLATE latin1_general_ci;


CREATE TABLE IF NOT EXISTS ricetta (
    id INT NOT NULL AUTO_INCREMENT,
    account_id VARCHAR(45) NOT NULL,
    nome VARCHAR(45) DEFAULT 'Nome ricetta',
    autore VARCHAR(45),
    tipo VARCHAR(45) DEFAULT 'All Grain',
    attrezzatura_id INT DEFAULT 0,
    stile_id INT DEFAULT 0,
    abv INT DEFAULT 0,
    og INT DEFAULT 0,
    fg INT DEFAULT 0,
    ebc INT DEFAULT 0,
    ibu INT DEFAULT 0,
    ultima_modifica TIMESTAMP NOT NULL,
    
    PRIMARY KEY(id),
	CONSTRAINT fk_ricetta_account FOREIGN KEY (account_id) REFERENCES account(email)
)ENGINE = InnoDB DEFAULT CHARSET = latin1 COLLATE latin1_general_ci;
