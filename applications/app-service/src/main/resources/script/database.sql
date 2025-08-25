-- AGREGAR RELACIÓN CON ROLE
CREATE TABLE user
(
    user_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)    NOT NULL,
    last_name   VARCHAR(50)    NOT NULL,
    birth_date  DATE,
    nit         VARCHAR(20)    NOT NULL,
    phone       VARCHAR(20),
    email       VARCHAR(100)   NOT NULL UNIQUE,
    address     VARCHAR(100),
    base_salary DECIMAL(10, 2) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE role
(
    role_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(50) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO role
    VALUES (1,'ADMIN', 'ADMIN ROLE WITH ALL FUNCTIONS'),
    (2,'CLIENT', 'CLIENT ROLE - LOAN APPLICANT');