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
    task_in_progress_status_id AS (
        SELECT s.id
        FROM status s
                 JOIN status_type t ON s.type_id = t.id
        WHERE t.name = 'TASK'
          AND s.code = 'IN_PROGRESS'
        LIMIT 1
    ),
    task_completed_status_id AS (
        SELECT s.id
        FROM status s
                 JOIN status_type t ON s.type_id = t.id
        WHERE t.name = 'TASK'
          AND s.code = 'COMPLETED'
        LIMIT 1
    ),
    task_cancelled_status_id AS (
        SELECT s.id
        FROM status s
                 JOIN status_type t ON s.type_id = t.id
        WHERE t.name = 'TASK'
          AND s.code = 'CANCELLED'
        LIMIT 1
    ),
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
            (SELECT id FROM task_in_progress_status_id),
            1, 2, 10, now(), NULL
        ) RETURNING id
    ),
    group_resource_task1_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now() FROM generate_series(1,3)
        RETURNING id
    ),
    group_resource_task1_item_list AS (
        SELECT * FROM (
            VALUES
                ('捐贈物資管理', '適合熟悉 Excel 或文書處理，協助登記、盤點、管理捐贈物資'),
                ('物資採購', '適合有採購經驗或願意外出比價、聯絡供應商者，具備機車駕照者佳'),
                ('物資分發', '適合具備溝通協調能力，部分角色可公開聯絡資訊以方便外界聯絡')
        ) AS t(name, description)
    ),
    group_resource_task1_items AS (
        INSERT INTO rescue_group_task_item (
            audit_id, task_id, name, description, status_id, started_at, completed_at)
        SELECT
            audits.id,
            (SELECT id FROM group_resource_task1),
            item.name,
            item.description,
            (SELECT id FROM task_in_progress_status_id),
            NULL, NULL
        FROM
            (SELECT id, row_number() OVER () AS rn FROM group_resource_task1_audits) audits
        JOIN
            (SELECT name, description, row_number() OVER () AS rn FROM group_resource_task1_item_list) item
        ON audits.rn = item.rn
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
            (SELECT id FROM task_cancelled_status_id),
            1, 2, 8, now(), NULL
        ) RETURNING id
    ),
    group_resource_task2_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now() FROM generate_series(1,3)
        RETURNING id
    ),
    group_resource_task2_item_list AS (
        SELECT * FROM (
            VALUES
                ('食材準備', '適合有食材採購或備料經驗者參與'),
                ('烹飪', '適合有團體烹飪經驗或願意協助大量餐食製作者'),
                ('餐食分配', '適合具備分配與溝通能力，協助現場分配餐食')
        ) AS t(name, description)
    ),
    group_resource_task2_items AS (
        INSERT INTO rescue_group_task_item (
            audit_id,
            task_id,
            name,
            description,
            status_id,
            started_at,
            completed_at
        )
        SELECT audits.id,
               (SELECT id FROM group_resource_task2),
               item.name,
               item.description,
               (SELECT id FROM task_in_progress_status_id),
               NULL, NULL
        FROM (
            SELECT id, row_number() OVER () AS rn FROM group_resource_task2_audits
        ) audits
        JOIN (
            SELECT name, description, row_number() OVER () AS rn FROM group_resource_task2_item_list
        ) item ON audits.rn = item.rn
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
            (SELECT id FROM task_in_progress_status_id),
            3, 2, 8, now(), NULL
        ) RETURNING id
    ),
    group_resource_task3_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now() FROM generate_series(1,2)
        RETURNING id
    ),
    group_resource_task3_item_list AS (
        SELECT * FROM (
            VALUES
                ('物資運送', '適合有機車駕照或願意協助物資運送者'),
                ('現場分發', '適合具備現場分發與溝通能力者')
        ) AS t(name, description)
    ),
    group_resource_task3_items AS (
        INSERT INTO rescue_group_task_item (
            audit_id,
            task_id,
            name,
            description,
            status_id,
            started_at,
            completed_at
        )
        SELECT audits.id,
               (SELECT id FROM group_resource_task3),
               item.name,
               item.description,
               (SELECT id FROM task_in_progress_status_id),
               NULL, NULL
        FROM (
            SELECT id, row_number() OVER () AS rn FROM group_resource_task3_audits
        ) audits
        JOIN (
            SELECT name, description, row_number() OVER () AS rn FROM group_resource_task3_item_list
        ) item ON audits.rn = item.rn
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
            (SELECT id FROM task_in_progress_status_id),
            2, 2, 6, now(), NULL
        ) RETURNING id
    ),
    group_resource_task4_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now() FROM generate_series(1,2)
        RETURNING id
    ),
    group_resource_task4_item_list AS (
        SELECT * FROM (
            VALUES
                ('垃圾收集', '適合願意協助現場收集垃圾者'),
                ('垃圾運送', '適合有機車駕照或願意協助垃圾運送者')
        ) AS t(name, description)
    ),
    group_resource_task4_items AS (
        INSERT INTO rescue_group_task_item (
            audit_id,
            task_id,
            name,
            description,
            status_id,
            started_at,
            completed_at
        )
        SELECT audits.id,
               (SELECT id FROM group_resource_task4),
               item.name,
               item.description,
               (SELECT id FROM task_in_progress_status_id),
               NULL, NULL
        FROM (
            SELECT id, row_number() OVER () AS rn FROM group_resource_task4_audits
        ) audits
        JOIN (
            SELECT name, description, row_number() OVER () AS rn FROM group_resource_task4_item_list
        ) item ON audits.rn = item.rn
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
            (SELECT id FROM task_in_progress_status_id),
            4, 2, 4, now(), NULL
        ) RETURNING id
    ),
    group_resource_task5_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now() FROM generate_series(1,1)
        RETURNING id
    ),
    group_resource_task5_item_list AS (
        SELECT * FROM (
            VALUES
                ('廁所清掃', '適合細心負責、願意協助現場清掃者')
        ) AS t(name, description)
    ),
    group_resource_task5_items AS (
        INSERT INTO rescue_group_task_item (
            audit_id,
            task_id,
            name,
            description,
            status_id,
            started_at,
            completed_at
        )
        SELECT audits.id,
               (SELECT id FROM group_resource_task5),
               item.name,
               item.description,
               (SELECT id FROM task_in_progress_status_id),
               NULL, NULL
        FROM (
            SELECT id, row_number() OVER () AS rn FROM group_resource_task5_audits
        ) audits
        JOIN (
            SELECT name, description, row_number() OVER () AS rn FROM group_resource_task5_item_list
        ) item ON audits.rn = item.rn
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
    group_medical_task_audit AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        VALUES (gen_random_uuid(), now(), now())
        RETURNING id
    ),
    group_medical_task AS (
        INSERT INTO rescue_group_task (
            audit_id, group_id, disaster_id, name, description, status_id, priority, min_member, max_member, assigned_at, completed_at
        ) VALUES (
            (SELECT id FROM group_medical_task_audit),
            (SELECT id FROM group_medical),
            (SELECT id FROM disaster),
            '醫療救護與健康管理',
            '負責現場醫療救護及人員健康管理',
            (SELECT id FROM task_in_progress_status_id),
            2, 2, 10, now(), NULL
        ) RETURNING id
    ),
    group_medical_task_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now() FROM generate_series(1,2)
        RETURNING id
    ),
    group_medical_task_item_list AS (
        SELECT * FROM (
            VALUES
                ('緊急救護', '需具備急救技能與醫療證照，負責現場醫療救護'),
                ('健康狀況管理', '適合具備健康管理或護理相關知識者，協助現場人員健康管理')
        ) AS t(name, description)
    ),
    group_medical_task_items AS (
        INSERT INTO rescue_group_task_item (
            audit_id,
            task_id,
            name,
            description,
            status_id,
            started_at,
            completed_at
        )
        SELECT audits.id,
               (SELECT id FROM group_medical_task),
               item.name,
               item.description,
               (SELECT id FROM task_in_progress_status_id),
               NULL, NULL
        FROM (
            SELECT id, row_number() OVER () AS rn FROM group_medical_task_audits
        ) audits
        JOIN (
            SELECT name, description, row_number() OVER () AS rn FROM group_medical_task_item_list
        ) item ON audits.rn = item.rn
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
            (SELECT id FROM task_in_progress_status_id),
            2, 2, 6, now(), NULL
        ) RETURNING id
    ),
    group_traffic_task1_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now() FROM generate_series(1,2)
        RETURNING id
    ),
    group_traffic_task1_item_list AS (
        SELECT * FROM (
            VALUES
                ('交通指揮', '適合具備現場交通指揮經驗或願意協助人車分流者'),
                ('路線規劃', '適合有路線規劃能力或願意協助救災車輛順利通行者')
        ) AS t(name, description)
    ),
    group_traffic_task1_items AS (
        INSERT INTO rescue_group_task_item (
            audit_id,
            task_id,
            name,
            description,
            status_id,
            started_at,
            completed_at
        )
        SELECT audits.id,
               (SELECT id FROM group_traffic_task1),
               item.name,
               item.description,
               (SELECT id FROM task_in_progress_status_id),
               NULL, NULL
        FROM (
            SELECT id, row_number() OVER () AS rn FROM group_traffic_task1_audits
        ) audits
        JOIN (
            SELECT name, description, row_number() OVER () AS rn FROM group_traffic_task1_item_list
        ) item ON audits.rn = item.rn
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
            (SELECT id FROM task_in_progress_status_id),
            1, 2, 8, now(), NULL
        ) RETURNING id
    ),
    group_cleanup_task1_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now() FROM generate_series(1,1)
        RETURNING id
    ),
    group_cleanup_task1_item_list AS (
        SELECT * FROM (
            VALUES
                ('挖土機清淤', '需具備挖土機或小山貓操作證照')
        ) AS t(name, description)
    ),
    group_cleanup_task1_items AS (
        INSERT INTO rescue_group_task_item (
            audit_id, task_id, name, description, status_id, started_at, completed_at
        )
        SELECT audits.id,
               (SELECT id FROM group_cleanup_task1),
               item.name,
               item.description,
               (SELECT id FROM task_in_progress_status_id),
               NULL, NULL
        FROM (
            SELECT id, row_number() OVER () AS rn FROM group_cleanup_task1_audits
        ) audits
        JOIN (
            SELECT name, description, row_number() OVER () AS rn FROM group_cleanup_task1_item_list
        ) item ON audits.rn = item.rn
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
            (SELECT id FROM task_completed_status_id),
            5, 2, 10, now(), NULL
        ) RETURNING id
    ),
    group_cleanup_task2_audits AS (
        INSERT INTO audit_info (id, created_at, updated_at)
        SELECT gen_random_uuid(), now(), now() FROM generate_series(1,2)
        RETURNING id
    ),
    group_cleanup_task2_item_list AS (
        SELECT * FROM (
            VALUES
                ('住宅清淤', '適合具備體力或現場清理經驗者'),
                ('水溝清淤', '適合有水溝清理經驗或願意配合團隊作業者')
        ) AS t(name, description)
    ),
    group_cleanup_task2_items AS (
        INSERT INTO rescue_group_task_item (
            audit_id,
            task_id,
            name,
            description,
            status_id,
            started_at,
            completed_at
        )
        SELECT audits.id,
               (SELECT id FROM group_cleanup_task2),
               item.name,
               item.description,
               (SELECT id FROM task_in_progress_status_id),
               NULL, NULL
        FROM (
            SELECT id, row_number() OVER () AS rn FROM group_cleanup_task2_audits
        ) audits
        JOIN (
            SELECT name, description, row_number() OVER () AS rn FROM group_cleanup_task2_item_list
        ) item ON audits.rn = item.rn
    )
SELECT 1;
