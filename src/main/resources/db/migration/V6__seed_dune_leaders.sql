ALTER TABLE dune_leaders
    ADD COLUMN variant_id UUID;

ALTER TABLE dune_leaders
    ADD CONSTRAINT fk_dune_leaders_variant
        FOREIGN KEY (variant_id)
            REFERENCES game_variants (id);

ALTER TABLE dune_leaders
    ADD CONSTRAINT unique_variant_name
        UNIQUE (variant_id, name);

-- Leaders for DUNE_UPRISING
INSERT INTO dune_leaders (id, variant_id, name)
SELECT gen_random_uuid(), gv.id, l.name
FROM game_variants gv
         JOIN games g ON g.id = gv.game_id
         JOIN (
    VALUES
        ('Muad’Dib'),
        ('Feyd-Rautha Harkonnen'),
        ('Lady Margot Fenring'),
        ('Lady Amber Metulli'),
        ('Gurney Halleck'),
        ('Lady Jessica'),
        ('Princess Irulan'),
        ('Staban Tuek'),
        ('Emperor Shaddam Corrino IV'),
        ('Kota Odax of Ix'),
        ('Liet-Kynes'),
        ('Steersman Yrkoon'),
        ('Esmar Tuek'),
        ('Piter de Vries'),
        ('Chani'),
        ('Duncan Idaho'),
        ('Count Hasimir Fenring')
) AS l(name) ON true
WHERE g.code = 'DUNE_UPRISING'
  AND gv.code IN ('BASE', 'BLOODLINES')
ON CONFLICT (name) DO NOTHING;

-- Leaders for DUNE_IMPERIUM
INSERT INTO dune_leaders (id, variant_id, name)
SELECT gen_random_uuid(), gv.id, l.name
FROM game_variants gv
         JOIN games g ON g.id = gv.game_id
         JOIN (
    VALUES
        ('Princess Yuna Moritani'),
        ('Archduke Armand Ecaz'),
        ('Baron Vladimir Harkonnen'),
        ('Count Ilban Richese'),
        ('Countess Ariana Thorvald'),
        ('Duke Leto Atreides'),
        ('Earl Memnon Thorvald'),
        ('Glossu "The Beast" Rabban'),
        ('Helena Richese'),
        ('Ilesa Ecaz'),
        ('Paul Atreides'),
        ('Prince Rhombur Vernius'),
        ('Tessia Vernius'),
        ('Viscount Hundro Moritani')
) AS l(name) ON true
WHERE g.code = 'DUNE_IMPERIUM'
  AND gv.code IN ('BASE', 'RISE_OF_IX', 'IMMORTALITY')
ON CONFLICT (name) DO NOTHING;

