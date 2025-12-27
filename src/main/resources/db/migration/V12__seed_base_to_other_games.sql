INSERT INTO game_variants (game_id, code, name)
SELECT g.id, 'BASE', 'Base'
FROM games g
WHERE g.code = 'SCYTHE'
ON CONFLICT (game_id, code) DO NOTHING;

INSERT INTO game_variants (game_id, code, name)
SELECT g.id, 'BASE', 'Base'
FROM games g
WHERE g.code = 'BLOOD_RAGE'
ON CONFLICT (game_id, code) DO NOTHING;