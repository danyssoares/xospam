package br.net.toolbox.android.model;
/** 
 * Classe que representa a tabela que armazena os n�meros
 * que o usu�rio n�o quer que sejam considerados como spam
 * 
 * @author Y8DE
 *
 */
public class NumeroDesconsiderado {

	private int id;
	private String numero;
	private String criadoEm;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getCriadoEm() {
		return criadoEm;
	}
	public void setCriadoEm(String criadoEm) {
		this.criadoEm = criadoEm;
	}	
}
