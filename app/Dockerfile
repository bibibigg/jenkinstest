# 사용할 베이스 이미지 지정
FROM openjdk:17

# 작업 디렉토리 설정vi
WORKDIR /app

# Gradle 빌드를 실행하여 JAR 파일 생성
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle
COPY gradlew /app/
RUN chmod +x /app/gradlew
RUN ./gradlew build

# 애플리케이션 JAR 파일 복사
COPY build/libs/app-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 실행 명령
CMD ["java", "-jar", "app.jar"]