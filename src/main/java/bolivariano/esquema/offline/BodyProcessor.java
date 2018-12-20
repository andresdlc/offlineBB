package bolivariano.esquema.offline;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;


public class BodyProcessor implements Processor{

	static final Logger logger = Logger.getLogger(BodyProcessor.class);

    public void process(Exchange exchange) throws Exception {   
    	CamelContext context = exchange.getContext();
		String payload = exchange.getIn().getBody(String.class);
		
		String envelope = context.resolvePropertyPlaceholders("{{common.envelope}}");		
		payload = envelope.replace("{Body}", payload);
		
		exchange.getIn().setBody(payload);

    }

}
 