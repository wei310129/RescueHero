-- UUID 擴充套件
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


-- 共用審計欄位
CREATE TABLE audit_info (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by BIGINT,
    updated_by BIGINT
);
CREATE INDEX IF NOT EXISTS idx_audit_created_by ON audit_info(created_by);
CREATE INDEX IF NOT EXISTS idx_audit_updated_by ON audit_info(updated_by);


-- 國家表
CREATE TABLE IF NOT EXISTS country (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    name VARCHAR(64) NOT NULL UNIQUE, -- 國家名稱
    native_name VARCHAR(128),          -- 本地語言名稱（台灣）
    code VARCHAR(8) NOT NULL UNIQUE,  -- 國家代碼 (如 TW, US)
    CHECK (code ~ '^[A-Z]{2,3}$')
);
WITH country_data AS (
    SELECT *
    FROM jsonb_to_recordset(
                 '[{"name":"台灣","native_name":"臺灣","code":"TW"},
                   {"name":"美國","native_name":"United States","code":"US"},
                   {"name":"日本","native_name":"日本","code":"JP"},
                   {"name":"韓國","native_name":"대한민국","code":"KR"}]'
         ) AS (name text, native_name text, code text)
),
audit_rows AS (
    INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now()
        FROM country_data
        RETURNING id
)
INSERT INTO country (audit_id, name, native_name, code)
SELECT a.id, c.name, c.native_name, c.code
FROM audit_rows a
         JOIN country_data c ON TRUE
LIMIT (SELECT count(*) FROM country_data);


-- 地址單位表
CREATE TABLE IF NOT EXISTS address_level (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    country_id BIGINT NOT NULL REFERENCES country(id) ON DELETE CASCADE,
    name VARCHAR(32) NOT NULL,                       -- 層級名稱 (ex: 縣市、區、鄉、路、街)
    suffix VARCHAR(32),                              -- 後綴字（如: 一段、二段）
    sequence INTEGER NOT NULL CHECK (sequence > 0),  -- 層級順序
    UNIQUE (country_id, name, suffix, sequence)
);

-- 台灣 address_level 初始資料
INSERT INTO audit_info (id, created_at, updated_at)
SELECT gen_random_uuid(), now(), now() FROM generate_series(1, 27);

INSERT INTO address_level (audit_id, country_id, name, suffix, sequence)
SELECT a.id, c.id, l.name, l.suffix, l.sequence
FROM (
    SELECT id, row_number() OVER (ORDER BY created_at DESC) AS rn FROM audit_info ORDER BY created_at DESC LIMIT 27
) a
CROSS JOIN (
    SELECT id FROM country WHERE code = 'TW'
) c
JOIN (
    SELECT * FROM (
        VALUES
            ('縣', NULL, 1),
            ('市', NULL, 1),
            ('鄉', NULL, 2),
            ('鎮', NULL, 2),
            ('市', NULL, 2),
            ('區', NULL, 2),
            ('村', NULL, 3),
            ('里', NULL, 3),
            ('鄰', NULL, 4),
            ('路', NULL, 5),
            ('路', '一段', 5),
            ('路', '二段', 5),
            ('路', '三段', 5),
            ('路', '四段', 5),
            ('路', '五段', 5),
            ('路', '六段', 5),
            ('街', NULL, 5),
            ('街', '一段', 5),
            ('街', '二段', 5),
            ('街', '三段', 5),
            ('街', '四段', 5),
            ('街', '五段', 5),
            ('街', '六段', 5),
            ('巷', NULL, 6),
            ('弄', NULL, 7),
            ('號', NULL, 8),
            ('樓', NULL, 9),
            ('樓', NULL, 10)
    ) AS t(name, suffix, sequence)
) l ON l.sequence = a.rn;


-- 地址欄位表
CREATE TABLE IF NOT EXISTS address_cell (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    level_id BIGINT NOT NULL REFERENCES address_level(id) ON DELETE CASCADE,
    parent_id BIGINT REFERENCES address_cell(id) ON DELETE SET NULL,   -- 上層地點，例如中正區→台北市
    name VARCHAR(128) NOT NULL,                  -- 地點名稱
    UNIQUE (level_id, parent_id, name)
);

