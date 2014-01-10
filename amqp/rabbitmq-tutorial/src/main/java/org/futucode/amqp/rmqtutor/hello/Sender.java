
package org.futucode.amqp.rmqtutor.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * RabbitMQ Sender.
 */
public class Sender {
    
    private final static String RMQ_HOST = "miami";
    
    private final static String QUEUE_NAME = "hello";
    
    public static void main(String[] args) throws IOException {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost( RMQ_HOST );
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        final String message = buildMessage(args);
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        
        channel.close();
        connection.close();
    }
    
    private static String buildMessage(String[] strings) {
        
        String message = "Hello, Rabbit!";
        if (strings.length >= 1) {
            message = StringUtils.join(strings, " ");
        }
        
        return message;
    }
    
}
