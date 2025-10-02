-- UUID 需啟用 uuid-ossp 擴充套件，只須執行一次
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


-- 共用審計欄位
CREATE TABLE audit_info (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    created_by VARCHAR(100) NOT NULL
);

-- 災害事件
CREATE TABLE disaster (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    status_id BIGINT REFERENCES status(id),         -- 災害狀態 (active, resolved)
    name VARCHAR(255) NOT NULL CHECK (name <> ''),                    -- 災害名稱
    occurred_at DATE NOT NULL,                      -- 發生時間，只存到日 (yyyy-MM-dd)
    location TEXT NOT NULL,                         -- 發生地點
    description TEXT,                               -- 描述
    UNIQUE (name, occurred_at)
);


-- 狀態類型表 (ex: household, rescue_task, rescue_task_item)
CREATE TABLE status_type (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(50) NOT NULL UNIQUE CHECK (name <> ''), -- ex: household, rescue_task
    description TEXT
);

-- 狀態表
CREATE TABLE status (
    id BIGSERIAL PRIMARY KEY,
    disaster_id BIGINT NOT NULL REFERENCES disaster(id) ON DELETE CASCADE, -- 所屬災害
    type_id BIGINT NOT NULL REFERENCES status_type(id) ON DELETE CASCADE,
    code VARCHAR(50) NOT NULL CHECK (code <> ''),        -- ex: pending, completed
    name VARCHAR(100) NOT NULL CHECK (name <> ''),       -- 顯示名稱 (ex: 待處理, 已完成)
    description TEXT,                                    -- 說明
    UNIQUE(disaster_id, type_id, code)                                -- 在同一類型內唯一
);


-- 抽象化的人員表
CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(100) NOT NULL CHECK (name <> ''), -- 姓名
    identification VARCHAR(10) UNIQUE CHECK (name <> ''),          -- 身分證號 (可選)
    phone VARCHAR(50),
    email VARCHAR(100)
);


-- 抽象化的單位表：受災戶、救援隊、其他可能單位
CREATE TABLE unit (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(200) NOT NULL CHECK (name <> ''), -- 單位名稱 (受災戶戶名 or 救援隊名稱)
    address TEXT,                               -- 地址 (家庭住址 or 營地)
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    contact_name VARCHAR(100),
    contact_phone VARCHAR(50)
);

-- 角色類型表
CREATE TABLE role_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE CHECK (name <> ''), -- ex: team_role, task_role, system_role
    description VARCHAR(200)
);

-- 角色表 (救援隊成員角色)
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT NOT NULL REFERENCES disaster(id) ON DELETE CASCADE, -- 所屬災害
    type_id BIGINT NOT NULL REFERENCES role_type(id),   -- 類型
    name VARCHAR(50) NOT NULL CHECK (name <> ''),     -- 角色代碼 (ex: leader, medic, support)
    description TEXT,                     -- 角色說明
    UNIQUE(disaster_id, type_id, name) -- 在同一災害內唯一，但不同災害可重複
);


-- 受災戶
CREATE TABLE household (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    unit_id BIGINT NOT NULL UNIQUE REFERENCES unit(id) ON DELETE CASCADE,
    disaster_id BIGINT REFERENCES disaster(id),   -- 所屬災害事件
    status_id BIGINT REFERENCES status(id),  -- 受災戶狀態
    note TEXT
);


-- 受災戶成員
CREATE TABLE household_member (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    person_id BIGINT NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    household_id BIGINT NOT NULL REFERENCES household(id) ON DELETE CASCADE,
    age INT, -- 年齡
    gender VARCHAR(10), -- 性別
    status_id BIGINT REFERENCES status(id),  -- 個人狀態 (ex: safe, injured, missing)
    note TEXT            -- 備註(病史)
);


-- 救援組織
CREATE TABLE rescue_organization (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(200) NOT NULL UNIQUE CHECK (name <> ''), -- 組織名稱 (例: 某某消防局, 某某 NGO)
    description TEXT                         -- 組織說明
);

