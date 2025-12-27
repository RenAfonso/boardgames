CREATE OR REPLACE VIEW match_players_readable AS
SELECT
    mp.id                  AS match_player_id,
    m.id                   AS match_id,
    g.name                 AS game_name,
    gv.name                AS variant_name,
    u.username             AS username,
    mp.points              AS points,
    mp.starting_position   AS starting_position,
    mp.won                 AS won,
    m.played_at            AS played_at
FROM match_players mp
         JOIN matches m        ON m.id = mp.match_id
         JOIN games g          ON g.id = m.game_id
         LEFT JOIN game_variants gv ON gv.id = m.variant_id
         JOIN users u          ON u.id = mp.user_id
ORDER BY m.played_at DESC, mp.starting_position;

ALTER TABLE dune_match_players
    DROP COLUMN IF EXISTS spice_collected;
