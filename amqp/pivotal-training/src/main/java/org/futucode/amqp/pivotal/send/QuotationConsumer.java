
package org.futucode.amqp.pivotal.send;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import org.futucode.amqp.pivotal.broker.RabbitMqBroker;

/**
 *
 * Quotation Consumer.
 */
public class QuotationConsumer {

    public static void main(String[] args) throws Exception {
        
        ConnectionFactory factory = new ConnectionFactory();
        
        factory.setUsername(RabbitMqBroker.GUEST_USER);
        factory.setPassword(RabbitMqBroker.GUEST_PASS);
        factory.setVirtualHost(RabbitMqBroker.DEFAULT_VHOST);
        factory.setHost(RabbitMqBroker.MIAMI_HOST);
        factory.setPort(RabbitMqBroker.MIAMI_PORT);
        
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.basicConsume("quotations", true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body) throws IOException {
                
                System.out.println("Received quotation: " + new String(body));
            }
        });
    }

}
