package br.net.toolbox.android.db;

import java.util.HashMap;

public class MinhaBaseDeDados {
	
	private HashMap<String, String> operadoras = new HashMap<String, String>();
	
	public final static String SERVICO_INEXISTENTE = "servicoInexistente";
	
	public MinhaBaseDeDados() {
		operadoras.put("CLARO", SERVICO_INEXISTENTE);
		operadoras.put("VIVO", "7726");
		operadoras.put("TIM", SERVICO_INEXISTENTE);
		operadoras.put("OI", SERVICO_INEXISTENTE);
		operadoras.put("ANDROID", SERVICO_INEXISTENTE);
	}
	
	public String obterNumero(String operadora){
		return operadoras.get(operadora.toUpperCase());
	}
}
