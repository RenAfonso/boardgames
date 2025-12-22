CREATE TABLE games
(
    id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(50)  NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE game_variants
(
    id      UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    game_id UUID       NOT NULL,
    code    VARCHAR(50)  NOT NULL,
    name    VARCHAR(100) NOT NULL,

    CONSTRAINT fk_game_variants_game
        FOREIGN KEY (game_id)
            REFERENCES games (id)
            ON DELETE CASCADE,

    UNIQUE (game_id, code)
);

CREATE TABLE matches
(
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    game_id    UUID NOT NULL,
    variant_id UUID NULL,
    created_by UUID NOT NULL,

    CONSTRAINT fk_matches_game
        FOREIGN KEY (game_id)
            REFERENCES games (id),

    CONSTRAINT fk_matches_variant
        FOREIGN KEY (variant_id)
            REFERENCES game_variants (id),

    CONSTRAINT fk_matches_created_by
        FOREIGN KEY (created_by)
            REFERENCES users (id)
);

CREATE TABLE match_players
(
    id                UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    match_id          UUID  NOT NULL,
    user_id           UUID  NOT NULL,

    points            INT     NOT NULL,
    starting_position INT     NOT NULL,
    won               BOOLEAN NOT NULL,

    CONSTRAINT fk_match_players_match
        FOREIGN KEY (match_id)
            REFERENCES matches (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_match_players_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    UNIQUE (match_id, user_id)
);
