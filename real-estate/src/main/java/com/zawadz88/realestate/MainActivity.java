package com.zawadz88.realestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.TextView;
import com.zawadz88.realestate.fragment.AbstractSectionFragment;
import com.zawadz88.realestate.fragment.AdsSectionFragment;
import com.zawadz88.realestate.fragment.NavigationDrawerFragment;
import com.zawadz88.realestate.model.Section;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, AbstractSectionFragment.SectionAttachedListener {

	public static final int RE_NAVIGATION_ADS = 0;
	public static final int RE_NAVIGATION_ARTICLES = 1;
	public static final int RE_NAVIGATION_PROJECTS = 2;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(R.drawable.re_app_background_gradient);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			//TODO implement
			//getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		//TODO implement
		/*switch (item.getItemId()) {
			case R.id.action_settings:
				return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onNavigationDrawerItemSelected(final int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment sectionFragment = null;
		Section selectedSection = Section.getSectionForPosition(position);
		switch (selectedSection) {
			case ADS:
				sectionFragment = AdsSectionFragment.newInstance();
				break;
			case ARTICLES:
				sectionFragment = PlaceholderFragment.newInstance(position);
				break;
			case PROJECTS:
				sectionFragment = PlaceholderFragment.newInstance(position);
				break;
		}
		fragmentManager.beginTransaction()
				.replace(R.id.content_container, sectionFragment)
				.commit();
	}

	public void onSectionAttached(Section section) {
		System.out.println("onSectionAttached " + section);
		mTitle = getString(section.getTitleResourceId());
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	public CharSequence getActionBarTitle() {
		return mTitle;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends AbstractSectionFragment {

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putSerializable(ARG_SECTION, Section.getSectionForPosition(sectionNumber));
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			TextView textView = (TextView) rootView.findViewById(R.id.section_label);
			textView.setText(getArguments().getSerializable(ARG_SECTION).toString());
			return rootView;
		}

	}
}
