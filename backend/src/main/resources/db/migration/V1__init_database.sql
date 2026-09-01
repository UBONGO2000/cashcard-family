CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(180) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       role VARCHAR(30) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE cash_cards (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(120) NOT NULL,
                            balance NUMERIC(19,2) NOT NULL DEFAULT 0,
                            spending_limit NUMERIC(19,2),
                            status VARCHAR(30) NOT NULL,
                            owner_id BIGINT NOT NULL,
                            version BIGINT NOT NULL DEFAULT 0,
                            created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                            CONSTRAINT fk_cash_cards_owner
                                FOREIGN KEY (owner_id)
                                    REFERENCES users(id)
);

CREATE TABLE card_transactions (
                                   id BIGSERIAL PRIMARY KEY,
                                   card_id BIGINT NOT NULL,
                                   type VARCHAR(30) NOT NULL,
                                   status VARCHAR(30) NOT NULL,
                                   amount NUMERIC(19,2) NOT NULL,
                                   balance_after NUMERIC(19,2) NOT NULL,
                                   merchant VARCHAR(255),
                                   reason VARCHAR(255),
                                   created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                   CONSTRAINT fk_card_transactions_card
                                       FOREIGN KEY (card_id)
                                           REFERENCES cash_cards(id)
);