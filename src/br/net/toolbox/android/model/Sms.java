package br.net.toolbox.android.model;

import java.io.Serializable;

public class Sms implements Serializable{
	
	private static final long serialVersionUID = 5321348310268036504L;
	
	private long id;	
	private String numero;
	private String data;
	private String mensagem;
	private String status;
	private String criadoEm;
	
	public final static String SMS_RECEBIDO 		= "0";
	public final static String SMS_ENVIADO_PENDENTE = "1";
	public final static String SMS_ENVIADO_SUCESSO 	= "2";
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCriadoEm() {
		return criadoEm;
	}
	public void setCriadoEm(String criadoEm) {
		this.criadoEm = criadoEm;
	}
}
