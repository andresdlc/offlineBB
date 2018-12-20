package bolivariano.esquema.offline;

public class Cuenta {
	private String tipo;
	private String cuenta;
	private String TipoD;
	private String cuentaD;
	private String transaccion;
	private String Saldo;
	private String Comision;
	private String tipoTrx;
	public Cuenta() {
		  
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public double getSaldo() {
		return Double.parseDouble(Saldo);
	}

	public void setSaldo(String saldo) {
		Saldo = saldo;
	}

	public double getComision() {
		return Double.parseDouble(Comision);
	}

	public void setComision(String comision) {
		Comision = comision;
	}

	public String getTipoD() {
		return TipoD;
	}

	public void setTipoD(String tipoD) {
		TipoD = tipoD;
	}

	public String getCuentaD() {
		return cuentaD;
	}

	public void setCuentaD(String cuentaD) {
		this.cuentaD = cuentaD;
	}

	public String getTipoTrx() {
		return tipoTrx;
	}

	public void setTipoTrx(String tipoTrx) {
		this.tipoTrx = tipoTrx;
	}
	
}
