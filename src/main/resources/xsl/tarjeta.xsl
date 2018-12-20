<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:men="http://www.bolivariano.com/CashManagement/MensajesTarjetaCash"
	exclude-result-prefixes="xsl">
	<xsl:output omit-xml-declaration="yes"/>
	<xsl:template match="/">
<men:MensajeSalidaPagarTarjetaCredito >
	<codigoError>0</codigoError>
	<codigoErrorRemoto/>
	<mensajeSistema>Transaccion existosa OFFLINE</mensajeSistema>
	<mensajeUsuario>Transaccion existosa</mensajeUsuario>
	<fechaProceso><xsl:value-of select='substring(/*/*/*/fecha,0,11)'/></fechaProceso>
	<fechaTransaccion><xsl:value-of select='/*/*/*/fecha'/></fechaTransaccion>
	<secuenciaTransaccion><xsl:value-of select='/*/*/*/secuencial'/></secuenciaTransaccion>
	<offline>S</offline>
</men:MensajeSalidaPagarTarjetaCredito>
</xsl:template>
	
</xsl:stylesheet>