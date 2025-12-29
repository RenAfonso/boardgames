ALTER TABLE roles
    ALTER COLUMN id TYPE BIGINT;

-- Fix sequence if needed
ALTER SEQUENCE roles_id_seq
    AS BIGINT;
