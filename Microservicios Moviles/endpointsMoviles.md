Aquí tienes la lista completa y documentada de los endpoints de cada microservicio, con sus métodos HTTP, rutas, ejemplos de cuerpo JSON (Request) y respuestas (Response).

---

### 1. Microservicio: `Usuario-service`
* **Puerto:** `8081`
* **URL Base:** `http://localhost:8081/usuarios` *(Desde emulador Android: `http://10.0.2.2:8081/usuarios`)*

#### A. Inicio de Sesión (Login)
* **Método:** `POST`
* **Ruta:** `/usuarios/login`
* **Uso:** Autenticación desde `LoginScreen`. Acepta `email`/`correo` y `password`/`clave`.
* **Request Body:**
  ```json
  {
    "email": "carlos.abarzua@example.com",
    "password": "Nose123"
  }
  ```
* **Response (Éxito - 200 OK):**
  ```json
  {
    "status": "ok",
    "token": "eyJhbGciOiJIUzI1NiIsIn...",
    "id": 1,
    "nombre": "Carlos Abarzua",
    "email": "carlos.abarzua@example.com",
    "rol": "DONANTE"
  }
  ```

#### B. Registro de Usuario
* **Método:** `POST`
* **Ruta:** `/usuarios/register`
* **Uso:** Registro desde `RegistroContent`.
* **Request Body:**
  ```json
  {
    "nombre": "Bayron Garcia",
    "email": "bayron@example.com",
    "password": "miPassword123"
  }
  ```
* **Response (200 OK):**
  ```json
  {
    "status": "ok",
    "message": "Usuario creado exitosamente!"
  }
  ```

#### C. Obtener Perfil por ID
* **Método:** `GET`
* **Ruta:** `/usuarios/{id}`  *(Ej: `/usuarios/1`)*
* **Response (200 OK):**
  ```json
  {
    "id": 1,
    "nombre": "Carlos Abarzua",
    "email": "carlos.abarzua@example.com",
    "rol": "DONANTE",
    "foto_perfil": null,
    "activo": true,
    "creado_en": "2026-11-18T22:00:00"
  }
  ```

---

### 2. Microservicio: `Donaciones-service`
* **Puerto:** `8082`
* **URL Base:** `http://localhost:8082/donaciones` *(Desde emulador Android: `http://10.0.2.2:8082/donaciones`)*

#### A. Registrar Donación (Aporte)
* **Método:** `POST`
* **Ruta:** `/donaciones`
* **Uso:** Formulario de donación (`DonacionScreen`).
* **Request Body:**
  ```json
  {
    "usuario_id": 1,
    "nombre_donante": "Carlos Abarzua",
    "correo_donante": "carlos.abarzua@example.com",
    "monto": 15000,
    "fecha_aporte": "18/11/2026",
    "acepta_condiciones": true,
    "recibir_seguimiento": true,
    "foto_comprobante": "comprobante_123.jpg",
    "region": "Metropolitana",
    "comuna": "Santiago"
  }
  ```
* **Response (201 Created):**
  ```json
  {
    "status": "ok",
    "message": "Donación registrada con éxito",
    "donacion": {
      "id": 1,
      "usuario_id": 1,
      "monto": 15000,
      "estado": "PENDIENTE",
      "fecha_registro": 1731980000000,
      "creado_en": "2026-11-18T23:25:00"
    }
  }
  ```

#### B. Listar Donaciones por Usuario
* **Método:** `GET`
* **Ruta:** `/donaciones/usuario/{usuarioId}`  *(Ej: `/donaciones/usuario/1`)*
* **Uso:** Pantalla "Mis Aportes" / Historial (`HistorialAportesScreen`).
* **Response (200 OK):** Lista de donaciones pertenecientes a ese usuario ordenadas de la más reciente a la más antigua.

#### C. Listar Todas las Donaciones (Admin)
* **Método:** `GET`
* **Ruta:** `/donaciones`
* **Uso:** Panel de Administrador para auditar y revisar todos los aportes de todos los usuarios.

