
package org.futucode.amqp.pivotal.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.futucode.amqp.pivotal.broker.RabbitMqBroker;
import org.futucode.amqp.pivotal.quote.QuotationService;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * Sending And Receiving Test.
 */
public class SendingAndReceivingTest {

    private final QuotationService quotationService = new QuotationService();

    @Test
    public void sendAndReceiving() throws Exception {
        
        ConnectionFactory factory = new ConnectionFactory();
        
        factory.setUsername(RabbitMqBroker.GUEST_USER);
        factory.setPassword(RabbitMqBroker.GUEST_PASS);
        factory.setVirtualHost(RabbitMqBroker.DEFAULT_VHOST);
        factory.setHost(RabbitMqBroker.MIAMI_HOST);
        factory.setPort(RabbitMqBroker.MIAMI_PORT);
        
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        /* Uncomment the next lines if no quotations queue and exchange exist.
        
          channel.exchangeDeclare("quotations", "fanout", true);
          channel.queueDeclare("quotations", false, false, false, null);
          channel.queueBind("quotations", "quotations", "");
         */
        
        channel.queuePurge("quotations");

        String quotation = quotationService.next();
        byte[] message = quotation.getBytes();

        channel.basicPublish("quotations", "nasq", null, message);

        GetResponse response = null;
        short attempts = 5;
        while (response == null && attempts-- > 0) {
            response = channel.basicGet("quotations", true);
        }

        Assert.assertNotNull(response);

        String receivedQuotation = new String(response.getBody());
        Assert.assertEquals(quotation, receivedQuotation);

        channel.close();
        connection.close();
    }

}
