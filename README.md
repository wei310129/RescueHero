# 🚑 救援任務派遣平台

因有感於2025年9月發生「花蓮馬太鞍溪堰塞湖事件」時，各類志工與政府沒有一個統一登記分派任務的平台，因此建構此專案，目的是讓組織單位與志工可以統一調度，並且在日後發生其他災害時，志工可以在第一時間取得救災指揮所派發的任務，避免調度混亂造成人力物力的損耗。

本專案為一套「救援任務派遣平台」，提供任務調度、即時通訊、通知推播與資料管理等功能，採用 前後端分離 + 分散式架構 設計，以提升系統可擴展性與可維運性，並且全部使用容器化部署的專案，並且使用分散式架構避免producer或consumer 單點故障導致系統故障，使用Redis 統一管理登錄資訊及快取。

---

## 🖥️ Backend（Java / Spring Boot）

### 🔐 安全與認證
- **Spring Security + JWT**
  - 採用無狀態驗證機制
  - 自訂 `JwtAuthFilter` 攔截請求並驗證 Token
  - 避免 Session 依賴，利於水平擴展

### 🗄️ 資料存取與查詢
- **Spring Data JPA + QueryDSL**
  - 型別安全的動態查詢能力
  - 提升複雜條件查詢可維護性
- **JpaAuditing**
  - 自動記錄資料建立/更新時間與操作者
  - 降低人工維護稽核欄位成本

### 🧩 多資料庫架構
- **PostgreSQL**：關聯式核心業務資料  
- **MongoDB**：非結構化或文件型資料  
- **Redis**：快取與 Session/暫存資料  
- 透過 `DataSourceConfig` 統一管理多資料來源

### ⚡ 即時通訊
- **WebSocket**
  - `WebSocketConfig` 建立雙向通訊管道
  - 用於聊天與即時通知功能

### 📡 事件驅動架構
- **Apache Kafka**
  - 非同步事件處理
  - 解耦核心業務與通知模組
  - Kafka Cluster + Consumer Cluster 避免單點故障

### 🤖 AI Agent（開發中）
- `ai-agent` branch 正在整合 LLM 能力
- 預計提供智慧輔助與自動化決策功能

### 🔒 設定安全
- **Jasypt**
  - 敏感設定（如資料庫密碼）以加密方式儲存於設定檔

---

## 🎨 Frontend（Vue.js）

### 🧭 路由管理
- **Vue Router**
  - SPA 路由控制與頁面導航

### 💬 即時互動
- **ChatWidget**
  - 聊天 UI 元件
  - 搭配 WebSocket 提供即時溝通能力

### 🛠️ 自訂工具
- **apiFetch.js**
  - API 呼叫統一封裝
  - 集中處理 Token、錯誤與攔截邏輯
- **logout.js**
  - 統一登出流程
  - 清除 Token 與狀態管理

---

## 🏗️ Infrastructure / DevOps

### 🐳 容器化部署
- **Docker Compose**
  - 一鍵啟動完整開發環境
  - 包含 Nginx、Spring Boot、PostgreSQL、MongoDB、Redis

### 🌐 網路與安全
- **Nginx**
  - 反向代理
  - SSL Termination（自簽憑證）

### 🔐 多環境隔離
- **Docker Secrets**
  - 敏感資訊集中管理
  - 避免寫死於設定檔或程式碼中

### ♻️ 高可用設計
- Kafka Cluster  
- Java Consumer Cluster  
- 避免服務單點故障並提升訊息處理吞吐量

---

## 📌 系統特色總覽
- 無狀態安全架構（JWT）
- 多資料庫混合儲存策略
- 即時通訊 + 事件驅動
- 容器化與多環境部署能力
- AI Agent 擴展潛力
- 使用 DDD 領域驅動開發

---


# 專案使用說明

## 部屬（僅供測試用，正式環境要重新設定不可commit）
### VM options
```
1.啟用 java 21 preview 功能
--enable-preview
2.略過測試
-Dmaven.test.skip=true
3.Jasypt 加/解密
-Djasypt.encryptor.password=AAALKJPO872349JJMjskkKLfs0ekvZZZ
4.JWT 加/解密
-DJWT_SECRET=AAALKJPO872349JJMjskkKLfs0ekvZZZ
5.多台部屬 prot 不可重複(ex: 8082、8083....)
-Dserver.port=8081 
6.多台部屬 group-id 不可重複，否則相同 group-id 的 listener 只有一台會收到訊息
-Dchat.kafka.group-id.chat=rescuehero-chat-2
```

## Jasypt 加/解密 Maven 插件使用說明
### 加密
```
mvn jasypt:encrypt-value -Djasypt.encryptor.password=yourPassword -Djasypt.plugin.value=originalValue
```
### 解密
```
mvn jasypt:encrypt-value -Djasypt.encryptor.password=yourPassword -Djasypt.plugin.value=originalValue
```
