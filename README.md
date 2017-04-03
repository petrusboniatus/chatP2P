# Responsabilidades del servidor

### Requisitos informacionales

1. __Base de datos__ Guardar Usuarios:
 - Lista de amigos
 - Usuario, contraseña
 - Peticiones amistad pendientes

2. Gestionar usuarios conectados:
 - Guardar ips actuales

### Funcionalidad

1. Búsqueda de usuarios
2. Registro de usuarios
3. Login de usuarios
4. Enviar peticiones de amistad
5. Enviar cancelación de amistad
6. Obtener ipAmigo()


 # Responsabilidades del cliente

### Funcionalidad

#### Contra el servidor
1. Registro de usuarios
2. Login
3. Notificar ip
4. Mostrar amigos conectados
5. Petición de amistad
6. Cancelación de amistad
7. Petición de chat
8. Buscar usuarios

#### Contra otros clientes
1. Enviar documentos
2. Enviar mensajes
3. Notificar desconexión

# Secuencia de uso

1. El usuario se registra, registerUser(nombre, contraseña)
2. El usuario hace login y recibe un token Token login(nombre, contrasenha)
3. El usuario busca amigos, List<String> getConnectedFriends(token)

