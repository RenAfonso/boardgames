CREATE VIEW v_matches_readable AS
SELECT
    m.id,
    g.code  AS game,
    gv.code AS variant,
    m.played_at,
    m.tiebreaker
FROM matches m
         JOIN games g ON g.id = m.game_id
         LEFT JOIN game_variants gv ON gv.id = m.variant_id;
