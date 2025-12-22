CREATE TABLE dune_leaders
(
    id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE dune_match_players
(
    id                UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    match_player_id   UUID NOT NULL, -- FK to match_players.id
    leader_id         UUID NOT NULL, -- FK to dune_leaders.id
    team_side         VARCHAR(20) NOT NULL, -- 'Fremen' or 'Emperor'
    spice_collected   INT DEFAULT 0 NOT NULL,

    CONSTRAINT fk_dune_match_players_match_player
        FOREIGN KEY (match_player_id)
            REFERENCES match_players (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_dune_match_players_leader
        FOREIGN KEY (leader_id)
            REFERENCES dune_leaders (id)
);