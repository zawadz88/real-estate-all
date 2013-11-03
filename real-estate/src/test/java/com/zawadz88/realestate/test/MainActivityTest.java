package com.zawadz88.realestate.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.activity.MainActivity;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;

import com.zawadz88.realestate.test.category.DefaultTestCategory;

@RunWith(RobolectricTestRunner.class)
@Category(DefaultTestCategory.class)
public class MainActivityTest {

	private MainActivity activity;

	@Before
	public void setUp() {
		activity = Robolectric.buildActivity(MainActivity.class).create().get();
	}

	@Test
	public void testActivityNotNull() throws Exception {
		assertNotNull("Activity is null!", activity);
	}

	@Test
	public void testNavigationLayoutsNotNull() {
		DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		Fragment navigationDrawer = activity.getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		FrameLayout contentLayout = (FrameLayout) activity.findViewById(R.id.content_container);
		assertNotNull("DrawerLayout does not exist!", drawerLayout);
		assertNotNull("navigationDrawer does not exist!", navigationDrawer);
		assertNotNull("contentLayout does not exist!", contentLayout);
	}

}
