package bolivariano.esquema.offline;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
//import org.apache.log4j.Logger;

import com.bb.cliente.ClientePostillion;
import com.bb.cliente.IClientePostillion;
import com.bb.cliente.RespuestaConsulta;
import com.bb.enums.TipoCuenta;

public class EndProcessor implements Processor{

	//private static final Logger logger = Logger.getLogger(EndProcessor.class); 
	
	public void process(Exchange exchange) throws Exception {
		
		CamelContext context = exchange.getContext();
		
		String respuestaServicioCore = exchange.getIn().getBody(String.class);
				
		//String envelope = context.resolvePropertyPlaceholders("{{common.envelope}}");
		
		Cuenta cuenta = Utils.obtenerDatosCuenta(respuestaServicioCore);
		TipoCuenta tipoCuenta = null;
		
		if(cuenta!=null) {
			if(cuenta.getTipo().equalsIgnoreCase("AHO")) {
				tipoCuenta = TipoCuenta.AHORROS;
			}else if(cuenta.getTipo().equalsIgnoreCase("CTE")) {
				tipoCuenta = TipoCuenta.CORRIENTE;
			}else if(cuenta.getTipo().equalsIgnoreCase("VIR")) {
				tipoCuenta = TipoCuenta.VIRTUAL;
			}
		}
		String ip =context.resolvePropertyPlaceholders("{{postillion.ip}}");
		int puerto = Integer.parseInt(context.resolvePropertyPlaceholders("{{postillion.puerto}}"));
		
		IClientePostillion cliente = ClientePostillion.getClientePostillion(ip,puerto);
		RespuestaConsulta respuestaPostillion = cliente.consultarSaldoOffline(tipoCuenta,cuenta.getCuenta());

		//String payload = envelope.replace("{Body}",Utils.reemplazarSaldos(respuestaServicioCore, respuestaPostillion.getSaldoDisponible(), respuestaPostillion.getSaldoDisponible()));
		String payload = Utils.reemplazarSaldos(respuestaServicioCore,respuestaPostillion.getSaldoDisponible(), respuestaPostillion.getSaldoDisponible());
		exchange.getIn().setBody(payload);
	}

}
