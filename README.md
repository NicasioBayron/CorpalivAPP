Nombres: Bayron Nicasio y Eileen Salas

# CORPALIV App

## Descripción del proyecto

Aplicación Android desarrollada como proyecto académico para presentar la misión y el trabajo de CORPALIV y Alba Lab, facilitar solicitudes de contacto y registrar aportes de demostración.

La aplicación está construida con Kotlin y Jetpack Compose. Utiliza almacenamiento local con Room: las cuentas, solicitudes y aportes se guardan en el dispositivo, sin un backend ni sincronización entre teléfonos.

Los aportes son simulados: no se procesan pagos ni se realizan cobros. La fotografía adjunta demuestra la integración con la cámara; no comprueba la autenticidad de una cédula ni verifica automáticamente la identidad. Para las pruebas se puede fotografiar una hoja o un documento ficticio.

## Funcionalidades

- Pantallas informativas de inicio, sobre nosotros y formas de aportar.
- Navegación mediante menú lateral.
- Registro e inicio de sesión con cuentas locales y validación de campos.
- Protección de contraseñas mediante hash PBKDF2 con sal individual.
- Perfil con nombre, correo y cambio de fotografía mediante el selector de imágenes de Android.
- Copia privada de la foto de perfil para conservarla entre ejecuciones.
- Formulario de contacto y consulta de solicitudes asociadas a la cuenta.
- Formulario de aporte personal con nombre, correo, monto entero positivo, fecha, aceptación de condiciones y preferencia de seguimiento.
- Búsqueda dinámica de comunas en un catálogo local de la Región Metropolitana de Santiago. La búsqueda ignora mayúsculas y tildes, y exige seleccionar una opción válida. No incluye todas las regiones del país.
- Captura y confirmación obligatoria de una fotografía de prueba antes de registrar un aporte, mediante la aplicación de cámara del dispositivo.
- Historial de aportes por usuario, ordenado del más reciente al más antiguo, con monto, fecha, región, comuna y referencia al documento adjunto.
- Notificación local de prueba desde Ajustes, con solicitud del permiso del sistema cuando corresponde. No hay recordatorios automáticos cada minuto.
- Tema claro y oscuro controlado desde Ajustes.
- Animación de aparición y desaparición de las sugerencias de comunas.
- Mensajes de validación, carga y resultado de las operaciones.
- Operaciones de almacenamiento mediante corrutinas y migraciones de Room para conservar datos al actualizar la estructura.

## Organización del proyecto

- `ui/screens`: pantallas y formularios.
- `ui/components`: componentes visuales reutilizables y menú.
- `ui/theme`: colores, tipografías y configuración del tema.
- `viewmodel`: estados y coordinación de acciones de las funcionalidades que utilizan ViewModel.
- `viewmodel/validation`: reglas de validación de login y aportes.
- `data`: entidades y consultas de Room, repositorios, migraciones y catálogo de comunas.
- `notifications`: creación del canal y envío de notificaciones locales.

El proyecto incorpora MVVM en funcionalidades como login, perfil, aportes e historial, separando el estado y la lógica de sus componentes visuales.

## Requisitos para ejecutar

- Android Studio compatible con las versiones de Android Gradle Plugin y Gradle declaradas en el proyecto.
- SDK de Android API 37, utilizado para compilar el módulo `app`.
- Un teléfono o emulador con Android 8.0 (API 26) o superior.
- Conexión a Internet para descargar el SDK y las dependencias durante la preparación inicial.
- Una aplicación de cámara disponible para probar la captura de documentos. Para la demostración completa se recomienda un teléfono físico.

## Pasos de ejecución

1. Clonar este repositorio o descargar y descomprimir su código fuente.
2. Abrir Android Studio y seleccionar **Open**. Elegir la carpeta raíz del proyecto, donde están `settings.gradle.kts` y `gradlew`.
3. Configurar el JDK de Gradle con el entorno incluido en Android Studio, compatible con la versión de Gradle del proyecto.
4. Esperar la sincronización de Gradle e instalar los componentes del SDK que solicite Android Studio. No es necesario instalar Gradle por separado: se incluye su wrapper.
5. Conectar un teléfono por USB, activar la depuración USB y aceptar la autorización del equipo. Como alternativa, iniciar un emulador con API 26 o superior.
6. Seleccionar el dispositivo y la configuración del módulo **app**.
7. Pulsar **Run** para compilar e instalar la aplicación.
8. Crear una cuenta de prueba desde **Iniciar sesión → Regístrate**. No hay credenciales iniciales predefinidas.
9. Iniciar sesión con esa cuenta para acceder al perfil, registrar aportes y consultar los historiales.

`local.properties` contiene la ruta del SDK de cada equipo. Android Studio debe utilizar la ruta local del equipo donde se ejecuta; no se debe copiar la configuración de otro integrante.

### Compilar desde una terminal

Desde la raíz del proyecto, con el SDK y un JDK compatible configurados:

```powershell
.\gradlew.bat assembleDebug
```

El APK de depuración se genera en `app/build/outputs/apk/debug/app-debug.apk`.

## Recorrido de demostración

1. Crear una cuenta ficticia e iniciar sesión.
2. Abrir el perfil y cambiar la foto. Volver a entrar para comprobar que se conserva.
3. Entrar en **Aportes** y abrir el formulario de donación.
4. Probar campos vacíos, un correo inválido, un monto no válido y una fecha pasada para observar las validaciones.
5. Buscar una comuna escribiendo parte de su nombre y seleccionarla en el cuadro de sugerencias.
6. Completar los campos, aceptar las condiciones y tomar y confirmar una fotografía de prueba. Sin una foto confirmada no se permite guardar.
7. Registrar el aporte y abrir **Ver mis aportes** desde el perfil.
8. Cerrar y abrir la app, iniciar sesión nuevamente y comprobar que el aporte permanece en el historial.
9. Probar con otra cuenta para comprobar que el historial muestra los aportes correspondientes a esa cuenta.
10. Enviar una solicitud desde Contacto y consultarla en **Mis solicitudes** con la cuenta correspondiente.
11. Desde Ajustes, cambiar el tema y enviar una notificación de prueba. En Android 13 o superior, conceder el permiso cuando el sistema lo solicite. Revisar la bandeja de notificaciones del teléfono.

## Consideraciones

- La selección de imágenes da acceso a la imagen elegida, sin requerir acceso general a toda la galería.
- La cámara se abre mediante una aplicación externa y comparte el destino de la fotografía usando FileProvider.
- Si las notificaciones están bloqueadas, pueden habilitarse desde los ajustes de la aplicación en Android.
- Desinstalar la aplicación o borrar sus datos elimina la información local de esta instalación. Para probar migraciones se debe actualizar la app conservando sus datos.
- La opción de seguimiento guarda una preferencia; no envía correos ni mensajes automáticamente.

## Validación final y rotación

- Registro y Contacto mantienen sus campos en ViewModels durante cambios de orientación; sus operaciones de guardado no dependen de la composición de la pantalla.
- Los campos muestran mensajes e iconos de error. Los botones y controles se bloquean mientras se procesa el formulario, y los resultados aparecen con una transición animada.
- Al registrar correctamente un aporte se intenta publicar una notificación local. Requiere que el usuario haya autorizado las notificaciones desde Ajustes; rechazar ese permiso no impide guardar el aporte.
- La fotografía es un documento de prueba adjunto; no autentica cédulas ni personas.

Para ejecutar las pruebas locales de validación: `.gradlew.bat testDebugUnitTest`.
