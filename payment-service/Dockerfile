FROM openjdk:11.0.15
COPY ["target/payments-1.0.0-SNAPSHOT.jar","/target/payment.jar"]
COPY ["target/classes/db.migration/V1_0_0__init_payment.sql","target/db.migration/V1_0_0__init_payment.sql"]
CMD ["java","-jar","/target/payment.jar"]