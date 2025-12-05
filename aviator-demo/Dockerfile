# 多阶段构建 Dockerfile（备用方案）
# 使用方法: docker build -t aviator-demo:latest .

# 阶段1: 构建应用
FROM gradle:7.6-jdk8 AS build
WORKDIR /app

# 复制 Gradle 配置文件
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

# 下载依赖（利用 Docker 缓存）
RUN gradle dependencies --no-daemon || return 0

# 复制源代码
COPY src ./src

# 构建应用
RUN gradle bootJar --no-daemon -x test

# 阶段2: 运行时镜像
FROM eclipse-temurin:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 创建非 root 用户
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

# 从构建阶段复制 jar 文件
COPY --from=build /app/build/libs/*.jar app.jar

# 修改文件所有者
RUN chown -R appuser:appuser /app

# 切换到非 root 用户
USER appuser

# 暴露端口
EXPOSE 8080

# JVM 优化参数
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

