package cl.duoc.vidasalud.ms_vidasalud_notify.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final RabbitMQProperties properties;

    public RabbitMQConfig(RabbitMQProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public TopicExchange appointmentsExchange() {
        return new TopicExchange(properties.getExchange());
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(properties.getDlx());
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(properties.getDlq()).build();
    }

    @Bean
    public Queue notifyQueue() {
        return QueueBuilder.durable(properties.getQueue().getNotify())
                .withArgument("x-dead-letter-exchange", properties.getDlx())
                .withArgument("x-dead-letter-routing-key", properties.getDlq())
                .build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(properties.getDlq());
    }

    @Bean
    public Binding notifyCreatedBinding() {
        return BindingBuilder.bind(notifyQueue())
                .to(appointmentsExchange())
                .with(properties.getRoutingKey().getCreated());
    }

    @Bean
    public Binding notifyStatusChangedBinding() {
        return BindingBuilder.bind(notifyQueue())
                .to(appointmentsExchange())
                .with(properties.getRoutingKey().getStatusChanged());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}