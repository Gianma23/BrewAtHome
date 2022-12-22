USE gianmaria_saggini;
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
    tipo VARCHAR(45) DEFAULT 'All Grain',
    attrezzatura_id INT DEFAULT 0,
    stile_id INT DEFAULT 0,
    abv INT,
    og INT,
    fg INT,
    ebc INT,
    ibu INT,
    ultima_modifica TIMESTAMP DEFAULT NOW(),
    
    PRIMARY KEY(id),
    CONSTRAINT fk_ricetta_account FOREIGN KEY (account_id) REFERENCES account(email)
)ENGINE = InnoDB DEFAULT CHARSET = latin1;


CREATE TABLE IF NOT EXISTS fermentabile (
    id INT NOT NULL AUTO_INCREMENT,
    ricetta_id INT NOT NULL,
    nome VARCHAR(45) NOT NULL,
    quantita INT NOT NULL,
    categoria VARCHAR(45) NOT NULL,
    fornitore VARCHAR(45),
    provenienza VARCHAR(45),
    tipo VARCHAR(45) NOT NULL,
    colore INT NOT NULL,
    potenziale INT NOT NULL,
    rendimento INT DEFAULT 75,
    non_fermentabile BOOLEAN DEFAULT false,
    
    PRIMARY KEY(id),
    CONSTRAINT fk_fermentabile_ricetta FOREIGN KEY (ricetta_id) REFERENCES ricetta(id)
)ENGINE = InnoDB DEFAULT CHARSET = latin1;


CREATE TABLE IF NOT EXISTS luppolo (
    id INT NOT NULL AUTO_INCREMENT,
    ricetta_id INT NOT NULL,
    quantita INT NOT NULL,
    nome VARCHAR(45) NOT NULL,
    categoria VARCHAR(45) NOT NULL,
    fornitore VARCHAR(45),
    provenienza VARCHAR(45),
    tipo VARCHAR(45) NOT NULL,
    colore INT NOT NULL,
    potenziale INT NOT NULL,
    rendimento INT DEFAULT 75,
    non_fermentabile BOOLEAN DEFAULT false,
    
    PRIMARY KEY(id),
    CONSTRAINT fk_fermentabile_ricetta FOREIGN KEY (ricetta_id) REFERENCES ricetta(id)
)ENGINE = InnoDB DEFAULT CHARSET = latin1;