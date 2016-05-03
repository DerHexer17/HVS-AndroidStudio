package hight.ht.datahandling;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.Activity;
import android.util.Log;

public class DBGateway {
	private DatabaseHelper dbh;

	public DBGateway(Activity activiy) {
		this.dbh = DatabaseHelper.getInstance(activiy);
	}

	public void saveGamesIntoDB(List<Spiel> spiele) {
		int ligaNr = spiele.get(2).getLigaNr();
		String saison = dbh.getLiga(ligaNr).getSaison();
		List<Spiel> spieleOfSpieltag = new ArrayList<Spiel>();

		Spieltag spieltag = new Spieltag();
		int spieltagsNr = 0;
		GregorianCalendar aktuell = new GregorianCalendar();
		GregorianCalendar aktuellPlusOne = new GregorianCalendar();
		GregorianCalendar aktuellPlusTwo = new GregorianCalendar();
		GregorianCalendar pruefung = new GregorianCalendar();
		long startTime = System.currentTimeMillis();
		for (Spiel s : spiele) {
			pruefung.setTime(s.getDate());
			aktuellPlusOne.setTime(aktuell.getTime());
			aktuellPlusOne.add(Calendar.DAY_OF_YEAR, 1);
			aktuellPlusTwo.setTime(aktuell.getTime());
			aktuellPlusTwo.add(Calendar.DAY_OF_YEAR, 2);

			if (spieltagsNr == 0) {
				aktuell.setTime(s.getDate());
				spieltagsNr++;
				spieltag.setLigaNr(ligaNr);
				spieltag.setSaison(saison);
				spieltag.setSpieltags_Nr(spieltagsNr);
				spieltag.setSpieltags_Name(spieltagsNr + ". Spieltag");
				spieltag.setDatumBeginn(s.getDate());
			}

			if (aktuell.get(Calendar.YEAR) == pruefung.get(Calendar.YEAR)
					&& (aktuell.get(Calendar.DAY_OF_YEAR) == pruefung.get(Calendar.DAY_OF_YEAR) || aktuellPlusOne.get(Calendar.DAY_OF_YEAR) == pruefung.get(Calendar.DAY_OF_YEAR) || aktuellPlusTwo.get(Calendar.DAY_OF_YEAR) == pruefung.get(Calendar.DAY_OF_YEAR))) {
				s.setSpieltagsNr(spieltagsNr);
				spieleOfSpieltag.add(s);
				aktuell.setTime(pruefung.getTime());
			} else {
				spieltag.setDatumEnde(aktuell.getTime());
				dbh.addSpieltag(spieltag);
				dbh.addSpieleForSpieltag(spieleOfSpieltag);
				spieleOfSpieltag.clear();

				spieltagsNr++;
				spieltag.setSpieltags_Nr(spieltagsNr);
				spieltag.setSpieltags_Name(spieltagsNr + ". Spieltag");
				spieltag.setDatumBeginn(s.getDate());
				s.setSpieltagsNr(spieltagsNr);
				spieleOfSpieltag.add(s);
				aktuell.setTime(s.getDate());
			}
		}
		spieltag.setDatumEnde(aktuell.getTime());
		dbh.addSpieltag(spieltag);
		dbh.addSpieleForSpieltag(spieleOfSpieltag);
		long diff = System.currentTimeMillis() - startTime;
		Log.d("BENNI", "DB Exec Time: " + Long.toString(diff) + "ms");

	}

	public void updateResults(List<Spiel> spiele) {
		long startTime = System.currentTimeMillis();
		for (Spiel s : spiele) {
			dbh.updateSpiel(s, true);
		}
		long diff = System.currentTimeMillis() - startTime;
		Log.d("BENNI", "DB Exec Time: " + Long.toString(diff) + "ms");
	}

	public void updateGamesInDB(List<Spiel> spiele) {
		long startTime = System.currentTimeMillis();
		for (Spiel s : spiele) {
			dbh.updateSpiel(s, false);
		}
		long diff = System.currentTimeMillis() - startTime;
		Log.d("BENNI", "DB Exec Time: " + Long.toString(diff) + "ms");
	}
}
