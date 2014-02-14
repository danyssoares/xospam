package br.net.toolbox.android.xospam;

import br.net.toolbox.android.db.MinhaBaseDeDados;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConfigDialog extends Dialog implements View.OnClickListener {
	
	private EditText txtNumSms; 
	Button btnSalvar;
    Button btnCancelar;

	public ConfigDialog(Context contexto){
		super(contexto);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.config);

        this.setTitle(R.string.config);
        
        Functions functions = new Functions();
		String operadora = functions.obterNomeOperadora(this.getContext());
		String numero = functions.obterNumeroOperadora(this.getContext());
        
		TextView lblNomeOperadora = (TextView) this.findViewById(R.id.lblNomeOperadora);
        txtNumSms = (EditText) this.findViewById(R.id.txtNumSms);
        
        lblNomeOperadora.setText(String.format("Sua operadora foi detectada como a %s. Se estiver correto, salve a configuração", operadora));
        txtNumSms.setText(numero);
        
        if(numero==null||numero.length()==0){
			numero = new MinhaBaseDeDados().obterNumero(operadora);
			if(numero.equals(MinhaBaseDeDados.SERVICO_INEXISTENTE)){
				
				lblNomeOperadora.setText(String.format("Sua operadora foi detectada como a %s, mas ainda não há serviço de sms para ela. Caso você saiba, preencha o número abaixo, caso contrário o contato será feito via web.", operadora));
				txtNumSms.setText(null);
					
			} else{
				txtNumSms.setText(numero);
			}
		}
        
        btnSalvar = (Button) this.findViewById(R.id.btnSalvar);
        btnCancelar = (Button) this.findViewById(R.id.btnCancelar);
        
        btnSalvar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);        
      
	}
	
	@Override
	public void onClick(View v) {
		
		if(v.equals(btnSalvar)){
			Functions functions = new Functions();
			
			String numeroSms = txtNumSms.getText().toString(); 
			if(numeroSms.length()==0){
				numeroSms = "";
			}
			functions.gravarNumeroOperadora(v.getContext(), numeroSms);
			functions.gravarPrimeiraExecucao(v.getContext());
		}
		
		ConfigDialog.this.dismiss();
	}
}
