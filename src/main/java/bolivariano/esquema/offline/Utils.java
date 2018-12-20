package bolivariano.esquema.offline;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class Utils {
	
	/**
	 * Reemplaza los saldos reales por los indicados por el offline postillion a la salida del ws core de obtenerDetalleCuenta
	 * @param xmlOriginal La respuesta original del servicio core ConsultasCuentas.obtenerDetaleCuenta
	 * @param saldoDisponible El saldo disponible indicado por postillion
	 * @param saldoContable El saldo contable indicado por postillion
	 * @return El xml de respuesta modificado a enviar al cliente por offline con los nuevos saldos de postillion
	 * @throws Exception
	 */
	public static String reemplazarSaldos(String xmlOriginal,double saldoDisponible,double saldoContable)throws Exception{
		String xmlFinal = "";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    Document documento = builder.parse(new InputSource(new StringReader(xmlOriginal)));
	    
		documento.getElementsByTagName("disponible")
			.item(0)
			.setTextContent(String.format("%.2f",saldoDisponible));
		
		documento.getElementsByTagName("saldoGirar")
			.item(0)
			.setTextContent(String.format("%.2f",saldoDisponible));
		
		documento.getElementsByTagName("saldoCont")
			.item(0)
			.setTextContent(String.format("%.2f",saldoContable));
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(documento), new StreamResult(writer));
		xmlFinal = writer.getBuffer().toString().replaceAll("\n|\r", "");
		
		return xmlFinal;
	}
	
	/**
	 * Obtiene la cuenta y tipo de cuenta de la respuesta del servicio core ConsultasCuentas.obtenerDetalleCuenta
	 * @param xmlOriginal La respuesta original del servicio core ConsultasCuentas.obtenerDetaleCuenta
	 * @return Los datos de la cuenta a consultar en postillion
	 * @throws Exception
	 */
	public static Cuenta obtenerDatosCuenta(String xmlOriginal)throws Exception{
		Cuenta cuenta = new Cuenta();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    Document documento = builder.parse(new InputSource(new StringReader(xmlOriginal)));
		
		XPathFactory xquery = XPathFactory.newInstance();
		XPath xpath = xquery.newXPath();
		//MensajeEntradaPagarTarjetaCredito
		
		Node nodoTransaccion = (Node)xpath.compile("/*/*/*").evaluate(documento,XPathConstants.NODE);
		cuenta.setTransaccion(nodoTransaccion.getNodeName());
		
		
		if(cuenta.getTransaccion().contains("MensajeEntradaPagarTarjetaCredito"))
		{
			Node nodoCuenta = (Node)xpath.compile("//cuentaDebito").evaluate(documento,XPathConstants.NODE);
			Node nodoTipo = (Node)xpath.compile("//tipoCuentaDebito").evaluate(documento,XPathConstants.NODE);
			Node nodoSaldo = (Node)xpath.compile("//montoPago").evaluate(documento,XPathConstants.NODE);
			Node nodoComision = (Node)xpath.compile("//comision").evaluate(documento,XPathConstants.NODE);
			Node nodoTipoPago = (Node)xpath.compile("//tipoPago").evaluate(documento,XPathConstants.NODE);
			
		
			cuenta.setTipo(nodoTipo.getTextContent());
			cuenta.setCuenta(nodoCuenta.getTextContent());
			cuenta.setComision(nodoComision.getTextContent());
			cuenta.setSaldo(nodoSaldo.getTextContent());
			cuenta.setTipoTrx(nodoTipoPago.getTextContent());
		}else if(cuenta.getTransaccion().contains("mensajeEntradaEjecutarTransferencia")) {
			Node nodoCuentaO = (Node)xpath.compile("//cuentaOrigen/cuenta").evaluate(documento,XPathConstants.NODE);
			Node nodoTipoO = (Node)xpath.compile("//cuentaOrigen/tipo").evaluate(documento,XPathConstants.NODE);
			Node nodoCuentaD = (Node)xpath.compile("//cuentaDestino/cuenta").evaluate(documento,XPathConstants.NODE);
			Node nodoTipoD = (Node)xpath.compile("//cuentaDestino/tipo").evaluate(documento,XPathConstants.NODE);
			
			Node nodoSaldo = (Node)xpath.compile("//montoOrigen").evaluate(documento,XPathConstants.NODE);
			Node nodoComision = (Node)xpath.compile("//comision").evaluate(documento,XPathConstants.NODE);
			Node nodoTipoTrx =(Node)xpath.compile("//tipo").evaluate(documento,XPathConstants.NODE);
		
			cuenta.setTipo(nodoTipoO.getTextContent());
			cuenta.setCuenta(nodoCuentaO.getTextContent());
			cuenta.setTipoD(nodoTipoD.getTextContent());
			cuenta.setCuentaD(nodoCuentaD.getTextContent());
			cuenta.setComision(nodoComision.getTextContent());
			cuenta.setSaldo(nodoSaldo.getTextContent());
			cuenta.setTipoTrx(nodoTipoTrx.getTextContent());
	    }else
		{
		
		Node nodoCuenta = (Node)xpath.compile("//identificadorCuenta/cuenta").evaluate(documento,XPathConstants.NODE);
		Node nodoTipo = (Node)xpath.compile("//identificadorCuenta/tipo").evaluate(documento,XPathConstants.NODE);
		
		cuenta.setTipo(nodoTipo.getTextContent());
		cuenta.setCuenta(nodoCuenta.getTextContent());
		}

		return cuenta;
	}
}
