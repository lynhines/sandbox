
package org.futucode.amqp.pivotal.quote;

import java.util.Date;

/**
 * 
 * Quotation Service.
 */
public class QuotationService {

    private double current = 80.63;

    private final String symbol = "VMW";

    public String next() {
        return nextDetailed().toSimpleString();
    }

    public Quotation nextDetailed() {
        // VMware stocks are skyrocketting! BUY!!!
        current += Math.random() - 0.4;
        Quotation quotation = new Quotation(symbol, current, new Date());
        return quotation;
    }

}
