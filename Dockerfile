FROM openjdk:17
WORKDIR /app

COPY target/bharath-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml application.yml
EXPOSE 8443
# ENV SPRING_DATASOURCE_URL=jdbc:postgresql://14.194.29.26:5432/exceldata2
# ENV SPRING_DATASOURCE_USERNAME=postgres
# ENV SPRING_DATASOURCE_PASSWORD=1811786
CMD ["java","-jar","app.jar"]

