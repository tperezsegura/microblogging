# Microblogging API – Clean Architecture (Kotlin + Spring Boot)

Una API backend minimalista inspirada en Twitter, desarrollada con Kotlin y Spring Boot, siguiendo Clean Architecture.

---

## Requisitos para correr el proyecto

Para ejecutar la aplicación, solo es necesario tener instalado:

- [Docker](https://www.docker.com/) 20+
- [Docker Compose](https://docs.docker.com/compose/) v2+

---

## Comandos disponibles

| Comando          | Descripción                                          |
|------------------|------------------------------------------------------|
| `make up`        | Construye y levanta la aplicación                    |
| `make down`      | Elimina el contenedor y la imagen local              |

- En caso de no contar con `make`, se pueden utilizar los comandos equivalentes presentes dentro del archivo [Makefile](Makefile)

---

## Endpoints disponibles

| Método | Ruta                                   | Descripción                        |
|--------|----------------------------------------|------------------------------------|
| POST   | `/users`                               | Crear un usuario                   |
| POST   | `/tweets`                              | Publicar un tweet                  |
| POST   | `/users/{id}/follow/{targetId}`        | Seguir a otro usuario              |
| GET    | `/users/{id}/timeline`                 | Ver el timeline de un usuario      |

---

## Ejemplos de uso con `curl`

### 1. Crear un usuario
```bash
curl -i -X POST http://localhost:8080/users -H "Content-Type: application/json" -d '{"username":"Tomas"}'
```

### 2. Publicar un tweet
```bash
curl -i -X POST http://localhost:8080/tweets -H "Content-Type: application/json" -d '{"authorId":1,"content":"Hola mundo"}'
```

### 3. Seguir a otro usuario
```bash
curl -i -X POST http://localhost:8080/users/1/follow/2
```

### 4. Ver el timeline de un usuario
```bash
curl -i http://localhost:8080/users/1/timeline
```

---

## Más información 
La [Wiki del repositorio](https://github.com/tperezsegura/microblogging/wiki) incluye:

- Decisiones de diseño y arquitectura
- Detalles de infraestructura simulada (Redis, PostgreSQL)
- Enfoque de testing y validaciones

---
