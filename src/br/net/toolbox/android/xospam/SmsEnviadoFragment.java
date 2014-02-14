package br.net.toolbox.android.xospam;

import br.net.toolbox.android.model.Sms;
import br.net.toolbox.android.xospam.helper.ListScrollLoader;
import br.net.toolbox.android.xospam.helper.SmsListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class SmsEnviadoFragment extends ListFragment {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        return inflater.inflate(R.layout.sms_list_enviado, container, false);
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);        
    }

	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);     
        ListView listaSms = getListView();    		
		
		listaSms.setOnScrollListener(new ListScrollLoader());
		setListAdapter(new SmsListAdapter(this.getActivity(), new Functions().getListaSmsEnviado(this.getActivity()) ));
        
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
		intent.putExtra("SMS_ENVIADO", "S");
		
		this.getActivity().startActivity(intent);
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
		setListAdapter(new SmsListAdapter(this.getActivity(), new Functions().getListaSmsEnviado(this.getActivity()) ));
	}
    
}