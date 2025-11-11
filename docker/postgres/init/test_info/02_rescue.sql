WITH
    -- 建立「台北市中正區忠孝東路一段1號」address，補建 address_cell（中正區、忠孝東路、一段、1號）
    audit_dist AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    audit_road AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    audit_number AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    audit_unit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    audit_rescue_org AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),

    tw_country AS (
        SELECT id FROM country WHERE code = 'TW'
    ),
    taipei_cell AS (
        SELECT id FROM address_cell WHERE name = '台北' AND parent_id IS NULL LIMIT 1
    ),
    level_dist AS (
        SELECT id FROM address_level WHERE name = '區' AND country_id = (SELECT id FROM tw_country) LIMIT 1
    ),
    level_road AS (
        SELECT id FROM address_level WHERE name = '路' AND suffix = '一段' AND country_id = (SELECT id FROM tw_country) LIMIT 1
    ),
    level_number AS (
        SELECT id FROM address_level WHERE name = '號' AND country_id = (SELECT id FROM tw_country) LIMIT 1
    ),
    ins_dist AS (
        INSERT INTO address_cell (audit_id, level_id, parent_id, name)
        VALUES (
                (SELECT id FROM audit_dist),
                (SELECT id FROM level_dist),
                (SELECT id FROM taipei_cell),
                '中正'
               )
        RETURNING id
    ),
    ins_road AS (
        INSERT INTO address_cell (audit_id, level_id, parent_id, name)
        VALUES (
                (SELECT id FROM audit_road),
                (SELECT id FROM level_road),
                (SELECT id FROM ins_dist),
                '忠孝東'
               )
        RETURNING id
    ),
    ins_number AS (
        INSERT INTO address_cell (audit_id, level_id, parent_id, name)
        VALUES (
                (SELECT id FROM audit_number),
                (SELECT id FROM level_number),
                (SELECT id FROM ins_road),
                '1號'
               )
        RETURNING id
    ),
    latest_audit AS (
        SELECT id FROM audit_number
    ),
    address_insert AS (
        INSERT INTO address (
            audit_id,
            country_id,
            address_cell_id,
            detail,
            full_address
        ) VALUES (
            (SELECT id FROM latest_audit),
            (SELECT id FROM tw_country),
            (SELECT id FROM ins_number),
            '1號',
            '台北市中正區忠孝東路一段1號'
        )
        RETURNING id
    ),


    -- 建立基本 unit 及 organization
    unit_insert AS (
        INSERT INTO unit (
            audit_id, country_id, name, location, contact_name, contact_phone
        ) VALUES (
            (SELECT id FROM audit_unit),
            (SELECT id FROM tw_country),
            '中央災害應變中心',
            (SELECT id FROM address_insert),
            '陳XX 專員',
            '0912345678'
        )
        RETURNING id
    ),
    disaster AS (
        SELECT id FROM disaster WHERE name = '花蓮馬太鞍堰塞湖潰壩' LIMIT 1
    ),
    rescue_org_insert AS (
        INSERT INTO rescue_organization (
            audit_id, disaster_id, unit_id, description
        ) VALUES (
            (SELECT id FROM audit_rescue_org),
            (SELECT id FROM disaster),
            (SELECT id FROM unit_insert),
            '中央災害應變中心(花蓮馬太鞍堰塞湖)'
        )
        RETURNING id
    ),


    -- 建立基本 group 及 所屬 task + taskItems
    group_resource_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_resource AS (
        INSERT INTO rescue_group (audit_id, disaster_id, organization_id, name, description)
        VALUES (
            (SELECT id FROM group_resource_audit),
            (SELECT id FROM disaster),
            (SELECT id FROM rescue_org_insert),
            '物資膳勤組',
            '負責物資分配與現場管理'
        ) RETURNING id
    ),
    group_resource_task1_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_resource_task1 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_resource_task1_audit),
            (SELECT id FROM group_resource),
            (SELECT id FROM disaster),
            '物資採購分發',
            '負責物資採購與分發',
            NULL, 2, 2, 10, now(), NULL
        ) RETURNING id
    ),
    group_resource_task2_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_resource_task2 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_resource_task2_audit),
            (SELECT id FROM group_resource),
            (SELECT id FROM disaster),
            '團膳',
            '負責現場團體膳食供應',
            NULL, 2, 2, 8, now(), NULL
        ) RETURNING id
    ),
    group_resource_task3_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_resource_task3 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_resource_task3_audit),
            (SELECT id FROM group_resource),
            (SELECT id FROM disaster),
            '送餐送水',
            '負責現場送餐與送水',
            NULL, 2, 2, 8, now(), NULL
        ) RETURNING id
    ),
    group_resource_task4_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_resource_task4 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_resource_task4_audit),
            (SELECT id FROM group_resource),
            (SELECT id FROM disaster),
            '垃圾清運',
            '負責現場垃圾收集及清運',
            NULL, 2, 2, 6, now(), NULL
        ) RETURNING id
    ),
    group_resource_task5_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_resource_task5 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_resource_task5_audit),
            (SELECT id FROM group_resource),
            (SELECT id FROM disaster),
            '流動廁所清掃',
            '負責流動廁所清掃',
            NULL, 2, 2, 4, now(), NULL
        ) RETURNING id
    ),



    -- 醫療組
    group_medical_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_medical AS (
        INSERT INTO rescue_group (audit_id, disaster_id, organization_id, name, description)
        VALUES (
            (SELECT id FROM group_medical_audit),
            (SELECT id FROM disaster),
            (SELECT id FROM rescue_org_insert),
            '醫療組',
            '負責醫療救護與健康管理'
        ) RETURNING id
    ),
    group_medical_task1_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_medical_task1 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_medical_task1_audit),
            (SELECT id FROM group_medical),
            (SELECT id FROM disaster),
            '緊急救護',
            '負責現場醫療救護',
            NULL, 1, 2, 8, now(), NULL
        ) RETURNING id
    ),
    group_medical_task2_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_medical_task2 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_medical_task2_audit),
            (SELECT id FROM group_medical),
            (SELECT id FROM disaster),
            '現場人員健康狀況管理',
            '負責現場人員健康管理',
            NULL, 2, 2, 6, now(), NULL
        ) RETURNING id
    ),



    -- 交通組
    group_traffic_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_traffic AS (
        INSERT INTO rescue_group (audit_id, disaster_id, organization_id, name, description)
        VALUES (
            (SELECT id FROM group_traffic_audit),
            (SELECT id FROM disaster),
            (SELECT id FROM rescue_org_insert),
            '交通組',
            '負責現場交通管理'
        ) RETURNING id
    ),
    group_traffic_task1_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_traffic_task1 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_traffic_task1_audit),
            (SELECT id FROM group_traffic),
            (SELECT id FROM disaster),
            '管理並確保現場交通順暢',
            '負責現場交通順暢與安全',
            NULL, 2, 2, 6, now(), NULL
        ) RETURNING id
    ),



    -- 清淤組
    group_cleanup_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_cleanup AS (
        INSERT INTO rescue_group (audit_id, disaster_id, organization_id, name, description)
        VALUES (
            (SELECT id FROM group_cleanup_audit),
            (SELECT id FROM disaster),
            (SELECT id FROM rescue_org_insert),
            '清淤組',
            '負責災區清理與環境恢復'
        ) RETURNING id
    ),
    group_cleanup_task1_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_cleanup_task1 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_cleanup_task1_audit),
            (SELECT id FROM group_cleanup),
            (SELECT id FROM disaster),
            '操作機具清淤(挖土機、小山貓)',
            '負責操作機具進行清淤',
            NULL, 2, 2, 8, now(), NULL
        ) RETURNING id
    ),
    group_cleanup_task2_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_cleanup_task2 AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_cleanup_task2_audit),
            (SELECT id FROM group_cleanup),
            (SELECT id FROM disaster),
            '人力清淤(住宅、水溝)',
            '負責人力清淤住宅及水溝',
            NULL, 2, 2, 10, now(), NULL
        ) RETURNING id
    )
SELECT 1;
