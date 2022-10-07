create table address(
    id serial PRIMARY KEY,
    country VARCHAR(255) NOT NULL,
    city VARCHAR (255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    house_number int NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_address_country_city_street_houseNumber
        UNIQUE (country,city,street,house_number)
);

INSERT  INTO address(country,city,street,house_number,postal_code) values ('Ukraine','Lviv','Kulparkenko',15,'7803654');