package br.net.toolbox.android.xospam.helper;

import android.database.ContentObserver;

public class ContactObserver extends ContentObserver {

	public ContactObserver() {
		super(null);
	}
	
	@Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        //verifica se o número inserido tem na lista de spam
        
        System.out.println (" Calling onChange" );
    }
}