#### D. Cambiar Estado de Donación (Aprobar / Rechazar)
* **Método:** `PUT`
* **Ruta:** `/donaciones/{id}/estado`  *(Ej: `/donaciones/1/estado`)*
* **Uso:** Acción del Administrador en el panel.
* **Request Body:**
  ```json
  {
    "estado": "APROBADA" 
  }
  ```
* **Response (200 OK):**
  ```json
  {
    "status": "ok",
    "message": "Estado actualizado exitosamente",
    "donacion": {
      "id": 1,
      "estado": "APROBADA"
    }
  }
  ```

---

### 3. Microservicio: `Contacto-service`
* **Puerto:** `8083`
* **URL Base:** `http://localhost:8083/contacto` *(Desde emulador Android: `http://10.0.2.2:8083/contacto`)*

#### A. Enviar Solicitud de Contacto
* **Método:** `POST`
* **Ruta:** `/contacto`
* **Uso:** Formulario de contacto (`ContactoScreen`). `usuario_id` puede ser un número o `null` si es anónimo.
* **Request Body:**
  ```json
  {
    "usuario_id": 1,
    "nombre": "Carlos Abarzua",
    "correo": "carlos.abarzua@example.com",
    "tipo_colaboracion": "Voluntariado",
    "mensaje": "Hola, me gustaría apoyar en las actividades de estimulación visual."
  }
  ```
* **Response (201 Created):**
  ```json
  {
    "status": "ok",
    "message": "Solicitud de contacto enviada con éxito",
    "contacto": {
      "id": 1,
      "estado": "RECIBIDA",
      "fecha_registro": 1731980000000,
      "creado_en": "2026-11-18T23:25:00"
    }
  }
  ```

#### B. Listar Solicitudes por Usuario
* **Método:** `GET`
* **Ruta:** `/contacto/usuario/{usuarioId}`  *(Ej: `/contacto/usuario/1`)*
* **Uso:** Pantalla "Mis solicitudes" (`MisSolicitudesScreen`).
* **Response (200 OK):** Lista de solicitudes del usuario con su estado y la respuesta del admin si ya fue respondida.

#### C. Listar Todas las Solicitudes (Admin)
* **Método:** `GET`
* **Ruta:** `/contacto`
* **Uso:** Panel de Administrador para ver todos los mensajes y colaboraciones recibidas.

#### D. Responder Solicitud (Admin)
* **Método:** `PUT`
* **Ruta:** `/contacto/{id}/responder`  *(Ej: `/contacto/1/responder`)*
* **Uso:** El administrador escribe una respuesta a la consulta.
* **Request Body:**
  ```json
  {
    "respuesta_admin": "¡Hola Carlos! Te hemos enviado las fechas de inducción a tu correo.",
    "estado": "RESPONDIDA"
  }
  ```
* **Response (200 OK):**
  ```json
  {
    "status": "ok",
    "message": "Solicitud respondida exitosamente",
    "contacto": {
      "id": 1,
      "estado": "RESPONDIDA",
      "respuesta_admin": "¡Hola Carlos! Te hemos enviado las fechas de inducción a tu correo."
    }
  }
  ```

---

### Resumen para Retrofit en Android

Cuando configures tus interfaces en Kotlin, puedes crear un servicio por microservicio o si luego levantas el Gateway, apuntar a cada subruta:

```kotlin
interface UsuarioApi {
    @POST("usuarios/login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>

    @POST("usuarios/register")
    suspend fun register(@Body body: Map<String, String>): Response<RegisterResponse>
}

interface DonacionesApi {
    @POST("donaciones")
    suspend fun registrarDonacion(@Body donacion: DonacionDto): Response<DonacionResponse>

    @GET("donaciones/usuario/{usuarioId}")
    suspend fun getMisDonaciones(@Path("usuarioId") usuarioId: Long): Response<List<DonacionDto>>
}

interface ContactoApi {
    @POST("contacto")
    suspend fun enviarContacto(@Body contacto: ContactoDto): Response<ContactoResponse>

    @GET("contacto/usuario/{usuarioId}")
    suspend fun getMisSolicitudes(@Path("usuarioId") usuarioId: Long): Response<List<ContactoDto>>
}
```