FROM openjdk:11.0.15
COPY ["target/address-service-1.0.0-SNAPSHOT.jar","/target/address.jar"]
COPY ["target/classes/db.migration/V1_0_0__creating_addresses.sql","/target/db.migration/V1_0_0__creating_addresses.sql"]
CMD ["java","-jar","/target/address.jar"]