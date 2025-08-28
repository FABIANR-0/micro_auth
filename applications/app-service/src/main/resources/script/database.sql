CREATE TABLE role
(
    role_id     BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(50) NOT NULL
);

INSERT INTO role (role_id, name, description)
VALUES (1, 'ADMIN', 'ADMIN ROLE WITH ALL FUNCTIONS'),
       (2, 'CLIENT', 'CLIENT ROLE - LOAN APPLICANT');

CREATE TABLE "user"
(
    user_id     BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50)    NOT NULL,
    last_name   VARCHAR(50)    NOT NULL,
    birth_date  DATE,
    dni         VARCHAR(20)    NOT NULL,
    phone       VARCHAR(20),
    email       VARCHAR(100)   NOT NULL UNIQUE,
    address     VARCHAR(100),
    base_salary NUMERIC(10, 2) NOT NULL,
    role_id     BIGINT NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(role_id)
);