-- 建立 audit_info
WITH
    audit_rows AS (
        INSERT INTO audit_info (id, created_at, updated_at)
            SELECT gen_random_uuid(), now(), now() FROM generate_series(1, 22)
            RETURNING id
    ),
    tw_country AS (
        SELECT id FROM country WHERE code = 'TW'
    ),
    level_county AS (
        SELECT id FROM address_level
        WHERE country_id = (SELECT id FROM tw_country)
          AND name = '縣'
          AND sequence = 1
        LIMIT 1
    ),
    level_city AS (
        SELECT id FROM address_level
        WHERE country_id = (SELECT id FROM tw_country)
          AND name = '市'
          AND sequence = 1
        LIMIT 1
    ),
    cities AS (
        SELECT ROW_NUMBER() OVER () AS rn, name
        FROM (VALUES
                  ('台北'), ('新北'), ('桃園'), ('台中'), ('台南'), ('高雄'),
                  ('基隆'), ('新竹'), ('嘉義'), ('新竹'), ('苗栗'), ('彰化'),
                  ('南投'), ('雲林'), ('嘉義'), ('屏東'), ('宜蘭'), ('花蓮'),
                  ('台東'), ('澎湖'), ('金門'), ('連江')
             ) AS t(name)
    ),
    audits AS (
        SELECT ROW_NUMBER() OVER () AS rn, id
        FROM audit_rows
    )
INSERT INTO address_cell (audit_id, level_id, parent_id, name)
SELECT
    audits.id,
    CASE
        WHEN cities.name IN ('台北', '新北', '桃園', '台中', '台南', '高雄') THEN level_city.id
        ELSE level_county.id
        END,
    NULL,
    cities.name
FROM audits
         JOIN cities ON audits.rn = cities.rn,
     level_city,
     level_county;

-- 地址主表：具體地址實例（包含災害、座標）
CREATE TABLE IF NOT EXISTS address (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    country_id BIGINT NOT NULL REFERENCES country(id) ON DELETE CASCADE,   -- 在程式確保 country_id 一致性
    address_cell_id BIGINT NOT NULL REFERENCES address_cell(id), -- 對應最末層節點
    detail VARCHAR(255),       -- 詳細資訊 (如 "123號3樓5室")
    full_address TEXT NOT NULL, -- 完整地址
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6)
);


-- 角色類型表
CREATE TABLE role_type (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(50) NOT NULL UNIQUE CHECK (name <> ''), -- ex: team_role, task_role, system_role
    description VARCHAR(200)
);
CREATE INDEX idx_role_type_audit_id ON role_type(audit_id);

-- 角色表 (救援隊成員角色)
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    type_id BIGINT NOT NULL REFERENCES role_type(id),   -- 類型
    name VARCHAR(50) NOT NULL CHECK (name <> ''),     -- 角色代碼 (ex: leader, medic, support)
    description TEXT,                     -- 角色說明
    UNIQUE(type_id, name) -- 在同一災害內唯一，但不同災害可重複
);
CREATE INDEX idx_role_audit_id ON role(audit_id);
CREATE INDEX idx_role_type_id ON role(type_id);


-- 帳號控管
CREATE TABLE IF NOT EXISTS account (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    username VARCHAR(64) NOT NULL UNIQUE,
    password_hash VARCHAR(255), -- OAuth2 可為 NULL
    email VARCHAR(128) NOT NULL UNIQUE,
    phone VARCHAR(32),
    google_id VARCHAR(64) UNIQUE, -- Google OAuth2 ID
    nickname VARCHAR(64),
    role_id BIGINT NOT NULL REFERENCES role(id), -- 角色外鍵，必填
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE
);

-- 建立完成後再補上 FK（此時 account 已存在）
ALTER TABLE audit_info
    ADD CONSTRAINT fk_audit_created_by FOREIGN KEY (created_by) REFERENCES account(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_audit_updated_by FOREIGN KEY (updated_by) REFERENCES account(id) ON DELETE SET NULL;


WITH new_audit_type AS (
    INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
),
new_role_type AS (
    INSERT INTO role_type (audit_id, name, description)
        SELECT id, 'ACCOUNT', '帳號角色'
        FROM new_audit_type
        RETURNING id
),
new_audit_roles AS (
    INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now()
        FROM generate_series(1, 3)  -- 👈 想插入幾筆角色就改這裡
        RETURNING id
)
INSERT INTO role (audit_id, type_id, name, description)
SELECT r.id,
       t.id,
       v.name,
       v.description
FROM new_audit_roles r
         JOIN new_role_type t ON true
         JOIN (VALUES
                   ('ROLE_ADMIN', '管理者'),   -- 第1筆
                   ('ROLE_ORGAN', '組織'),     -- 第2筆
                   ('ROLE_USER', '個人')       -- 第3筆
) AS v(name, description)
              ON true
LIMIT 3;  -- 👈 要與上面的 generate_series(1,3) 相同


