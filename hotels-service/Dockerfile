FROM openjdk:11.0.15
COPY ["target/booking-api-hotel-1.0.1-SNAPSHOT.jar","/target/hotel.jar"]
COPY ["target/classes/db.migration/V1_0_0__init.sql","/target/db.migration/V1_0_0__init.sql"]
COPY ["target/classes/db.migration/V1_0_1__add_Reservation_Table.sql","/target/db.migration/V1_0_1__add_Reservation_Table.sql"]
COPY ["target/classes/db.migration/V1_0_2__remove_StatusColumn_in_HotelRoom.sql","/target/db.migration/V1_0_2__remove_StatusColumn_in_HotelRoom.sql"]
COPY ["target/classes/db.migration/V1_0_3__add_RoomPrice_table.sql","/target/db.migration/V1_0_3__add_RoomPrice_table.sql"]
CMD ["java","-jar","/target/hotel.jar"]