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
