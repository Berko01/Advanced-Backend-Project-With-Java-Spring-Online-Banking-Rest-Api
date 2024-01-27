# Kullanılacak temel imaj
FROM openjdk:18

# Çalışma dizini
WORKDIR /app


COPY target/ /app/

CMD ["java", "-jar", "DemoBank_v1-0.0.1-SNAPSHOT.jar"]
