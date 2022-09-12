create table reservation
(
    id        serial PRIMARY KEY,
    hotel_id  INT    NOT NULL,
    room_id   INT    NOT NULL,
    date_from BIGINT NOT NULL,
    date_to   BIGINT NOT NULL,
    CONSTRAINT UQ_hotel_room_from_to UNIQUE (hotel_id, room_id, date_from, date_to)
)