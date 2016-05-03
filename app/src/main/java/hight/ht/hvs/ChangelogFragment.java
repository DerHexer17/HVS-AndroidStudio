package hight.ht.hvs;

import hight.ht.datahandling.DatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import hight.ht.R;

public class ChangelogFragment extends Fragment{
	
	DatabaseHelper dbh;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_changelog, container, false);
		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());
		
		return rootView;
	}

}
