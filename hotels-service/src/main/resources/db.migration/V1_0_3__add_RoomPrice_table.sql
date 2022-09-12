ALTER TABLE hotel_rooms
    ADD COLUMN IF NOT EXISTS price_id INT;

create table if not exists room_price
(
    id            serial PRIMARY KEY,
    default_price DOUBLE PRECISION NOT NULL,
    price         DOUBLE PRECISION NOT NULL,
    start_date    BIGINT           NOT NULL,
    end_date      BIGINT           NOT NULL,
    CONSTRAINT FK_defaultPrice_price_startDate_endDate_hotelRoomId
        UNIQUE (default_price, price, start_date, end_date)

);
ALTER TABLE hotel_rooms
    ADD FOREIGN KEY (price_id) REFERENCES room_price (id);
