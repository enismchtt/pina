FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /app
COPY pom.xml .

RUN mkdir -p /root/.m2 && \
    echo '<settings><profiles><profile><id>default</id><activation><activeByDefault>true</activeByDefault></activation><properties><maven.compiler.fork>true</maven.compiler.fork></properties></profile></profiles></settings>' > /root/.m2/settings.xml


# Download dependencies separately (for better caching)
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests && \
    ls -la target/ && \
    echo "JAR file build complete!"

FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN ls -la /app/ && \
    echo "JAR file found in runtime image"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]