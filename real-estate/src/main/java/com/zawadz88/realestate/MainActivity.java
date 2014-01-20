package com.zawadz88.realestate;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.zawadz88.realestate.api.model.Section;
import com.zawadz88.realestate.fragment.*;

/**
 * Main activity of the application
 *
 * @author Piotr Zawadzki
 */
public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, AbstractSectionFragment.SectionAttachedListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Drawer layout containing the navigation drawer and content. This is null for 10'' tablets in landscape mode
     */
    private DrawerLayout mDrawerLayout;

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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, mDrawerLayout);
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
		Section selectedSection = Section.getSectionForPosition(position);
        AbstractSectionFragment sectionFragment = (AbstractSectionFragment) fragmentManager.findFragmentByTag(AbstractSectionFragment.SECTION_FRAGMENT_TAG);

		//replace only if the fragment is different than the one added before (this includes orientation change handling)
		if (sectionFragment == null
				|| sectionFragment.getArguments() == null
				|| !sectionFragment.getArguments().containsKey(AbstractSectionFragment.ARG_SECTION)
				|| !selectedSection.equals(sectionFragment.getArguments().getSerializable(AbstractSectionFragment.ARG_SECTION))) {
			switch (selectedSection) {
				case ADS:
					sectionFragment = AdsSectionFragment.newInstance();
					break;
				case ARTICLES:
					sectionFragment = ArticlesSectionFragment.newInstance();
					break;
				case PROJECTS:
					sectionFragment = ProjectsSectionFragment.newInstance();
					break;
			}


		}
		fragmentManager.beginTransaction()
				.replace(R.id.content_container, sectionFragment, AbstractSectionFragment.SECTION_FRAGMENT_TAG)
				.commit();
	}

	@Override
	public void onSectionAttached(Section section) {
		mTitle = getString(section.getTitleResourceId());
	}

	/**
	 * Sets the title of the action bar to selected section's name
	 */
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

    /**
     * Returns the current Action Bar Title
     * @return
     */
	public String getActionBarTitle() {
		return String.valueOf(mTitle);
	}

    /**
     * Returns true in navigation drawer is open or if drawer layout is not present (navigation drawer fragment is always visible then)
     * @return
     */
    public boolean isDrawerOpen() {
        return mDrawerLayout != null ? mDrawerLayout.isDrawerOpen(findViewById(R.id.navigation_drawer)) : true;
    }

}