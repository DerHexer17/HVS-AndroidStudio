package hight.ht.hvs;

import hight.ht.helper.SpieleAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;
import hight.ht.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import hight.ht.datahandling.DatabaseHelper;
import hight.ht.datahandling.Halle;
import hight.ht.datahandling.Spiel;

public class SpielDetailsFragment extends Fragment {

	int ligaNr;
	int spielNr;
	DatabaseHelper dbh;
	Spiel spiel;
	Halle halle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_spiel_details, container, false);

		this.ligaNr = getActivity().getIntent().getIntExtra("liganummer", 0);
		this.spielNr = getActivity().getIntent().getIntExtra("spielnummer", 0);

		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());
		spiel = dbh.getGame(ligaNr, spielNr);
		halle = dbh.getHalle(spiel.getHalle());
		

		TextView titel = (TextView) rootView.findViewById(R.id.textTitelSpielDetail);
		TextView ergebnis = (TextView) rootView.findViewById(R.id.textErgebnisSpielDetail);
		if(spiel.getToreHeim() > 0 || spiel.getToreGast() > 0){
			
			ergebnis.setText(spiel.getToreHeim()+" : "+spiel.getToreGast());
		}else{
			ergebnis.setHeight(1);
		}
		TextView datum = (TextView) rootView.findViewById(R.id.spielDetailsDatum);
		TextView textHalle = (TextView) rootView.findViewById(R.id.spielDetailsHalle);
		TextView textSR = (TextView) rootView.findViewById(R.id.spielDetailsSR);

		titel.setText(spiel.getTeamHeim() + " - " + spiel.getTeamGast());

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.GERMANY);
			datum.setText("Datum: " + formatter.format(spiel.getDate()) + " Uhr");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		textHalle.setText("Spielhalle: " + halle.getName() + " (" + halle.kompletteAdresse() + ")");
		textSR.setText("Schiedsrichter: " + spiel.getSchiedsrichter());

		if (textSR.getText().toString().split(" ")[1].equals("null")) {
			textSR.setText("Schiedsrichter: unbekannt");
		}
		
		ListView lv = (ListView) rootView.findViewById(R.id.listWeitereSpieleInHalle);
		SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
		lv.setAdapter(new SpieleAdapter(getActivity().getApplicationContext(), dbh.getAllSpieleInHalle(halle.getHallenNr(), sf.format(spiel.getDate())), dbh, 1));

		return rootView;
	}

}
