package hight.ht.helper;

import java.util.List;

import hight.ht.datahandling.DatabaseHelper;
import hight.ht.datahandling.Liga;
import hight.ht.hvs.LigaTabActivity;
import hight.ht.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class LigenAdapter extends BaseAdapter{
	
	DatabaseHelper dbh;
	Context context;
    List<Liga> ligen;
    public LigenAdapter(Context context, List<Liga> ligen){
         this.context = context;
         this.ligen = ligen;
         dbh = DatabaseHelper.getInstance(context);
       }
       @Override
       public int getCount() {
           return ligen.size();
       }

       @Override
       public Object getItem(int position) {
           return ligen.get(position);
       }

       @Override
       public long getItemId(int position) {
           return position;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           if (convertView == null) {
               LayoutInflater mInflater = (LayoutInflater)
                       context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
               convertView = mInflater.inflate(R.layout.list_ligen, null);
           }

           Button button = (Button) convertView.findViewById(R.id.buttonLiga);
           if(ligen.get(position).getJugend().equals("")){
        	   button.setText(ligen.get(position).getName());
           }else{
        	   button.setText(ligen.get(position).getName()+" ("+ligen.get(position).getJugend()+"-Jugend)"); 
           }
           //button.setText(ligen.get(position).getName()+" "+ligen.get(position).getJugend());
           button.setHint("ligaNr:" + ligen.get(position).getLigaNr());
           button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Perform action on click
					TextView b = (TextView) v;
					int ligaNr = Integer.parseInt(b.getHint().toString().split(":")[1]);
					Intent intent = new Intent(context, LigaTabActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("liganame", (String) b.getText());
					bundle.putInt("liganummer", ligaNr);
					intent.putExtras(bundle);
					dbh.addLog("Liga gestartet", ligaNr);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
			});

           return convertView;
       }

}
