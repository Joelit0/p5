# P5
Aplicación web que usa Spring JPA para persistir los datos de un API REST de gestión de usuarios.
El API permite el registro de nuevos usuarios y su identificación mediante email y password.
Una vez identificados, se emplea una cookie de sesión para autenticar las peticiones que permiten 
a los usuarios leer, modificar y borrar sus datos. También existe un endpoint para cerrar la sesión.  

## Endpoints

// TODO#1: rellena la tabla siguiente analizando el código del proyecto

| Método | Ruta | Descripción | Respuestas |
|--------|------|-------------|------------|
|   POST     |    /api/users  |       Registro de un nuevo usaurio      |     201 Created, 409 Conflict       |
|     POST   |   /api/users/me/session   |       Inicio de sesión      |      201 Created, 401 Unauthorized      |
|   DELETE     |   /api/users/me/session   |        Cierre de sesión     |    204 No Content, 401 Unauthorized        |
|   GET     |   /api/users/me   |       Obtener perfil de un usuario logueado      |   200 OK, 401 Unauthorized         |
|     PUT   |   /api/users/me   |       Actualizar perfil  de un usuario logueado     |   200 OK, 401 Unauthorized         |
|   DELETE     |    /api/users/me  |      Dar de baja un usuario logueado       |     204 No Content, 401 Unauthorized       |


## Comandos 

- Construcción: 
  ```sh
  ./mvnw clean package
  ```

- Ejecución: 
  ```sh
  ./mvnw spring-boot:run
  ```

- Tests:
  ```sh
  ./mvnw test
  ```
