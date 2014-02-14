package br.net.toolbox.android.xospam.helper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.net.toolbox.android.model.Sms;
import br.net.toolbox.android.xospam.R;

public class SmsListAdapter extends ArrayAdapter<Sms> {
	
	private final Context context;
	//private final Sms[] values;
	private final List<Sms> values;
	
	public SmsListAdapter(Context context, List<Sms> values) {
	    super(context, R.layout.sms_list_item, values);
	    this.context = context;
	    this.values = values;	    
	  }
	
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View rowView = inflater.inflate(R.layout.sms_list_item, parent, false);
	    
	    TextView textId = (TextView) rowView.findViewById(R.id.textId);
	    textId.setText(String.valueOf(values.get(position).getId()));
	    TextView textNumero = (TextView) rowView.findViewById(R.id.textNumero);	    
	    textNumero.setText(values.get(position).getNumero());
	    TextView textData = (TextView) rowView.findViewById(R.id.textData);
	    
	    String data = new DataFormaterHelper().formatData(Long.valueOf(values.get(position).getData()));
	    
	    textData.setText(data);
	    TextView textMensagem = (TextView) rowView.findViewById(R.id.textMensagem);	    
	    textMensagem.setText(values.get(position).getMensagem());
	    
	    /*rowView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, SmsDetailActivity.class);
				Sms sms = new Sms();
				sms.setData(((TextView) view.findViewById(R.id.textData)).getText().toString());	    
				sms.setNumero(((TextView) view.findViewById(R.id.textNumero)).getText().toString());
				sms.setMensagem(((TextView) view.findViewById(R.id.textMensagem)).getText().toString());
				
				intent.putExtra("SMS", sms);	
				context.startActivity(intent);
				
			}
		});*/
	    
	    return rowView;
	  }
	  
	  
}
