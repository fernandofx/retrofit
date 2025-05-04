Endpoints:
/find/dni -> Busca los datos de la persona por dni utilizando retrofit. </br>
/save/dni -> Busca el DNI en el servicio con retrofit y lo inserta en la base de datos. Si ya existe lanza un error de sql porque el DNI es UNIQUE. </br>
/update/dni/newStatus -> actualiza el estado del DNI (solo puede ser ACTIVE o INACTIVE) Esto podria hacerse por el body pero se están pasan por la URL. <br>
/findAllByStatus/status -> buscar el stattus.

application.properties:
spring.application.name=retrofit
token.api=apis-token-14584.u7rV7qnhMOgPK0JiXV41W77oTeHB5URn
spring.datasource.url=jdbc:postgresql://localhost:5432/persistenciag8
spring.datasource.username=alumno
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update 
