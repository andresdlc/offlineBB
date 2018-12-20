package bolivariano.esquema.offline;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
//import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

import com.bb.cliente.ClientePostillion;
import com.bb.cliente.IClientePostillion;
import com.bb.cliente.RespuestaConsulta;
import com.bb.enums.TipoCuenta;

public class ActualizarProcessor implements Processor {

	private static final Logger logger = Logger.getLogger(ActualizarProcessor.class);

	public void process(Exchange exchange) throws Exception {

		CamelContext context = exchange.getContext();

		String respuestaServicioCore = exchange.getIn().getBody(String.class);

		// String envelope = context.resolvePropertyPlaceholders("{{common.envelope}}");

		Cuenta cuenta = Utils.obtenerDatosCuenta(respuestaServicioCore);
		TipoCuenta tipoCuenta = tipoCuenta(cuenta.getTipo());

		String ip = context.resolvePropertyPlaceholders("{{postillion.ip}}");
		int puerto = Integer.parseInt(context.resolvePropertyPlaceholders("{{postillion.puerto}}"));

		IClientePostillion cliente =  ClientePostillion.getClientePostillion(ip, puerto);
		RespuestaConsulta respuestaPostillion = cliente.debito(tipoCuenta, cuenta.getCuenta(),
				cuenta.getSaldo(), cuenta.getComision());

		respuestaPostillion.getRespuesta();
		if (respuestaPostillion.isExitoOperacion()) {
			exchange.getIn().setHeader("ErrorActualizarSaldo", true);
		} else {
			exchange.getIn().setHeader("ErrorActualizarSaldo", false);
		}
		
		if (cuenta.getTipoD()!=null) {
			TipoCuenta tipoCuentaD = tipoCuenta(cuenta.getTipoD());
			RespuestaConsulta respuestaPostillion1 = cliente.credito(tipoCuentaD, cuenta.getCuentaD(),
					cuenta.getSaldo(), 0.00);
			respuestaPostillion1.getRespuesta();
			
			if (respuestaPostillion1.isExitoOperacion()) {
				logger.debug("Error al realizar el credito:"+tipoCuentaD);
				//exchange.getIn().setHeader("ErrorActualizarSaldo", true);
			} //else {
				//exchange.getIn().setHeader("ErrorActualizarSaldo", false);
				//SERIA EL REVERSO DE LA ACTUALIZACION ANTERIOR POR QUE NO PUEDO REALIZAR EL CREDITO
				//logger.debug("****REVERSO DE ACTUALIZACION DE SALDO****");
			//	respuestaPostillion = cliente.credito(tipoCuenta, cuenta.getCuenta(),
			//			cuenta.getSaldo(), cuenta.getComision());
			//}
		}

		exchange.getIn().setBody(respuestaServicioCore);
	}

	private TipoCuenta tipoCuenta(String cuenta) {
		if (cuenta != null) {
			if (cuenta.equalsIgnoreCase("AHO")) {
				return TipoCuenta.AHORROS;
			} else if (cuenta.equalsIgnoreCase("CTE")) {
				return TipoCuenta.CORRIENTE;
			} else if (cuenta.equalsIgnoreCase("VIR")) {
				return TipoCuenta.VIRTUAL;
			}
		}
		return null;
	}

}
