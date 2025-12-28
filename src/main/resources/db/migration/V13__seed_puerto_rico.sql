INSERT INTO games (code, name)
VALUES
    ('PUERTO_RICO', 'Puerto Rico')
ON CONFLICT (code) DO NOTHING;