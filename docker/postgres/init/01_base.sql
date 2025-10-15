-- UUID 擴充套件
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


-- 共用審計欄位
CREATE TABLE audit_info (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by VARCHAR(100) NOT NULL
);


-- 國家表
CREATE TABLE IF NOT EXISTS country (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    name VARCHAR(64) NOT NULL UNIQUE, -- 國家名稱
    native_name VARCHAR(128),          -- 本地語言名稱（台灣）
    code VARCHAR(8) NOT NULL UNIQUE,  -- 國家代碼 (如 TW, US)
    CHECK (code ~ '^[A-Z]{2,3}$')
);

-- 地址單位表
CREATE TABLE IF NOT EXISTS address_level (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    country_id BIGINT NOT NULL REFERENCES country(id) ON DELETE CASCADE,
    name VARCHAR(32) NOT NULL,                       -- 層級名稱 (ex: 縣市、區、鄉、路、街)
    suffix VARCHAR(32),                              -- 後綴字（如: 一段、二段）
    sequence INTEGER NOT NULL CHECK (sequence > 0),  -- 層級順序
    UNIQUE (country_id, name, sequence)
);

-- 地址欄位表
CREATE TABLE IF NOT EXISTS address_cell (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    level_id BIGINT NOT NULL REFERENCES address_level(id) ON DELETE CASCADE,
    parent_id BIGINT REFERENCES address_cell(id) ON DELETE SET NULL,   -- 上層地點，例如中正區→台北市
    name VARCHAR(128) NOT NULL,                  -- 地點名稱
    UNIQUE (level_id, parent_id, name)
);

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
    phone VARCHAR(32) NOT NULL UNIQUE,
    google_id VARCHAR(64) UNIQUE, -- Google OAuth2 ID
    nickname VARCHAR(64),
    role_id BIGINT NOT NULL REFERENCES role(id), -- 角色外鍵，必填
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE
);