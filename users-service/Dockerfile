FROM openjdk:11.0.15
COPY ["target/booking-app-user-1.0.0-SNAPSHOT.jar","/target/user.jar"]
COPY ["target/classes/db.migration/V1_0_0__creating_user.sql","/target/db.migration/V1_0_0__creating_user.sql"]
CMD  ["java","-jar","/target/user.jar"]