INSERT INTO dune_leaders (id, variant_id, name)
SELECT gen_random_uuid(), gv.id, l.name
FROM game_variants gv
         JOIN games g ON g.id = gv.game_id
         JOIN (
    VALUES
        ('Reverend Mother Gaius Mohiam')
) AS l(name) ON true
WHERE g.code = 'DUNE_UPRISING'
  AND gv.code IN ('BASE', 'BLOODLINES')
ON CONFLICT (name) DO NOTHING;