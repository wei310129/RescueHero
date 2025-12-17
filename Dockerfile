# =====================
# Build Stage
# =====================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# 複製 Maven wrapper 和 pom.xml
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY pom.xml pom.xml
COPY .mvn .mvn

# 給予 mvnw 執行權限
RUN chmod +x mvnw

# 下載依賴 (利用 Docker cache)
RUN ./mvnw dependency:go-offline -B

# 複製原始碼
COPY src src

# 打包應用程式 (跳過測試加快建置速度)
RUN ./mvnw package -DskipTests -B

# =====================
# Runtime Stage
# =====================
FROM eclipse-temurin:21-jre

WORKDIR /app

# 建立非 root 用戶執行應用程式 (安全性考量)
RUN groupadd -r spring && useradd -r -g spring spring

# 從 builder 階段複製 JAR
COPY --from=builder /app/target/*.jar app.jar

# 設定檔案擁有者
RUN chown spring:spring app.jar

# 切換到非 root 用戶
USER spring:spring

# 暴露服務端口
EXPOSE 8081

# JVM 優化參數 (包含 --enable-preview 支援 preview 功能)
ENV JAVA_OPTS="--enable-preview -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# 應用程式環境變數 (可在 docker-compose 中覆蓋)
ENV JASYPT_ENCRYPTOR_PASSWORD=""
ENV JWT_SECRET=""
ENV SERVER_PORT=8081
ENV CHAT_KAFKA_GROUP_ID_CHAT=""

# 啟動應用程式
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djasypt.encryptor.password=$JASYPT_ENCRYPTOR_PASSWORD -DJWT_SECRET=$JWT_SECRET -Dserver.port=$SERVER_PORT -Dchat.kafka.group-id.chat=$CHAT_KAFKA_GROUP_ID_CHAT -jar app.jar"]
