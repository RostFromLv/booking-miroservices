create table users(
    id serial PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL ,
    last_name VARCHAR(255) NOT NULL ,
    email VARCHAR(255) UNIQUE NOT NULL ,
    phone_number VARCHAR(255) NOT NULL ,
    created_at bigint NOT NULL ,
    updated_at bigint,
    CONSTRAINT UQ_user_firstName_lastName UNIQUE (first_name,last_name)
);