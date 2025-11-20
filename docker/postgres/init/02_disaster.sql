-- 災害事件
CREATE TABLE disaster (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id),
    country_id BIGINT NOT NULL REFERENCES country(id) ON DELETE RESTRICT, -- 國籍
    status VARCHAR(30) NOT NULL,                             -- 災害狀態 (ACTIVE, RESOLVED)
    name VARCHAR(255) NOT NULL CHECK (name <> ''),                    -- 災害名稱
    occurred_at DATE NOT NULL,                      -- 發生時間，只存到日 (yyyy-MM-dd)
    location BIGINT NOT NULL REFERENCES address(id) ON DELETE RESTRICT, -- 發生地點
    description TEXT,                               -- 描述
    UNIQUE (name, occurred_at)
);
CREATE INDEX idx_disaster_audit_id ON disaster(audit_id);
CREATE INDEX idx_disaster_occurred_at ON disaster(occurred_at);


-- 狀態類型表 (ex: household, rescue_task, rescue_task_item)
CREATE TABLE status_type (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(50) NOT NULL UNIQUE CHECK (name ~ '^[A-Z0-9_]+$'), -- ex: HOUSEHOLD, TASK，必須全大寫且不得有空格
    description TEXT
);
CREATE INDEX idx_status_type_audit_id ON status_type(audit_id);

-- 狀態表
CREATE TABLE status (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT REFERENCES disaster(id) ON DELETE CASCADE, -- 所屬災害
    type_id BIGINT NOT NULL REFERENCES status_type(id) ON DELETE CASCADE,
    code VARCHAR(50) NOT NULL CHECK (code ~ '^[A-Z0-9_]+$'),        -- ex: PENDING, COMPLETED，必須全大寫且不得有空格
    name VARCHAR(100) NOT NULL CHECK (name <> ''),       -- 顯示名稱 (ex: 待處理, 已完成)
    description TEXT,                                    -- 說明
    UNIQUE(disaster_id, type_id, code)                                -- 在同一類型內唯一
);
CREATE INDEX idx_status_disaster_id ON status(disaster_id);
CREATE INDEX idx_status_type_id ON status(type_id);

-- task status, status_type 要移到 init SQL
WITH
    -- 任務狀態
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
        INSERT INTO status (audit_id, type_id, code, name, description)
            SELECT audits.id,
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
    ),

    -- 團隊狀態
    team_type_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
            VALUES (gen_random_uuid(), now(), now())
            RETURNING id
    ),
    team_type AS (
        INSERT INTO status_type (audit_id, name, description)
            SELECT a.id, UPPER(REPLACE('TEAM', ' ', '')), 'Rescue Team 狀態類型'
            FROM team_type_audit a
            RETURNING id
    ),
    team_status_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
            SELECT gen_random_uuid(), now(), now()
            FROM generate_series(1, 4)
            RETURNING id
    ),
    team_status_list AS (
        SELECT * FROM (
                          VALUES
                              ('ASSIGNED',     '已指派',       '已分配任務但尚未出發'),
                              ('DEPLOYED',     '出動中',       '已前往或正在前往現場'),
                              ('ON_SCENE',     '在場',         '已到達現場並執行救援'),
                              ('RETURNED',     '已返回',       '任務結束已返回基地/營地'),
                              ('COMPLETED',    '已完成',       '任務完成並結案'),
                              ('UNAVAILABLE',    '已解散',       '團隊已解散')
                      ) AS t(code, name, description)
    ),
    team_status AS (
        INSERT INTO status (audit_id, type_id, code, name, description)
            SELECT audits.id,
                   t.id,
                   UPPER(REPLACE(item.code, ' ', '')),
                   item.name,
                   item.description
            FROM (
                     SELECT id, row_number() OVER () AS rn FROM team_status_audits
                 ) audits
                     CROSS JOIN team_type t
                     JOIN (
                SELECT code, name, description, row_number() OVER () AS rn FROM team_status_list
            ) item ON audits.rn = item.rn
            RETURNING id
    )
select 1;


-- 抽象化的人員表
CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    country_id BIGINT NOT NULL REFERENCES country(id) ON DELETE RESTRICT, -- 國籍
    name VARCHAR(100) NOT NULL CHECK (name <> ''), -- 姓名
    identification VARCHAR(10) UNIQUE CHECK (name <> ''),          -- 身分證號 (可選)
    age INT, -- 年齡
    gender VARCHAR(10), -- 性別
    phone VARCHAR(50),
    email VARCHAR(100),
