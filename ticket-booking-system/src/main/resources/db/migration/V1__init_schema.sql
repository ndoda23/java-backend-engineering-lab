CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    role            VARCHAR(50)  NOT NULL,
    created_at      TIMESTAMP    NOT NULL
);

CREATE TABLE events (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255)  NOT NULL,
    description     VARCHAR(1000),
    event_date      TIMESTAMP     NOT NULL,
    location        VARCHAR(255)  NOT NULL,
    base_price      DOUBLE PRECISION NOT NULL,
    created_at      TIMESTAMP     NOT NULL
);

CREATE TABLE bookings (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT           NOT NULL REFERENCES users (id),
    total_price     DOUBLE PRECISION NOT NULL,
    status          VARCHAR(50)      NOT NULL,
    created_at      TIMESTAMP        NOT NULL,
    expires_at      TIMESTAMP
);

CREATE INDEX idx_bookings_user_id ON bookings (user_id);
CREATE INDEX idx_bookings_status_expires_at ON bookings (status, expires_at);

CREATE TABLE tickets (
    id              BIGSERIAL PRIMARY KEY,
    event_id        BIGINT           NOT NULL REFERENCES events (id),
    booking_id      BIGINT           REFERENCES bookings (id),
    seat_row        VARCHAR(50)      NOT NULL,
    seat_number     INTEGER          NOT NULL,
    price           DOUBLE PRECISION NOT NULL,
    status          VARCHAR(50)      NOT NULL,
    version         BIGINT           NOT NULL DEFAULT 0,
    CONSTRAINT uk_ticket_event_seat UNIQUE (event_id, seat_row, seat_number)
);

CREATE INDEX idx_tickets_event_id_status ON tickets (event_id, status);
CREATE INDEX idx_tickets_booking_id ON tickets (booking_id);
