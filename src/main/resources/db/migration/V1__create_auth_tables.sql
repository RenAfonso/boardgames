-- USERS TABLE
CREATE TABLE users (
                       id              UUID PRIMARY KEY,
                       username        VARCHAR(50)  NOT NULL UNIQUE,
                       email           VARCHAR(255) NOT NULL UNIQUE,
                       password_hash   VARCHAR(255) NOT NULL,
                       enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
                       created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ROLES / SCOPES TABLE
CREATE TABLE roles (
                       id      SERIAL PRIMARY KEY,
                       name    VARCHAR(50) NOT NULL UNIQUE
);

-- USER ↔ ROLE MAPPING
CREATE TABLE user_roles (
                            user_id UUID NOT NULL,
                            role_id INT  NOT NULL,

                            PRIMARY KEY (user_id, role_id),

                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users (id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_user_roles_role
                                FOREIGN KEY (role_id)
                                    REFERENCES roles (id)
                                    ON DELETE CASCADE
);
