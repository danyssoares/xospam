package br.net.toolbox.android.xospam.helper;

import java.util.ArrayList;
import java.util.List;

import br.net.toolbox.android.model.NumeroDesconsiderado;
import br.net.toolbox.android.model.Sms;
import br.net.toolbox.android.xospam.Functions;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	// Logcat tag
    public static final String LOG = "DatabaseHelper";
 
    // Versão da Base de Dados
    private static final int DATABASE_VERSION = 1;
 
    // Nome da Base de Dados
    private static final String DATABASE_NAME = "xospamDatabase";
    
    // Nomes das Tabelas
    public static final String TABELA_SMS = "sms";
    public static final String TABELA_NUMERO_DESCONSIDERADO = "numero_desconsiderado";
    
    // Nomes das Colunas Comuns
    private static final String KEY_ID = "id";
    private static final String KEY_NUMERO 	 = "numero";
    private static final String KEY_CRIADO_EM = "criado_em";
    
    // Tabela SMS - colunas
    private static final String KEY_DATA 	 = "data";
    private static final String KEY_MENSAGEM = "mensagem";
    private static final String KEY_STATUS 	 = "status";
    
    // Statements de Criação das Tabelas
    // Tabela SMS
    public static final String CRIAR_TABELA_SMS = "CREATE TABLE "
            + TABELA_SMS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMERO + " TEXT, "
            + KEY_DATA + " TEXT, " + KEY_MENSAGEM + " TEXT, "
            + KEY_STATUS + " TEXT, " + KEY_CRIADO_EM + " DATETIME" + ")";
 
    // Tabela NUMERO_DESCONSIDERADO
    public static final String CRIAR_TABELA_NUMERO_DESCONSIDERADO = "CREATE TABLE " 
    		+ TABELA_NUMERO_DESCONSIDERADO
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMERO + " TEXT,"
            + KEY_CRIADO_EM + " DATETIME" + ")";
    
	
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {		
		this.criarBanco(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.droparBanco(db);
        
        onCreate(db);

	}
	
	public void droparBanco(SQLiteDatabase db){
		db.execSQL("DROP TABLE IF EXISTS " + TABELA_SMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_NUMERO_DESCONSIDERADO);
	}
	
	public void criarBanco(SQLiteDatabase db){
		db.execSQL(CRIAR_TABELA_SMS);
		db.execSQL(CRIAR_TABELA_NUMERO_DESCONSIDERADO);
	}
	
	public long criarSms(Sms sms){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_NUMERO, sms.getNumero());
	    values.put(KEY_DATA, sms.getData());
	    values.put(KEY_MENSAGEM, sms.getMensagem());	    
	    values.put(KEY_STATUS, sms.getStatus());
	    values.put(KEY_CRIADO_EM, new Functions().getDateTime());
	 
	    long smsId = db.insert(TABELA_SMS, null, values);
	 
	    return smsId;
	}

	public int alterarSmsParaEnviadoPendente(Sms sms) {
	   return this.alterarSms(sms, Sms.SMS_ENVIADO_PENDENTE);
	}
	
	public int alterarSmsParaEnviadoSucesso(Sms sms) {
		return this.alterarSms(sms, Sms.SMS_ENVIADO_SUCESSO);
	}
	
	private int alterarSms(Sms sms, String status) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_STATUS, status);
	 
	    return db.update(TABELA_SMS, values, KEY_NUMERO + " = ?",
	            new String[] { String.valueOf(sms.getNumero()) });
	}
	
	public Sms getSms(long smsId) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    String selectQuery = "SELECT  * FROM " + TABELA_SMS + " WHERE "
	            + KEY_ID + " = " + smsId;
	 
	    //Log.e(LOG, selectQuery);
	 
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c != null)
	        c.moveToFirst();
	 
	    Sms sms = new Sms();
	    sms.setId(c.getInt(c.getColumnIndex(KEY_ID)));
	    sms.setNumero((c.getString(c.getColumnIndex(KEY_NUMERO))));
	    sms.setData((c.getString(c.getColumnIndex(KEY_DATA))));
	    sms.setMensagem((c.getString(c.getColumnIndex(KEY_MENSAGEM))));
	    sms.setStatus((c.getString(c.getColumnIndex(KEY_STATUS))));
	    sms.setCriadoEm(c.getString(c.getColumnIndex(KEY_CRIADO_EM)));
	 
	    return sms;
	}
	
	public List<Sms> getAllSmsEnviado() {
	    List<Sms> smsEnviados = new ArrayList<Sms>();
	    String selectQuery = "SELECT  * FROM " + TABELA_SMS + " WHERE " + KEY_STATUS + " != '0' " + " ORDER BY "+KEY_DATA+" DESC ";
	 
	    //Log.e(LOG, selectQuery);
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	   if (c.moveToFirst()) {
	        do {
	            Sms smsEnviado = new Sms();
	            smsEnviado.setId(c.getInt(c.getColumnIndex(KEY_ID)));
	    	    smsEnviado.setNumero((c.getString(c.getColumnIndex(KEY_NUMERO))));
	    	    smsEnviado.setData((c.getString(c.getColumnIndex(KEY_DATA))));
	    	    smsEnviado.setMensagem((c.getString(c.getColumnIndex(KEY_MENSAGEM))));
	    	    smsEnviado.setStatus((c.getString(c.getColumnIndex(KEY_STATUS))));
	    	    smsEnviado.setCriadoEm(c.getString(c.getColumnIndex(KEY_CRIADO_EM)));
	 
	    	    smsEnviados.add(smsEnviado);
	        } while (c.moveToNext());
	    }
	 
	  // Log.e(LOG, String.valueOf(smsEnviados.size()));
	    return smsEnviados;
	}
	
	public List<Sms> getAllSmsRecebido() {
	    List<Sms> smsRecebidos = new ArrayList<Sms>();
	    String selectQuery = "SELECT  * FROM " + TABELA_SMS + " WHERE " + KEY_STATUS + " = '0' " + " ORDER BY "+KEY_DATA+" DESC ";
	 
	    //Log.e(LOG, selectQuery);
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	   if (c.moveToFirst()) {
	        do {
	            Sms smsRecebido = new Sms();
	            smsRecebido.setId(c.getInt(c.getColumnIndex(KEY_ID)));
	    	    smsRecebido.setNumero((c.getString(c.getColumnIndex(KEY_NUMERO))));
	    	    smsRecebido.setData((c.getString(c.getColumnIndex(KEY_DATA))));
	    	    smsRecebido.setMensagem((c.getString(c.getColumnIndex(KEY_MENSAGEM))));
	    	    smsRecebido.setStatus((c.getString(c.getColumnIndex(KEY_STATUS))));
	    	    smsRecebido.setCriadoEm(c.getString(c.getColumnIndex(KEY_CRIADO_EM)));
	 
	    	    smsRecebidos.add(smsRecebido);
	        } while (c.moveToNext());
	    }
	 
	   //Log.e(LOG, String.valueOf(smsRecebidos.size()));
	    return smsRecebidos;
	}
	
	public long criarNumeroDesconsiderado(NumeroDesconsiderado numeroDesconsiderado){
		SQLiteDatabase db = this.getWritableDatabase();
		long numeroDesconsideradoId = 0;
		
		if (!existeNumeroDesconsiderado(numeroDesconsiderado.getNumero())){
		
		    ContentValues values = new ContentValues();
		    values.put(KEY_NUMERO, numeroDesconsiderado.getNumero());
		    values.put(KEY_CRIADO_EM, new Functions().getDateTime());
		 
		    numeroDesconsideradoId = db.insert(TABELA_SMS, null, values);
		} 
		
		return numeroDesconsideradoId;
		
	}

	public NumeroDesconsiderado getNumeroDesconsideradoPorId(long numeroDesconsideradoId) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    String selectQuery = "SELECT  * FROM " + TABELA_NUMERO_DESCONSIDERADO + " WHERE "
	            + KEY_ID + " = " + numeroDesconsideradoId;
	 
	    //Log.e(LOG, selectQuery);
	 
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c != null)
	        c.moveToFirst();
	 
	    NumeroDesconsiderado numeroDesconsiderado = new NumeroDesconsiderado();
	    numeroDesconsiderado.setId(c.getInt(c.getColumnIndex(KEY_ID)));
	    numeroDesconsiderado.setNumero((c.getString(c.getColumnIndex(KEY_NUMERO))));
	    numeroDesconsiderado.setCriadoEm(c.getString(c.getColumnIndex(KEY_CRIADO_EM)));
	 
	    return numeroDesconsiderado;
	}
	
	public boolean existeNumeroDesconsiderado(String numero){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT  * FROM " + TABELA_NUMERO_DESCONSIDERADO + " WHERE "
	            + KEY_NUMERO + " = " + numero;
		
		//Log.e(LOG, selectQuery);
		 
	    Cursor c = db.rawQuery(selectQuery, null);
	    
	    return (c.getCount()>0);
	}
	
	public List<NumeroDesconsiderado> getAllNumeroDesconsiderado() {
	    List<NumeroDesconsiderado> numerosDesconsiderados = new ArrayList<NumeroDesconsiderado>();
	    String selectQuery = "SELECT  * FROM " + TABELA_NUMERO_DESCONSIDERADO;
	 
	    //Log.e(LOG, selectQuery);
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c.moveToFirst()) {
	        do {
	        	NumeroDesconsiderado numeroDesconsiderado = new NumeroDesconsiderado();
	        	numeroDesconsiderado.setId(c.getInt(c.getColumnIndex(KEY_ID)));
	        	numeroDesconsiderado.setNumero((c.getString(c.getColumnIndex(KEY_NUMERO))));
	    	    numeroDesconsiderado.setCriadoEm(c.getString(c.getColumnIndex(KEY_CRIADO_EM)));
	 
	            numerosDesconsiderados.add(numeroDesconsiderado);
	        } while (c.moveToNext());
	    }
	 
	    return numerosDesconsiderados;
	}
	
	public int updateNumeroDesconsiderado(NumeroDesconsiderado numeroDesconsiderado) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_NUMERO, numeroDesconsiderado.getNumero());
	 
	    return db.update(TABELA_NUMERO_DESCONSIDERADO, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(numeroDesconsiderado.getId()) });
	}
	
	public void deleteNumeroDesconsiderado(NumeroDesconsiderado numeroDesconsiderado) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    db.delete(TABELA_NUMERO_DESCONSIDERADO, KEY_ID + " = ?",
	            new String[] { String.valueOf(numeroDesconsiderado.getId()) });
	}
	
	public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }



}