--     skill TEXT,          -- 專長 TODO 待新增
    note TEXT            -- 備註(病史)
);
CREATE INDEX idx_person_audit_id ON person(audit_id);


-- 抽象化的單位表：受災戶、救援隊、其他可能單位
CREATE TABLE unit (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    country_id BIGINT NOT NULL REFERENCES country(id) ON DELETE RESTRICT, -- 國籍
    name VARCHAR(200) NOT NULL CHECK (name <> ''), -- 單位名稱 (受災戶戶名 or 救援隊名稱)
    location BIGINT NOT NULL REFERENCES address(id) ON DELETE RESTRICT, -- 地址 (家庭住址 or 營地)
    contact_name VARCHAR(100),
    contact_phone VARCHAR(50)
);
CREATE INDEX idx_unit_audit_id ON unit(audit_id);


-- 受災戶
CREATE TABLE household (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    unit_id BIGINT NOT NULL UNIQUE REFERENCES unit(id) ON DELETE CASCADE,
    disaster_id BIGINT REFERENCES disaster(id),   -- 所屬災害事件
    status_id BIGINT REFERENCES status(id),  -- 受災戶狀態
    note TEXT
);
CREATE INDEX idx_household_audit_id ON household(audit_id);
CREATE INDEX idx_household_unit_id ON household(unit_id);
CREATE INDEX idx_household_disaster_id ON household(disaster_id);
CREATE INDEX idx_household_status_id ON household(status_id);



-- 受災戶成員
CREATE TABLE household_member (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    person_id BIGINT NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    household_id BIGINT NOT NULL REFERENCES household(id) ON DELETE CASCADE,
    status_id BIGINT REFERENCES status(id)  -- 個人狀態 (ex: safe, injured, missing)
);
CREATE INDEX idx_household_member_audit_id ON household_member(audit_id);
CREATE INDEX idx_household_member_person_id ON household_member(person_id);
CREATE INDEX idx_household_member_household_id ON household_member(household_id);
CREATE INDEX idx_household_member_status_id ON household_member(status_id);



-- 救援組織
CREATE TABLE rescue_organization (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT NOT NULL REFERENCES disaster(id),  -- 對應災害事件
    unit_id BIGINT NOT NULL UNIQUE REFERENCES unit(id) ON DELETE CASCADE,
    description TEXT                         -- 組織說明
);
CREATE INDEX idx_rescue_org_audit_id ON rescue_organization(audit_id);

