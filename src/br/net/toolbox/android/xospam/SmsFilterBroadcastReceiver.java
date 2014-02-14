package br.net.toolbox.android.xospam;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import br.net.toolbox.android.model.Sms;

public class SmsFilterBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getExtras()!=null){
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			for (int i = 0; i<pdus.length; i++) {
				SmsMessage SMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
		          String sender = SMessage.getOriginatingAddress();
		          String body = SMessage.getMessageBody().toString();
		          Sms sms = new Sms();
		          sms.setData(String.valueOf(SMessage.getTimestampMillis()));
		          sms.setMensagem(body);
		          sms.setNumero(sender);
		          // testar se está nos contatos
		          Functions funcoes = new Functions();
		          if (!funcoes.retrieveContactRecord(sender, context)){
		        	  sms.setStatus(Sms.SMS_RECEBIDO);
		        	  funcoes.criarSms(sms, context);
		        	  
		        	  sms.setData(sdf.format(new Date(Long.valueOf(sms.getData()))));
		        	  notificar(context, sms);
		        	  
		          }
		          
			}
		}
	}

	private void notificar(Context context, Sms sms){
		
		Intent intent = new Intent(context, SmsDetailActivity.class);
		intent.putExtra("SMS", sms);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        
        
        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(context)
                .setContentTitle("Spam?" )
                .setContentText(sms.getNumero())
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent)                
                .setAutoCancel(true).getNotification();

        n.setLatestEventInfo(context, "Spam?", sms.getNumero(), pIntent);
        NotificationManager notificationManager = 
          (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, n);  
	}
}
