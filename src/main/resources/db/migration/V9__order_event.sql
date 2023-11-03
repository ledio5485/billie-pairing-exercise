CREATE TABLE IF NOT EXISTS organisations_schema.order_event
(
    id           UUID PRIMARY KEY,
    aggregate_id UUID      NOT NULL,
    event_type   TEXT      NOT NULL,
    payload      JSONB,
    created_at   TIMESTAMP NOT NULL,
    created_by   TEXT      NOT NULL
);

CREATE INDEX idx_aggregate_id
    ON organisations_schema.order_event (aggregate_id);
