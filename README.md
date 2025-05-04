End points:
/find/dni -> Busca los datos de la persona por dni utilizando retrofit. </br>
/save/dni -> Busca el DNI en el servicio con retrofit y lo inserta en la base de datos. Si ya existe lanza un error de sql porque el DNI es UNIQUE. </br>
/update/dni/newStatus -> actualiza el estado del DNI (solo puede ser ACTIVE o INACTIVE) Esto podria hacerse por el body pero se están pasan por la URL. <br>
/findAllByStatus/status -> buscar el stattus.
