
package org.futucode.amqp.rmqtutor.log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Log Fanout Emitter.
 */
public class LogFanoutEmitter {
    
    private final static String RMQ_HOST = "miami";
    
    private static final String EXCHANGE_NAME = "logs";
    
    public static void main(String[] args) throws IOException {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RMQ_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        
        String message = buildMessage(args);
        
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
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
