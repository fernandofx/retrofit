Participante: LUIS FERNANDO DE LA FLOR VARGAS

Endpoints:
/find/dni -> Busca los datos de la persona por dni utilizando retrofit. </br>
/save/dni -> Busca el DNI en el servicio con retrofit y lo inserta en la base de datos. Si ya existe lanza un error de sql porque el DNI es UNIQUE. </br>
/update/dni/newStatus -> actualiza el estado del DNI (solo puede ser ACTIVE o INACTIVE) Esto podria hacerse por el body pero se están pasan por la URL. <br>
/findAllByStatus/status -> buscar todos los dni que tengan el stattus enviado como PathVariable.

application.properties:<br>
spring.application.name=retrofit<br>
token.api=apis-token-14584.u7rV7qnhMOgPK0JiXV41W77oTeHB5URn<br>
spring.datasource.url=jdbc:postgresql://localhost:5432/persistenciag8<br>
spring.datasource.username=alumno<br>
spring.datasource.password=123456<br>
spring.jpa.hibernate.ddl-auto=update 
