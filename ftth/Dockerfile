FROM openjdk:17-alpine
WORKDIR /opt/app

COPY ../.mvn .mvn
COPY ../mvnw mvnw
COPY ../pom.xml pom.xml
COPY ../libs libs
COPY ./ftth ftth
COPY ../dms dms

COPY /ftth/src/main/resources/jwt/private_key.der private_key.der
COPY /ftth/src/main/resources/jwt/public_key.der public_key.der

RUN ./mvnw -pl ftth -am clean package
EXPOSE 8086

ENTRYPOINT ["java", "-jar", "/opt/app/ftth/target/ftth-0.0.1-SNAPSHOT.jar" ]