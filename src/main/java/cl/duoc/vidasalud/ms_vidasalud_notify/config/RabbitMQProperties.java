package cl.duoc.vidasalud.ms_vidasalud_notify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {

    private String exchange;
    private String dlx;
    private String dlq;
    private Queue queue = new Queue();
    private RoutingKey routingKey = new RoutingKey();

    public static class Queue {
        private String notify;
        public String getNotify() { return notify; }
        public void setNotify(String notify) { this.notify = notify; }
    }

    public static class RoutingKey {
        private String created;
        private String statusChanged;
        public String getCreated() { return created; }
        public void setCreated(String created) { this.created = created; }
        public String getStatusChanged() { return statusChanged; }
        public void setStatusChanged(String statusChanged) { this.statusChanged = statusChanged; }
    }

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }
    public String getDlx() { return dlx; }
    public void setDlx(String dlx) { this.dlx = dlx; }
    public String getDlq() { return dlq; }
    public void setDlq(String dlq) { this.dlq = dlq; }
    public Queue getQueue() { return queue; }
    public void setQueue(Queue queue) { this.queue = queue; }
    public RoutingKey getRoutingKey() { return routingKey; }
    public void setRoutingKey(RoutingKey routingKey) { this.routingKey = routingKey; }
}