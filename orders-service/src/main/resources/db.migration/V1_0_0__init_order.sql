create table orders
(
    id            serial PRIMARY KEY,
    user_id       INT          NOT NULL,
    hotel_room_id INT          NOT NULL,
    hotel_id      INT          NOT NULL,
    from_date     BIGINT       NOT NULL,
    end_date      BIGINT       NOT NULL,
    status        VARCHAR(255) NOT NULL,
    created_at    BIGINT       NOT NULL,
    expired_at    BIGINT       NOT NULL
);