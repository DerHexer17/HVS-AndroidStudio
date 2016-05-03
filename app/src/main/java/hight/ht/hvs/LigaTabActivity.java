package hight.ht.hvs;


import hight.ht.R;
import java.util.List;

import hight.ht.datahandling.DatabaseHelper;
import hight.ht.datahandling.Liga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class LigaTabActivity extends ActionBarActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	int ligaNr;
	Liga liga;
	DatabaseHelper dbh;
	List<String> alleEbenen;
	String TAG = "logging";
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//LOG = Logger.getLogger(this.getClass().getName());
		//LOG.addHandler(StartActivity.handler);
		setContentView(R.layout.activity_liga_tab);

		ligaNr = getIntent().getIntExtra("liganummer", 0);
		Log.d(TAG, "Liga geladen;ligaNr: "+ligaNr);
		dbh = DatabaseHelper.getInstance(getApplicationContext());
		liga = dbh.getLiga(ligaNr);
		this.setTitle(getIntent().getStringExtra("liganame"));
		
		//Toast.makeText(getApplicationContext(), "Anzahl aufrufe diese Liga: "+dbh.getCountLigaAufruf(ligaNr), Toast.LENGTH_SHORT).show();

	
		if(dbh.getCountLigaAufruf(ligaNr) > 10 && liga.isFavorit()!=true && dbh.keineFavoritenAbfrage(ligaNr)==false){
			dialogZuFavoritenHinzu();
		}
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
		
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//Toast.makeText(getApplicationContext(), "Ist Liga Favorit? "+liga.isFavorit(), Toast.LENGTH_SHORT).show();

		if(liga.isFavorit()){
			getMenuInflater().inflate(R.menu.liga_tab_remove_favorit, menu);
		}else{
			getMenuInflater().inflate(R.menu.liga_tab_add_favorit, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			return true;
		/*case R.id.liga_refresh_small:
			kleinesUpdate();
			return true;*/
		case R.id.liga_refresh_full:
			grossesUpdate();
			return true;
		//case R.id.map:
		//	openMap();
		//	return true;
		case R.id.menueAddFavorit:
			addLigaZuFavoriten(ligaNr);
			return true;
		case R.id.menueRemoveFavorit:
			removeLigaVonFavoriten(ligaNr);
			return true;
		case R.id.ligaZuruecksetzen:
			ligaEntfernen();
			return true;
		
		}

		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			switch (position) {
			case 0:
				return new LigaSpieleFragment();
			case 1:
				return new LigaTabelleFragment();
			default:
				return PlaceholderFragment.newInstance(position + 1);
			}

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.liga_tab1);
			case 1:
				return getString(R.string.liga_tab2);
			case 2:
				return getString(R.string.liga_tab3);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_liga_tab, container, false);
			return rootView;
		}
	}

	// Hier die Methoden, damit auf Button klick zum nächsten oder vorherigen
	// Spieltag gesprungen wird
	public void vorherigerSpieltag(View view) {
		Spinner sp = (Spinner) findViewById(R.id.spinnerSpieltage);
		sp.setSelection(sp.getSelectedItemPosition() - 1);
	}

	public void naechsterSpieltag(View view) {
		Spinner sp = (Spinner) findViewById(R.id.spinnerSpieltage);
		sp.setSelection(sp.getSelectedItemPosition() + 1);
	}

	public void kleinesUpdate() {
		Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
		intent.putExtra("liga", ligaNr);
		intent.putExtra("update", 1);
		intent.putExtra("liganame", this.getTitle());
		// Spinner spinner = (Spinner) findViewById(R.id.spinnerSpieltage);
		// intent.putExtra("spinnerPos", spinner.getSelectedItemPosition());
		this.finish();
		startActivity(intent);

	}

	public void grossesUpdate() {
		Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
		intent.putExtra("liga", ligaNr);
		intent.putExtra("update", 2);
		intent.putExtra("liganame", this.getTitle());
		this.finish();
		startActivity(intent);
	}

	public void openMap() {
		Intent intent = new Intent(getApplicationContext(), MapActivity.class);
		intent.putExtra("liga", ligaNr);
		Spinner sp = (Spinner) findViewById(R.id.spinnerSpieltage);
		intent.putExtra("spieltag", sp.getSelectedItemPosition() + 1);

		startActivity(intent);
	}
	
	public void addLigaZuFavoriten(int ligaNr){
		liga.setFavorit(true);
		dbh.updateLiga(liga);
	}
	
	public void removeLigaVonFavoriten(int ligaNr){
		liga.setFavorit(false);
		dbh.updateLiga(liga);
	}
	
	public void dialogZuFavoritenHinzu(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Liga zu Favoriten hinzu?");
        alert.setMessage("Ich merke, du guckst dir diese Liga öfter an. Soll ich sie zu deinen Favoriten hinzufügen?");

        // Set an EditText view to get user input
        final CheckBox cb = new CheckBox(this);
        cb.setText("Nicht nochmal fragen");

        alert.setView(cb);

        alert.setNegativeButton("Ja", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                addLigaZuFavoriten(ligaNr);
                // Do something with value!
            }
        });

        alert.setPositiveButton("Nein", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            	if(cb.isChecked()){
            		dbh.addLog("keinFavoritAbfrage", ligaNr);
            	}
            }
        });


        alert.show();
	}
	
	public int ligaEntfernen(){
		Liga l = dbh.getLiga(ligaNr);
		l.setInitial("Nein");
		dbh.updateLiga(l);
		
		int i = dbh.deleteAllGamesFromLeague(ligaNr);
		Toast.makeText(getApplicationContext(), "Anzahl gelöschter Spiele: "+i, Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(getApplicationContext(), LigawahlActivity.class);
		startActivity(intent);
		return i;
	}
}
