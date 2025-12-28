-- Add variant_id column if missing
DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name='dune_leaders' AND column_name='variant_id'
        ) THEN
            ALTER TABLE dune_leaders
                ADD COLUMN variant_id UUID;
        END IF;
    END
$$;

-- Drop old unique constraint if it exists
DO $$
    BEGIN
        IF EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname='dune_leaders_name_key'
        ) THEN
            ALTER TABLE dune_leaders
                DROP CONSTRAINT dune_leaders_name_key;
        END IF;
    END
$$;

-- Add new unique constraint if it doesn't exist
DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname='unique_variant_name'
        ) THEN
            ALTER TABLE dune_leaders
                ADD CONSTRAINT unique_variant_name UNIQUE (variant_id, name);
        END IF;
    END
$$;

-- DUNE_UPRISING
INSERT INTO dune_leaders (id, variant_id, name)
SELECT
    gen_random_uuid(),
    gv.id,
    l.name
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
ON CONFLICT (variant_id, name) DO NOTHING;

-- DUNE_IMPERIUM
INSERT INTO dune_leaders (id, variant_id, name)
SELECT
    gen_random_uuid(),
    gv.id,
    l.name
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
        ('Glossu Rabban'),
        ('Helena Richese'),
        ('Ilesa Ecaz'),
        ('Paul Atreides'),
        ('Prince Rhombur Vernius'),
        ('Tessia Vernius'),
        ('Viscount Hundro Moritani')
) AS l(name) ON true
WHERE g.code = 'DUNE_IMPERIUM'
ON CONFLICT (variant_id, name) DO NOTHING;
