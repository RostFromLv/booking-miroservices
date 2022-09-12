create table hotels
(
    id         serial PRIMARY KEY,
    name       Varchar(256) NOT NULL,
    address_id INT UNIQUE   NOT NULL
);

create table hotel_rooms
(
    id            serial PRIMARY KEY,
    type          VARCHAR(256) NOT NULL,
    area          FLOAT        NOT NULL,
    status        VARCHAR(256) NOT NULL,
    hotels_id     INT          NOT NULL,
    price_id INT,
        CONSTRAINT FK_hotel_id FOREIGN KEY (hotels_id) REFERENCES hotels (id)
);