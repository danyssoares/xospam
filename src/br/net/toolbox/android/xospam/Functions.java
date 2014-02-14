package br.net.toolbox.android.xospam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import br.net.toolbox.android.model.Sms;
import br.net.toolbox.android.xospam.helper.DatabaseHelper;

public class Functions {
	
	public final static int totalPagina = 10;
	public final static String NUMERO_OPERADORA_REGISTRO = "numeroOperadoraRegistro";
	public final static String NAO_CONFIGURADO = "naoConfigurado";
	
	public List<Sms> obterListaSms(){
		return null;
	}
	
	public boolean retrieveContactRecord(String phoneNo, Context context) {
		boolean resultado = false;
	    try{
	        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
	        String[] projection = new String[] { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME };
	        String selection = null;
	        String[] selectionArgs = null;
	        String sortOrder = ContactsContract.PhoneLookup.DISPLAY_NAME+ " COLLATE LOCALIZED ASC";
	        ContentResolver cr = context.getContentResolver();
	        if(cr != null){
	            Cursor resultCur = cr.query(uri, projection, selection, selectionArgs, sortOrder);
	            if(resultCur != null){
	                if (resultCur.getCount()>0) {	                    
	                    resultado = true;	                    
	                }
	                resultCur.close();
	            }
	        }
	    }
	    catch(Exception sfg){
	        Log.e("Error", "Error in loadContactRecord : "+sfg.toString());
	    }
	    return resultado;
	}//fn retrieveContactRecord 
	
	
    public List<Sms> getListaSms(Context context, int inicio){
    	List<Sms> smsList = new ArrayList<Sms>();
    	

        Uri uri = Uri.parse("content://sms/inbox");
        //String[] projection = { Sms._ID, People.NAME, People.NUMBER };
        Cursor c= context.getContentResolver().query(uri, null, null ,null,null);        
        
        int countItensValids = 0;
        
        Functions funcoes = new Functions();
        
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
            	
            	
            	String address = c.getString(c.getColumnIndexOrThrow("address")).toString();
            	
                if (!funcoes.retrieveContactRecord(address, context)){
                	
                	countItensValids ++;
                	
                	if (countItensValids >= inicio){
                		Sms sms = new Sms();
	                      sms.setMensagem(abreviarMensagem(c.getString(c.getColumnIndexOrThrow("body")).toString()));
	                      sms.setNumero(address);
	                      sms.setData(c.getString(c.getColumnIndexOrThrow("date")).toString());
	                      smsList.add(sms);
                	}
                	
                }
               
                if(smsList.size()==totalPagina){
                	break;
                }
            
                c.moveToNext();
                
            }
        }
        c.close();
        
        return smsList;
    }
    
    public void carregarBancoDeDados(Context context){
    	reiniciarBancoDeDados(context);
    	
    	Uri uri = Uri.parse("content://sms/inbox");
        //String[] projection = { Sms._ID, People.NAME, People.NUMBER };
        Cursor c= context.getContentResolver().query(uri, null, null ,null,null);        
        
        Functions funcoes = new Functions();
        
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
            	
            	
            	String address = c.getString(c.getColumnIndexOrThrow("address")).toString();
            	
                if (!funcoes.retrieveContactRecord(address, context)){
                	
                	Sms sms = new Sms();
	                sms.setMensagem(c.getString(c.getColumnIndexOrThrow("body")).toString());
	                sms.setNumero(address);
	                sms.setData(c.getString(c.getColumnIndexOrThrow("date")).toString());
	                sms.setStatus(Sms.SMS_RECEBIDO);
	                // insere no banco                	
	                criarSms(sms, context);
                }
               
                c.moveToNext();
                
            }
        }
        c.close();
    }
    
    private String abreviarMensagem(String mensagem){
    	String retorno = mensagem;
    	
    	if (mensagem.length()>25){
    		retorno = mensagem.substring(0, 24)+"...";
    	}
    	
    	return retorno;
    }
	
    public String obterNomeOperadora(Context context){
    	TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    	return manager.getNetworkOperatorName();
    }
    
	
    public String obterNumeroOperadora(Context context){
    	SharedPreferences settings = context.getSharedPreferences("xospam", Activity.MODE_PRIVATE);
    	return settings.getString(NUMERO_OPERADORA_REGISTRO, null);

    }
    
    public void gravarNumeroOperadora(Context context, String numero){
    	SharedPreferences settings = context.getSharedPreferences("xospam", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString(NUMERO_OPERADORA_REGISTRO,numero);
    	editor.commit();
    }
    
    public void clearSharedPreferences(Context context){
    	SharedPreferences settings = context.getSharedPreferences("xospam", Context.MODE_PRIVATE);
    	settings.edit().clear().commit();
    }
    
    public boolean ePrimeiraExecucao(Context context){
    	SharedPreferences settings = context.getSharedPreferences("xospam", Activity.MODE_PRIVATE);
    	return !settings.getBoolean(NAO_CONFIGURADO, Boolean.FALSE);
    }
    
    public void gravarPrimeiraExecucao(Context context){
    	SharedPreferences settings = context.getSharedPreferences("xospam", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putBoolean(NAO_CONFIGURADO,Boolean.TRUE);
    	editor.commit();
    }
    
    public String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
    
    public long criarSms(Sms sms, Context context){
    	DatabaseHelper db = new DatabaseHelper(context);
    	
    	long idSms =  db.criarSms(sms);   
    	db.closeDB();
    	
    	return idSms;
    }
    
    public void reiniciarBancoDeDados(Context context){
    	DatabaseHelper databaseHelper = new DatabaseHelper(context);
    	SQLiteDatabase db = databaseHelper.getWritableDatabase();
    	
    	databaseHelper.droparBanco(db);
    	databaseHelper.criarBanco(db);
        
        databaseHelper.closeDB();
    }
    
    public List<Sms> getListaSmsEnviado(Context context){
    	DatabaseHelper db = new DatabaseHelper(context);
    	
    	List<Sms> listaSms = db.getAllSmsEnviado();
    	db.closeDB();
    	return listaSms;
    	
    }
    
    public List<Sms> getListaSmsRecebido(Context context){
    	DatabaseHelper db = new DatabaseHelper(context);
    	
    	List<Sms> listaSms = db.getAllSmsRecebido();
    	db.closeDB();
    	return listaSms;
    	
    }

    public int alterarSmsParaEnviadoPendente(Sms sms, Context context){
    	DatabaseHelper databaseHelper = new DatabaseHelper(context);
    	
    	int retorno = databaseHelper.alterarSmsParaEnviadoPendente(sms);
        
        databaseHelper.closeDB();
        
        return retorno;
    }
}
