
package org.futucode.amqp.rmqtutor.route;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Log Direct Emitter.
 */
public class LogDirectEmitter {
    
    private final static String RMQ_HOST = "miami";
    
    private static final String EXCHANGE_NAME = "direct_logs";
    
    public static void main(String[] args) throws IOException {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RMQ_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        
        String message = buildMessage(args);
        String severity = getSeverity(args);
        
        channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
        System.out.println(" [x] Sent [" + severity + "]: '" + message + "'");
        
        channel.close();
        connection.close();
    }
    
    private static String getSeverity(String[] args) {
        
        String severity = "error";
        if (args.length >= 1) {
            severity = args[0];
        }
        
        return severity;
    }
    
    private static String buildMessage(String[] args) {
        
        String message = "Hello, Rabbit!";
        if (args.length >= 2) {
            message = StringUtils.join(args, " ", 1, args.length);
        }
        
        return message;
    }
    
}
