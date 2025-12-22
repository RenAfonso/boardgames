INSERT INTO games (code, name)
VALUES
    ('DUNE_IMPERIUM', 'Dune: Imperium'),
    ('DUNE_UPRISING', 'Dune: Uprising'),
    ('SCYTHE', 'Scythe'),
    ('BLOOD_RAGE', 'Blood Rage')
ON CONFLICT (code) DO NOTHING;

-- DUNE_UPRISING variants
INSERT INTO game_variants (game_id, code, name)
SELECT g.id, 'BASE', 'Base'
FROM games g
WHERE g.code = 'DUNE_UPRISING'
ON CONFLICT (game_id, code) DO NOTHING;

INSERT INTO game_variants (game_id, code, name)
SELECT g.id, 'BLOODLINES', 'Bloodlines'
FROM games g
WHERE g.code = 'DUNE_UPRISING'
ON CONFLICT (game_id, code) DO NOTHING;

-- DUNE_IMPERIUM variants
INSERT INTO game_variants (game_id, code, name)
SELECT g.id, 'BASE', 'Base'
FROM games g
WHERE g.code = 'DUNE_IMPERIUM'
ON CONFLICT (game_id, code) DO NOTHING;

INSERT INTO game_variants (game_id, code, name)
SELECT g.id, 'RISE_OF_IX', 'Rise of Ix'
FROM games g
WHERE g.code = 'DUNE_IMPERIUM'
ON CONFLICT (game_id, code) DO NOTHING;

INSERT INTO game_variants (game_id, code, name)
SELECT g.id, 'IMMORTALITY', 'Immortality'
FROM games g
WHERE g.code = 'DUNE_IMPERIUM'
ON CONFLICT (game_id, code) DO NOTHING;
