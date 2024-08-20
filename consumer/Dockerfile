# Build fatjar
FROM gradle:8.8.0-jdk21-alpine AS builder
COPY . /app-builder
WORKDIR /app-builder
RUN gradle build
#RUN echo ${PWD} && ls -lR

# Build jar layers from fatjar
FROM eclipse-temurin:21-jre-alpine@sha256:9f1de3e01a3c43e2f158abf408ec761813da639961bde93427c1ea42a619a09b AS layers
WORKDIR /app-layers
COPY --from=builder /app-builder/build/libs/consumer-0.0.1-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Run Spring app
FROM eclipse-temurin:21-jre-alpine@sha256:9f1de3e01a3c43e2f158abf408ec761813da639961bde93427c1ea42a619a09b
WORKDIR /opt/app
RUN addgroup --system appuser && adduser -S -s /usr/sbin/nologin -G appuser appuser

COPY --from=layers /app-layers/dependencies/ ./
COPY --from=layers /app-layers/spring-boot-loader/ ./
COPY --from=layers /app-layers/snapshot-dependencies/ ./
COPY --from=layers /app-layers/application/ ./

RUN chown -R appuser:appuser /opt/app
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
#ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]