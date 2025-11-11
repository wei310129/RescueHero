-- 測試資料

-- 只建立「光復」鄉 address_cell，parent_id 指向已存在的「花蓮」cell
WITH
    audit_row AS (
        INSERT INTO audit_info (id, created_at, updated_at)
            VALUES (gen_random_uuid(), now(), now())
            RETURNING id
    ),
    tw_country AS (
        SELECT id FROM country WHERE code = 'TW'
    ),
    level_town AS (
        SELECT id FROM address_level
        WHERE country_id = (SELECT id FROM tw_country)
          AND name = '鄉'
          AND sequence = 2
        LIMIT 1
    ),
    hualien_cell AS (
        SELECT id FROM address_cell WHERE name = '花蓮' LIMIT 1
    )
INSERT INTO address_cell (audit_id, level_id, parent_id, name)
VALUES (
           (SELECT id FROM audit_row),
           (SELECT id FROM level_town),
           (SELECT id FROM hualien_cell),
           '光復'
       );

-- 建立 address，指定 address_cell 為「光復」
INSERT INTO audit_info (id, created_at, updated_at)
VALUES (gen_random_uuid(), now(), now());

WITH
    tw_country AS (
        SELECT id FROM country WHERE code = 'TW'
    ),
    cell_guangfu AS (
        SELECT id FROM address_cell WHERE name = '光復' LIMIT 1
    ),
    latest_audit AS (
        SELECT id FROM audit_info ORDER BY created_at DESC LIMIT 1
    )
INSERT INTO address (audit_id, country_id, address_cell_id, detail, full_address)
VALUES (
    (SELECT id FROM latest_audit),
    (SELECT id FROM tw_country),
    (SELECT id FROM cell_guangfu),
    NULL,
    '花蓮縣光復鄉'
);

-- 測試災害資料：花蓮光復
INSERT INTO audit_info (id, created_at, updated_at)
VALUES (gen_random_uuid(), now(), now());

WITH
    tw_country AS (
        SELECT id FROM country WHERE code = 'TW'
    ),
    addr AS (
        SELECT id FROM address WHERE full_address = '花蓮縣光復鄉' LIMIT 1
    ),
    latest_audit AS (
        SELECT id FROM audit_info ORDER BY created_at DESC LIMIT 1
    ),
    ins_disaster AS (
        INSERT INTO disaster (audit_id, country_id, status, name, occurred_at, location, description)
        VALUES (
            (SELECT id FROM latest_audit),
            (SELECT id FROM tw_country),
            'ACTIVE',
            '花蓮馬太鞍堰塞湖潰壩',
            '2025-11-10',
            (SELECT id FROM addr),
            '測試災害資料，地點為花蓮縣光復鄉'
        )
        RETURNING id
    ),
    ins_type_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
            VALUES (gen_random_uuid(), now(), now())
            RETURNING id
    ),
    ins_type AS (
        INSERT INTO status_type (audit_id, name, description)
            SELECT a.id, UPPER(REPLACE('TASK', ' ', '')), 'Rescue Task 狀態類型'
            FROM ins_type_audit a
            RETURNING id
    ),
    ins_status_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
            SELECT gen_random_uuid(), now(), now()
            FROM generate_series(1, 4)
            RETURNING id
    ),
    ins_status_list AS (
        SELECT * FROM (
            VALUES
                  ('PENDING',   '待處理',   '任務尚未開始'),
                  ('IN_PROGRESS','進行中',  '任務正在進行'),
                  ('COMPLETED', '已完成',   '任務已完成'),
                  ('CANCELLED', '已取消',   '任務已取消')
            ) AS t(code, name, description)
    ),
    ins_status AS (
        INSERT INTO status (audit_id, disaster_id, type_id, code, name, description)
            SELECT audits.id,
                   (SELECT id FROM ins_disaster),
                   t.id,
                   UPPER(REPLACE(item.code, ' ', '')),
                   item.name,
                   item.description
            FROM (
                     SELECT id, row_number() OVER () AS rn FROM ins_status_audits
                 ) audits
                     CROSS JOIN ins_type t
                     JOIN (
                SELECT code, name, description, row_number() OVER () AS rn FROM ins_status_list
            ) item ON audits.rn = item.rn
            RETURNING id
    )
SELECT 1;
