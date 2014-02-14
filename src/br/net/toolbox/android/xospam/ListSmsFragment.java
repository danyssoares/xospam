package br.net.toolbox.android.xospam;

import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import br.net.toolbox.android.model.Sms;
import br.net.toolbox.android.xospam.helper.ListScrollLoader;
import br.net.toolbox.android.xospam.helper.SmsListAdapter;

public class ListSmsFragment extends ListFragment {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        return inflater.inflate(R.layout.sms_list, container, false);
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);     
        ListView listaSms = getListView();    		
		
		listaSms.setOnScrollListener(new ListScrollLoader());
		setListAdapter(new SmsListAdapter(this.getActivity(), new Functions().getListaSmsRecebido(this.getActivity()) ));
        
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	  
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    
	    Sms sms = (Sms) getListAdapter().getItem(info.position);
	    
	    menu.setHeaderTitle(sms.getNumero());
	    
	    String[] menuItems = getResources().getStringArray(R.array.menuSms);
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	  
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this.getActivity(), SmsDetailActivity.class);
		Sms sms = new Sms();
		sms.setId( Long.valueOf(((TextView) v.findViewById(R.id.textId)).getText().toString()) );
		sms.setData(((TextView) v.findViewById(R.id.textData)).getText().toString());	    
		sms.setNumero(((TextView) v.findViewById(R.id.textNumero)).getText().toString());
		sms.setMensagem(((TextView) v.findViewById(R.id.textMensagem)).getText().toString());
		
		intent.putExtra("SMS", sms);	
		this.getActivity().startActivity(intent);
    }

	@Override
	public void onResume() {		
		super.onResume();
		
		setListAdapter(new SmsListAdapter(this.getActivity(), new Functions().getListaSmsRecebido(this.getActivity()) ));
	}
}