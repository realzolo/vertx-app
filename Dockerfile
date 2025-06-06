FROM eclipse-temurin:21-jre-jammy

# 设置工作目录
RUN mkdir -p /opt/vertx-app
WORKDIR /opt/vertx-app

# 复制Jar包到镜像中
COPY vertx-app/target/*.jar app.jar

## 设置 TZ 时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
## 设置 JAVA_OPTS 环境变量
ENV JAVA_OPTS="-Xms512m -Xmx2048m"
## 应用参数
ENV ARGS="--spring.profiles.active=prod"

# 暴露端口
EXPOSE 10240

## 启动后端项目
CMD java ${JAVA_OPTS} -jar app.jar $ARGS
