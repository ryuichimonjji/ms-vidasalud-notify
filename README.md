# ms-vidasalud-notify

Microservicio consumidor de RabbitMQ del sistema VidaSalud. Recibe los eventos de atenciones publicados por `ms-vidasalud-appointments` y genera las notificaciones.

## Tecnologías
- Java 17, Spring Boot 4.1.1
- Spring AMQP (RabbitMQ)

## Flujo de mensajería
- Exchange: `vidasalud.appointments.exchange` (topic)
- Routing keys: `appointment.created`, `appointment.status.changed`
- Cola: `vidasalud.notify.queue`
- Dead-letter: exchange `vidasalud.appointments.dlx`, cola `vidasalud.notify.dlq`

Todos los nombres están centralizados en `application.yml` y se leen con la clase `RabbitMQProperties`.

## Estructura
- `config/`: propiedades y beans de Queue, Exchange y Binding
- `listener/`: consumidores (`AppointmentNotificationListener`, `DeadLetterListener`)
- `service/`: lógica de negocio (`NotificationService`), sin código de RabbitMQ
- `event/` y `model/`: evento y enum de estados

## Manejo de errores (ACK manual)
- Éxito: `basicAck`.
- Mensaje inválido (error no recuperable): `basicNack` sin reencolar, va a la DLQ.
- Error temporal: un reintento; si falla de nuevo, va a la DLQ.
- Los mensajes de la DLQ se registran en el log con su header `x-death`.

## Ejecución
Requiere RabbitMQ en `localhost:5672` (ver `docker/docker-compose.yml` en `ms-vidasalud-rabbitmq-admin`).

    ./mvnw spring-boot:run

Puerto: 8082
