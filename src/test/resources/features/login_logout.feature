# language: es
# - Prioridad Alta | 
@alta @autenticacion
Característica: Login y logout
  Como usuario del sitio
  Quiero iniciar y cerrar sesión
  Para acceder y salir de mi cuenta de forma segura

  @smoke @login
  Escenario: Login con credenciales válidas
    Dado que tengo una cuenta registrada
    Cuando inicio sesión con mi email y password correctos
    Entonces veo "Logged in as {name}" en el header

  @login
  Escenario: Login con credenciales inválidas
    Dado que estoy en la página de Signup / Login
    Cuando inicio sesión con un password incorrecto
    Entonces veo el mensaje "Your email or password is incorrect!"
    Y no se inicia la sesión

  @logout
  Escenario: Logout de un usuario autenticado
    Dado que tengo una sesión iniciada
    Cuando selecciono la opción de cerrar sesión
    Entonces vuelvo a la página de Signup / Login
    Y la sesión queda cerrada
