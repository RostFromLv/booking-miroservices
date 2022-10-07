FROM openjdk:11.0.15
COPY ["target/orders-1.0.1-SNAPSHOT.jar","/target/order.jar"]
COPY ["target/classes/db.migration/V1_0_0__init_order.sql","/target/db.migration/V1_0_0__init_order.sql"]
CMD ["java","-jar","/target/order.jar"]