package br.net.toolbox.android.xospam.helper;

import java.util.List;

import android.util.Log;
import android.widget.AbsListView;
import br.net.toolbox.android.model.Sms;
import br.net.toolbox.android.xospam.Functions;

public class ListScrollLoader implements AbsListView.OnScrollListener{

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		if (firstVisibleItem + visibleItemCount >= totalItemCount){
			
			SmsListAdapter listAdapter = (SmsListAdapter)view.getAdapter();
			
			/*if (listAdapter != null){
			
				List<Sms> lista = new Functions().getListaSms(view.getContext(), totalItemCount);
			
				listAdapter.addAll(lista);
				listAdapter.notifyDataSetChanged();
				
			}*/
			
		}
		
		
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		Log.d("XoSpam - scrollState", String.valueOf(scrollState));
	}

}
