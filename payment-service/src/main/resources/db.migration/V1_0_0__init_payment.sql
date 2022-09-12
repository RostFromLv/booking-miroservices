create table payments
(
    id               serial PRIMARY KEY,
    number           VARCHAR(20) unique NOT NULL ,
    ccv2             VARCHAR(5) NOT NULL ,
    expiration_date  BIGINT NOT NULL ,
    holder_full_name VARCHAR(255)
)