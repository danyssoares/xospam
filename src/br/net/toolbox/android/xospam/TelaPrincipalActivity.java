package br.net.toolbox.android.xospam;

import br.net.toolbox.android.xospam.helper.ContactObserver;
import br.net.toolbox.android.xospam.helper.TabsPageAdapter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;

public class TelaPrincipalActivity extends FragmentActivity implements ActionBar.TabListener {
	
	private ViewPager viewPager;
	private TabsPageAdapter tabsPageAdapter;
	private ActionBar actionBar;
	
	//Tab titles
	private String[] tabs = {"Sms Recebidos", "Sms Enviados", "Números Desconsiderados" };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ContactObserver contactObserver = new ContactObserver();
		getApplicationContext().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, contactObserver);
		
		Functions functions = new Functions();
		
		/* Código para resetar as preferências e o Banco de Dados
		functions.clearSharedPreferences(this);
		*/
		
		
		if(functions.ePrimeiraExecucao(this)){
        	launchConfig();
        	functions.carregarBancoDeDados(this);
        }
		
		setContentView(R.layout.tela_principal_activity);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        tabsPageAdapter = new TabsPageAdapter(getSupportFragmentManager());
		
		
        viewPager.setAdapter(tabsPageAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);     

     // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        	 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
         
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
         
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.config:
            	launchConfig();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void launchConfig(){
    	ConfigDialog configDialog = new ConfigDialog(this);
    	
    	configDialog.show();
        
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
