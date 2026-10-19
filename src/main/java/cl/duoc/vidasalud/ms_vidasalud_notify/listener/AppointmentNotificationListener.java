package cl.duoc.vidasalud.ms_vidasalud_notify.listener;

import cl.duoc.vidasalud.ms_vidasalud_notify.event.AppointmentEvent;
import cl.duoc.vidasalud.ms_vidasalud_notify.service.NotificationService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AppointmentNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(AppointmentNotificationListener.class);

    private final NotificationService notificationService;

    public AppointmentNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notify}")
    public void onAppointmentEvent(AppointmentEvent event,
                                   Channel channel,
                                   Message message,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            notificationService.sendNotification(event);
            channel.basicAck(tag, false);
            log.info("ACK tag={} evento={}", tag, event.getId());

        } catch (IllegalArgumentException e) {
            // Error no recuperable: reintentar no sirve, va directo a la DLQ
            log.error("Mensaje invalido, enviado a DLQ. tag={} causa={}", tag, e.getMessage());
            channel.basicNack(tag, false, false);

        } catch (Exception e) {
            // Error recuperable: un reintento; si ya fue reentregado, va a la DLQ
            boolean yaReintentado = message.getMessageProperties().isRedelivered();
            if (yaReintentado) {
                log.error("Fallo tras reintento, enviado a DLQ. tag={} causa={}", tag, e.getMessage());
                channel.basicNack(tag, false, false);
            } else {
                log.warn("Fallo temporal, reintentando. tag={} causa={}", tag, e.getMessage());
                channel.basicNack(tag, false, true);
            }
        }
    }
}