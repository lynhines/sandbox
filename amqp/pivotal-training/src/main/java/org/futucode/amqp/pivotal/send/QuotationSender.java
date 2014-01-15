
package org.futucode.amqp.pivotal.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.futucode.amqp.pivotal.quote.QuotationService;
import org.futucode.amqp.pivotal.broker.RabbitMqBroker;

/**
 *
 * Quotation Sender.
 */
public class QuotationSender {
    
    private static final long PAUSE = 1000L;
    
    private static final QuotationService quotationService =
            new QuotationService();
    
    public static void main(String[] args) throws Exception {
        
        ConnectionFactory factory = new ConnectionFactory();
        
        factory.setUsername(RabbitMqBroker.GUEST_USER);
        factory.setPassword(RabbitMqBroker.GUEST_PASS);
        factory.setVirtualHost(RabbitMqBroker.DEFAULT_VHOST);
        factory.setHost(RabbitMqBroker.MIAMI_HOST);
        factory.setPort(RabbitMqBroker.MIAMI_PORT);
        
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        while (true) {
            letsWait();
            String quotation = quotationService.next();
            channel.basicPublish("quotations", "nasq", null, quotation.getBytes());
            System.out.println(" [x] Sent quotation '" + quotation + "'");
        }
    }

    private static void letsWait() throws Exception {
        Thread.sleep(PAUSE);
    }
    
}
