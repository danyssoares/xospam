package br.net.toolbox.android.xospam;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.net.toolbox.android.model.Sms;

public class SmsDetailActivity extends Activity{
	
	private Sms sms;
	private BroadcastReceiver smsDeliveredReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.envia);
		
		sms = (Sms)getIntent().getExtras().get("SMS");
		String smsEnviado = (String) getIntent().getExtras().get("SMS_ENVIADO");
		
		TextView txtEnviaNumero = (TextView)findViewById(R.id.txtEnviaNumero);
		TextView txtEnviaData = (TextView)findViewById(R.id.txtEnviaData);
		TextView txtEnviaMsg = (TextView)findViewById(R.id.txtEnviaMsg);
		
		Button btnEnviar = (Button)findViewById(R.id.btnEnviar);
		if(smsEnviado!=null && smsEnviado.equals("S")){
			btnEnviar.setVisibility(View.INVISIBLE);
		}
		
		txtEnviaData.setText(sms.getData());
		txtEnviaMsg.setText(sms.getMensagem());
		txtEnviaNumero.setText(sms.getNumero());
		
		
		btnEnviar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				handlBtnEnviarClick();				
			}
		});
		
		
	}
	
	private void handlBtnEnviarClick(){
		
		//Enviar Sms
		Functions funcoes = new Functions();
		//String operadora = funcoes.obterNomeOperadora(this);
		String numeroOperadora = funcoes.obterNumeroOperadora(this);
		
		if(numeroOperadora != null){
			
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(numeroOperadora, null, sms.getNumero(), null, null);
			
			funcoes.alterarSmsParaEnviadoPendente(sms, this);
			
			
		}
		
		this.finish();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		smsDeliveredReceiver = new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                switch(getResultCode()) {
                case Activity.RESULT_OK:
                	// atualizar o status da mensagem com ok
                    Toast.makeText(getBaseContext(), "SMS Delivered", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                	// atualizar o status da mensagem como cancelado
                    Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        };
        
        registerReceiver(smsDeliveredReceiver, new IntentFilter("SMS_DELIVERED"));
	}
	
	@Override
	public void onPause() {
        super.onPause();
        
        unregisterReceiver(smsDeliveredReceiver);
    }
}
