# Android Challenge SquadMakers


## Funcionalidades

- Login con usuario y contraseña
- Registrar usuario
- Mantener sesion de usuario
- Listar tareas 
- Crear tareas 
- Modificar tareas
- Eliminar tareas
- Compartir tareas

## Tecnologías usuadas

- Kotlin
- Android compose 
- Navigation compose
- MVVM
- Coroutines
- Flow
- Hilt
- Firebase Authentication
- Firestore
- Firebase Storage
- Firebase Dynamics Deeplink

## Instalación


- Clonar el proyecto desde GitHub
- Ejecutar test con "gradle test"
- Compilar el proyecto
- Instalar y/o generar apk

Si se desea cambiar el archivo de configuración firebase con un propio

- Crear un proyecto en Firebase
- Reemplazar el google-services.json
- Habilitar Authenticacion con usuario y contraseña
- Crear una base de datos firestore
- Habilitar dynamic deeplinks
- Crear configuración de deeplink en firebase console con los siguientes atributos:
    deep link: http://androidchallenge/task
    android package: com.example.androidchallenge
  

## Funcionalides pendientes

- Modo offline
- Aumentar coverage de pruebas unitarias
