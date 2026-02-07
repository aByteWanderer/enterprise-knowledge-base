FROM eclipse-temurin:22-jdk-alpine AS builder

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests -Pprod

FROM eclipse-temurin:22-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN apk add --no-cache bash

EXPOSE 8080

# 创建上传目录
RUN mkdir -p /app/uploads/{images,attachments,avatars}

VOLUME ["/app/uploads"]

ENTRYPOINT ["bash", "-c", "java -jar /app/app.jar --spring.profiles.active=prod"]
