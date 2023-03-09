FROM openjdk:17-alpine
WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY ./src ./src
COPY src/main/resources/jwt/private_key.der private_key.der
COPY src/main/resources/jwt/public_key.der public_key.der

RUN ./mvnw clean package
EXPOSE 8086

ENTRYPOINT ["java", "-jar", "/opt/app/target/ftth-0.0.1-SNAPSHOT.jar" ]