-- 救援群組
CREATE TABLE rescue_group (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT NOT NULL REFERENCES disaster(id),  -- 對應災害事件
    organization_id BIGINT NOT NULL REFERENCES rescue_organization(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL CHECK (name <> ''), -- 群組名稱 (例: 物資組, 醫療組、後勤組)
    description TEXT,                         -- 群組說明
    UNIQUE (organization_id, disaster_id, name), -- 同一組織同一災害內名稱唯一
    UNIQUE (id, disaster_id) -- 確保 id + disaster_id 一致性 (方便外鍵參照
);
CREATE INDEX idx_rescue_group_audit_id ON rescue_group(audit_id);
CREATE INDEX idx_rescue_group_disaster_id ON rescue_group(disaster_id);
CREATE INDEX idx_rescue_group_org_id ON rescue_group(organization_id);

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
    min_member BIGINT NOT NULL CHECK (min_member > 0), -- 最少成員數
    max_member BIGINT NOT NULL CHECK (max_member >= min_member), -- 最多成員數
    assigned_at TIMESTAMPTZ DEFAULT now(),                   -- 指派時間
    completed_at TIMESTAMPTZ,                                 -- 完成時間
    UNIQUE (group_id, name),                            -- 同一群組內名稱唯一
    -- 確保 group_id + disaster_id 一致性
    CONSTRAINT fk_group_task_disaster
    FOREIGN KEY (group_id, disaster_id)
    REFERENCES rescue_group(id, disaster_id)
    ON DELETE CASCADE
);
CREATE INDEX idx_group_task_audit_id ON rescue_group_task(audit_id);
CREATE INDEX idx_group_task_group_id ON rescue_group_task(group_id);
CREATE INDEX idx_group_task_disaster_id ON rescue_group_task(disaster_id);
CREATE INDEX idx_group_task_status_id ON rescue_group_task(status_id);
CREATE INDEX idx_group_task_priority ON rescue_group_task(priority);


-- 救援工項
CREATE TABLE rescue_group_task_item (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    task_id BIGINT NOT NULL REFERENCES rescue_group_task(id) ON DELETE CASCADE, -- 所屬群組任務
    name VARCHAR(200) NOT NULL CHECK (name <> ''), -- 工項名稱 (ex: 醫療檢查)
    description TEXT,                                     -- 工項描述
    --     skills TEXT,                                          -- 所需專長  TODO 待新增
    status_id BIGINT NOT NULL REFERENCES status(id),                -- 工項狀態
    started_at TIMESTAMPTZ,                                 -- 開始時間
    completed_at TIMESTAMPTZ,                                -- 完成時間
    UNIQUE (task_id, name)
);
CREATE INDEX idx_task_item_audit_id ON rescue_group_task_item(audit_id);
CREATE INDEX idx_task_item_task_id ON rescue_group_task_item(task_id);
CREATE INDEX idx_task_item_status_id ON rescue_group_task_item(status_id);

-- 救援團隊
CREATE TABLE rescue_team (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    unit_id BIGINT NOT NULL UNIQUE REFERENCES unit(id) ON DELETE CASCADE,
    task_id BIGINT NOT NULL REFERENCES rescue_group_task(id) ON DELETE CASCADE, -- 所屬任務，必須登記在某個群組任務下
    status_id BIGINT REFERENCES status(id),                -- 團隊狀態
    min_member BIGINT NOT NULL CHECK (min_member >= 0), -- 最少成員數
    max_member BIGINT NOT NULL CHECK (max_member >= min_member) -- 最多成員數
);
CREATE INDEX idx_rescue_team_audit_id ON rescue_team(audit_id);
CREATE INDEX idx_rescue_team_unit_id ON rescue_team(unit_id);
CREATE INDEX idx_rescue_team_task_id ON rescue_team(task_id);
CREATE INDEX idx_rescue_team_status_id ON rescue_team(status_id);

-- 救援團隊成員
CREATE TABLE rescue_team_member (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    person_id BIGINT NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    team_id BIGINT NOT NULL REFERENCES rescue_team(id) ON DELETE CASCADE,
    organization_id BIGINT REFERENCES rescue_organization(id), -- 所屬組織 (可 NULL 表示志工)
    status_id BIGINT REFERENCES status(id),                -- 成員狀態 (active, inactive)
    role_id BIGINT REFERENCES role(id),          -- 角色
    UNIQUE (person_id, team_id) -- 同一個人同一隊伍只能一筆
);
CREATE INDEX idx_rescue_team_member_audit_id ON rescue_team_member(audit_id);
CREATE INDEX idx_rescue_team_member_person_id ON rescue_team_member(person_id);
CREATE INDEX idx_rescue_team_member_team_id ON rescue_team_member(team_id);
CREATE INDEX idx_rescue_team_member_org_id ON rescue_team_member(organization_id);
CREATE INDEX idx_rescue_team_member_status_id ON rescue_team_member(status_id);
CREATE INDEX idx_rescue_team_member_role_id ON rescue_team_member(role_id);


-- 物資類型表
CREATE TABLE resource_type (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(100) NOT NULL UNIQUE,       -- 類型名稱 (例：食品, 醫療, 衣物)
    description TEXT                         -- 類型說明
);
CREATE INDEX idx_resource_type_audit_id ON resource_type(audit_id);

-- 物資種類表
CREATE TABLE resource (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    type_id BIGINT NOT NULL REFERENCES resource_type(id) ON DELETE RESTRICT, -- 所屬類型
    name VARCHAR(200) NOT NULL CHECK (name <> ''),  -- 物資名稱 (例：飲用水、帳篷)
    unit VARCHAR(50) NOT NULL CHECK (unit <> ''),   -- 計數單位 (瓶、箱、包)
    description TEXT,                         -- 說明
    UNIQUE (type_id, name)                           -- 同一類型內名稱唯一
);
CREATE INDEX idx_resource_audit_id ON resource(audit_id);
CREATE INDEX idx_resource_type_id ON resource(type_id);

-- 物資需求表
CREATE TABLE resource_request (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT NOT NULL REFERENCES disaster(id) ON DELETE CASCADE,
    resource_id BIGINT NOT NULL REFERENCES resource(id),
    quantity INT NOT NULL CHECK (quantity > 0),           -- 需求數量
    requested_by BIGINT REFERENCES person(id),     -- 提出需求的人 (救援人員)
    requested_at TIMESTAMPTZ NOT NULL DEFAULT now(),        -- 需求時間
    fulfilled BOOLEAN NOT NULL DEFAULT FALSE,             -- 是否已滿足
    fulfilled_at TIMESTAMPTZ,                               -- 滿足時間
    note TEXT
);
CREATE INDEX idx_resource_request_audit_id ON resource_request(audit_id);
CREATE INDEX idx_resource_request_disaster_id ON resource_request(disaster_id);
CREATE INDEX idx_resource_request_resource_id ON resource_request(resource_id);
CREATE INDEX idx_resource_request_requested_by ON resource_request(requested_by);
CREATE INDEX idx_resource_request_fulfilled ON resource_request(fulfilled);


CREATE TABLE storage_type (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    name VARCHAR(50) NOT NULL UNIQUE CHECK (name <> ''), -- ex: central, temporary, team
    description TEXT
);
CREATE INDEX idx_storage_type_audit_id ON storage_type(audit_id);

-- 災害物資庫存站
CREATE TABLE storage (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    country_id BIGINT NOT NULL REFERENCES country(id) ON DELETE RESTRICT, -- 所在國家
    type_id BIGINT NOT NULL REFERENCES storage_type(id), -- 倉庫型別
    status_id BIGINT REFERENCES status(id),                -- 儲存站狀態 (active, inactive)
    name VARCHAR(200) NOT NULL CHECK (name <> ''),
    location BIGINT NOT NULL REFERENCES address(id) ON DELETE RESTRICT, -- 地址
    contact_name VARCHAR(100),
    contact_phone VARCHAR(50),
    capacity INT NOT NULL CHECK (capacity > 0),     -- 可存放總容量
    UNIQUE (type_id, name)                             -- 同一類型內名稱唯一
);
CREATE INDEX idx_storage_audit_id ON storage(audit_id);
CREATE INDEX idx_storage_type_id ON storage(type_id);
CREATE INDEX idx_storage_status_id ON storage(status_id);

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
CREATE INDEX idx_storage_inventory_audit_id ON storage_inventory(audit_id);
CREATE INDEX idx_storage_inventory_disaster_id ON storage_inventory(disaster_id);
CREATE INDEX idx_storage_inventory_storage_id ON storage_inventory(storage_id);
CREATE INDEX idx_storage_inventory_resource_id ON storage_inventory(resource_id);

-- 物資分配紀錄
CREATE TABLE resource_distribution (
    id BIGSERIAL PRIMARY KEY,
    audit_id UUID NOT NULL REFERENCES audit_info(id), -- 審計資訊
    disaster_id BIGINT NOT NULL REFERENCES disaster(id) ON DELETE CASCADE,
    resource_id BIGINT NOT NULL REFERENCES resource(id),
    quantity INT NOT NULL CHECK (quantity > 0),          -- 分配數量
    delivered_by BIGINT NOT NULL REFERENCES rescue_team_member(id),     -- 執行分配的人 (救援人員)
    recipient_unit_id BIGINT REFERENCES unit(id),
    recipient_person_id BIGINT REFERENCES person(id),     -- 領取人 (可 NULL 表示非 person 成員，最好是直接建person紀錄 )
    delivered_at TIMESTAMPTZ NOT NULL DEFAULT now(),        -- 分配時間
    note TEXT,
    CHECK (recipient_unit_id IS NOT NULL OR recipient_person_id IS NOT NULL)
);
CREATE INDEX idx_resource_dist_audit_id ON resource_distribution(audit_id);
CREATE INDEX idx_resource_dist_disaster_id ON resource_distribution(disaster_id);
CREATE INDEX idx_resource_dist_resource_id ON resource_distribution(resource_id);
CREATE INDEX idx_resource_dist_delivered_by ON resource_distribution(delivered_by);
CREATE INDEX idx_resource_dist_recipient_unit_id ON resource_distribution(recipient_unit_id);
CREATE INDEX idx_resource_dist_recipient_person_id ON resource_distribution(recipient_person_id);

