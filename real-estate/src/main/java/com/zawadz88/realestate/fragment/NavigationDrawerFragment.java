package com.zawadz88.realestate.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.zawadz88.realestate.R;

/**
 * Created: 03.11.13
 * @author Zawada
 */
public class NavigationDrawerFragment extends Fragment {

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		ListView view = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
		return view;
	}
}
