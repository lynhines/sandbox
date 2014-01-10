
package org.futucode.amqp.rmqtutor.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;

/**
 *
 * RabbitMQ Receiver.
 */
public class Receiver {
    
    private final static String RMQ_HOST = "miami";
    
    private final static String QUEUE_NAME = "hello";
    
    public static void main(String[] args)
            throws IOException, InterruptedException {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost( RMQ_HOST );
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        
        QueueingConsumer consumer = new QueueingConsumer(channel);
        boolean autoAcknowledge = false;
        channel.basicConsume(QUEUE_NAME, autoAcknowledge, consumer);
        
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            
            System.out.println(" [x] Received '" + message + "'");
            doWork(message);
            // Send acknowledge back to the broker
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            System.out.println(" [x] Done");
        }
        
    }
    
    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
    
}
