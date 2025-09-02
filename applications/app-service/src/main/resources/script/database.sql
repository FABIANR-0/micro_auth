CREATE TABLE role
(
    role_id     BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200) NOT NULL
);

INSERT INTO role (role_id, name, description)
VALUES
    (1, 'ADMIN', 'ADMINISTRATOR ROLE WITH FULL SYSTEM ACCESS AND MANAGEMENT CAPABILITIES'),
    (2, 'CLIENT', 'CLIENT ROLE FOR USERS APPLYING FOR LOANS AND MANAGING THEIR APPLICATIONS'),
    (3, 'ADVISOR', 'ADVISOR ROLE RESPONSIBLE FOR ASSISTING CLIENTS AND REVIEWING LOAN APPLICATIONS');

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
    password    VARCHAR(200)    NOT NULL,
    role_id     BIGINT NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(role_id)
);

INSERT INTO "user" (
    name, last_name, birth_date, dni, phone, email, address, base_salary, role_id, password
)
VALUES (
           'Fabián',
           'Rincón Chinchilla',
           '2004-07-14',
           '123456789',
           '+57 3000000000',
           'fabian.rincon@example.com',
           'Ocaña, Colombia',
           5000000.00,
           1,
           '$2y$10$VQDgZPmoeLc1w13mdje.wOc1rlJyqjEeqDqgqKMQ1GKgdeonFi0cq'
       );