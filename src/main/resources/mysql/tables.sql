CREATE TABLE IF NOT EXISTS account (
    email VARCHAR(45) NOT NULL,
    password VARCHAR(128) NOT NULL,

    PRIMARY KEY(email)
)ENGINE = InnoDB DEFAULT CHARSET = latin1;


CREATE TABLE IF NOT EXISTS stile (
    nome VARCHAR(255) NOT NULL,
    guida VARCHAR(45) NOT NULL,
    abv_min DOUBLE,
    abv_max DOUBLE,
    og_min DOUBLE,
    og_max DOUBLE,
    fg_min DOUBLE,
    fg_max DOUBLE,
    srm_min DOUBLE,
    srm_max DOUBLE,
    ibu_min INT,
    ibu_max INT,
    
    PRIMARY KEY(nome)
)ENGINE = InnoDB DEFAULT CHARSET = latin1;


CREATE TABLE IF NOT EXISTS ricetta (
    id INT NOT NULL AUTO_INCREMENT,
    account_id VARCHAR(45) NOT NULL,
    nome VARCHAR(45) DEFAULT 'Nome ricetta',
    descrizione TEXT,
    autore VARCHAR(45),
    tipo VARCHAR(45) DEFAULT 'All Grain',
    stile_id VARCHAR(255) DEFAULT 'American Light Lager',
    volume DOUBLE DEFAULT 0,
    rendimento DOUBLE DEFAULT 0,
    ultima_modifica TIMESTAMP DEFAULT NOW(),
    
    PRIMARY KEY(id),
    CONSTRAINT fk_ricetta_account FOREIGN KEY (account_id) REFERENCES account(email) ON DELETE CASCADE,
    CONSTRAINT fk_ricetta_stile FOREIGN KEY (stile_id) REFERENCES stile(nome)
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
    potenziale DOUBLE NOT NULL,
    rendimento DOUBLE DEFAULT 75.0,
    
    PRIMARY KEY(id),
    CONSTRAINT fk_fermentabile_ricetta FOREIGN KEY (ricetta_id) REFERENCES ricetta(id) ON DELETE CASCADE
)ENGINE = InnoDB DEFAULT CHARSET = latin1;


CREATE TABLE IF NOT EXISTS luppolo (
    id INT NOT NULL AUTO_INCREMENT,
    ricetta_id INT NOT NULL,
    quantita INT NOT NULL,
    tempo INT NOT NULL,
    nome VARCHAR(45) NOT NULL,
    categoria VARCHAR(45) NOT NULL,
    fornitore VARCHAR(45),
    provenienza VARCHAR(45),
    tipo VARCHAR(45) NOT NULL,
    alpha DOUBLE NOT NULL,
    
    PRIMARY KEY(id),
    CONSTRAINT fk_luppolo_ricetta FOREIGN KEY (ricetta_id) REFERENCES ricetta(id) ON DELETE CASCADE
)ENGINE = InnoDB DEFAULT CHARSET = latin1;


