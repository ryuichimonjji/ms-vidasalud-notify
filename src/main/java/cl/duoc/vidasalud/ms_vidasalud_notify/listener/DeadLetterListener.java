package cl.duoc.vidasalud.ms_vidasalud_notify.listener;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class DeadLetterListener {

    private static final Logger log = LoggerFactory.getLogger(DeadLetterListener.class);

    @RabbitListener(queues = "${rabbitmq.dlq}")
    public void onDeadLetter(Message message,
                             Channel channel,
                             @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        Object xDeath = message.getMessageProperties().getHeaders().get("x-death");

        log.error("DLQ - mensaje no entregado. cuerpo={} x-death={}", body, xDeath);

        channel.basicAck(tag, false);
    }
}