-- 救援群組
CREATE TABLE rescue_group (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT NOT NULL REFERENCES disaster(id),  -- 對應災害事件
    organization_id BIGINT NOT NULL REFERENCES rescue_organization(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL CHECK (name <> ''), -- 群組名稱 (例: 物資組, 醫療組、後勤組)
    description TEXT,                         -- 群組說明
    UNIQUE (organization_id, disaster_id, name) -- 同一組織同一災害內名稱唯一
);

-- 救援團隊
CREATE TABLE rescue_team (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    unit_id BIGINT NOT NULL UNIQUE REFERENCES unit(id) ON DELETE CASCADE,
    group_id BIGINT REFERENCES rescue_group(id),          -- 所屬救援群組 (可 NULL 表示獨立團隊)
    status_id BIGINT REFERENCES status(id),                -- 團隊狀態
    contact_email VARCHAR(100)
);


-- 救援團隊成員
CREATE TABLE rescue_member (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    person_id BIGINT NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    team_id BIGINT NOT NULL REFERENCES rescue_team(id) ON DELETE CASCADE,
    organization_id BIGINT REFERENCES rescue_organization(id), -- 所屬組織 (可 NULL 表示志工)
    status_id BIGINT REFERENCES status(id),                -- 成員狀態 (active, inactive)
    role_id BIGINT REFERENCES role(id),          -- 角色
    UNIQUE (person_id, team_id) -- 同一個人同一隊伍只能一筆
);


-- 群組救援任務
CREATE TABLE rescue_group_task (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    group_id BIGINT NOT NULL REFERENCES rescue_group(id),   -- 所屬救援群組
    disaster_id BIGINT NOT NULL REFERENCES disaster(id),  -- 對應災害事件
    name VARCHAR(200) NOT NULL CHECK (name <> ''),      -- 任務名稱
    description TEXT,                                      -- 任務描述
    status_id BIGINT REFERENCES status(id),                -- 任務狀態
    priority INT NOT NULL CHECK (priority >= 1 AND priority <= 5),  -- 1=最高, 5=最低
    assigned_at TIMESTAMP DEFAULT now(),                   -- 指派時間
    completed_at TIMESTAMP,                                 -- 完成時間
    UNIQUE (group_id, name),                            -- 同一群組內名稱唯一
    -- 確保 group_id + disaster_id 一致性
    CONSTRAINT fk_group_task_disaster
    FOREIGN KEY (group_id, disaster_id)
    REFERENCES rescue_group(id, disaster_id)
    ON DELETE CASCADE
);



-- 救援工項
CREATE TABLE rescue_group_task_item (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    task_id BIGINT NOT NULL REFERENCES rescue_group_task(id) ON DELETE CASCADE, -- 所屬群組任務
    name VARCHAR(200) NOT NULL CHECK (name <> ''), -- 工項名稱 (ex: 醫療檢查)
    description TEXT,                                     -- 工項描述
    status_id BIGINT REFERENCES status(id),                -- 工項狀態
    started_at TIMESTAMP,                                 -- 開始時間
    completed_at TIMESTAMP,                                -- 完成時間
    UNIQUE (task_id, name)
);

-- 工項、隊員、角色多對多關聯
CREATE TABLE rescue_group_task_item_member_role (
    audit_id UUID NOT NULL REFERENCES audit_info(id),  -- 審計資訊
    task_item_id BIGINT NOT NULL REFERENCES rescue_group_task_item(id) ON DELETE CASCADE,
    member_id BIGINT NOT NULL REFERENCES rescue_member(id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES role(id),          -- 角色
    PRIMARY KEY (task_item_id, member_id, role_id)
);



-- 物資類型表
CREATE TABLE resource_type (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(100) NOT NULL UNIQUE,       -- 類型名稱 (例：食品, 醫療, 衣物)
    description TEXT                         -- 類型說明
);


-- 物資種類表
CREATE TABLE resource (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    type_id BIGINT NOT NULL REFERENCES resource_type(id) ON DELETE RESTRICT, -- 所屬類型
    name VARCHAR(200) NOT NULL CHECK (name <> ''),  -- 物資名稱 (例：飲用水、帳篷)
    unit VARCHAR(50) NOT NULL CHECK (unit <> ''),   -- 單位 (瓶、箱、包)
    description TEXT,                         -- 說明
    UNIQUE (type_id, name)                           -- 同一類型內名稱唯一
);



-- 物資需求表
CREATE TABLE resource_request (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT NOT NULL REFERENCES disaster(id) ON DELETE CASCADE,
    resource_id BIGINT NOT NULL REFERENCES resource(id),
    quantity INT NOT NULL CHECK (quantity > 0),           -- 需求數量
    requested_by BIGINT REFERENCES rescue_member(id),     -- 提出需求的人 (救援人員)
    requested_at TIMESTAMP NOT NULL DEFAULT now(),        -- 需求時間
    fulfilled BOOLEAN NOT NULL DEFAULT FALSE,             -- 是否已滿足
    fulfilled_at TIMESTAMP,                               -- 滿足時間
    note TEXT
);


CREATE TABLE storage_type (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(50) NOT NULL UNIQUE CHECK (name <> ''), -- ex: central, temporary, team
    description TEXT
);


-- 災害物資庫存站
CREATE TABLE storage (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    type_id BIGINT NOT NULL REFERENCES storage_type(id), -- 倉庫型別
    status_id BIGINT REFERENCES status(id),                -- 儲存站狀態 (active, inactive)
    name VARCHAR(200) NOT NULL CHECK (name <> ''),
    address TEXT,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    contact_name VARCHAR(100),
    contact_phone VARCHAR(50),
    capacity INT NOT NULL CHECK (capacity > 0),     -- 可存放總容量
    UNIQUE (type_id, name)                             -- 同一類型內名稱唯一
);


-- 每個儲存站的物資庫存
CREATE TABLE storage_inventory (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT NOT NULL REFERENCES disaster(id) ON DELETE CASCADE,
    storage_id BIGINT NOT NULL REFERENCES storage(id) ON DELETE CASCADE,
    resource_id BIGINT NOT NULL REFERENCES resource(id),
    quantity INT NOT NULL CHECK (quantity > 0),          -- 分配數量,
    UNIQUE(storage_id, resource_id)        -- 避免重複紀錄
);


-- 物資分配紀錄
CREATE TABLE resource_distribution (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT NOT NULL REFERENCES disaster(id) ON DELETE CASCADE,
    resource_id BIGINT NOT NULL REFERENCES resource(id),
    quantity INT NOT NULL CHECK (quantity > 0),          -- 分配數量
    delivered_by BIGINT NOT NULL REFERENCES rescue_member(id),     -- 執行分配的人 (救援人員)
    recipient_unit_id BIGINT REFERENCES unit(id),
    recipient_person_id BIGINT REFERENCES person(id),     -- 領取人 (可 NULL 表示非 person 成員，最好是直接建person紀錄 )
    delivered_at TIMESTAMP NOT NULL DEFAULT now(),        -- 分配時間
    note TEXT,
    CHECK (recipient_unit_id IS NOT NULL OR recipient_person_id IS NOT NULL)
);

