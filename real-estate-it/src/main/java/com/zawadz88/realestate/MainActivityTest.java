/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zawadz88.realestate;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import com.jayway.android.robotium.solo.Condition;
import com.jayway.android.robotium.solo.Solo;
import com.zawadz88.realestate.fragment.NavigationDrawerFragment;


/**
 * Make sure that the main launcher activity opens up properly, which will be
 * verified by {@link ActivityInstrumentationTestCase#testActivityTestCaseSetUpProperly}.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;
    /**
     * The first constructor parameter must refer to the package identifier of the
     * package hosting the activity to be launched, which is specified in the AndroidManifest.xml
     * file.  This is not necessarily the same as the java package name of the class - in fact, in
     * some cases it may not match at all.
     */
    public MainActivityTest() {
        super(MainActivity.class);
    }

	@Override
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

    @SmallTest
    public void testActivityNotNull() throws Exception {
        assertNotNull(solo.getCurrentActivity());
    }

	@SmallTest
	public void testNavigationLayoutsNotNull() {
		DrawerLayout drawerLayout = (DrawerLayout) solo.getView(R.id.drawer_layout);
		Fragment navigationDrawer = getActivity().getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		FrameLayout contentLayout = (FrameLayout) solo.getView(R.id.content_container);
		assertNotNull("DrawerLayout does not exist!", drawerLayout);
		assertNotNull("navigationDrawer does not exist!", navigationDrawer);
		assertNotNull("contentLayout does not exist!", contentLayout);
	}

	@SmallTest
	public void testNavigationDrawerListNotEmpty() {
		ListView listView = (ListView) solo.getView(R.id.navigation_list);
		assertNotNull(listView);
		assertNotNull(listView.getAdapter());
		assertFalse(listView.getAdapter().isEmpty());
	}

	@SmallTest
	public void testContentReplacementOnNavigationDrawerListItemClicked() {
		final int articlesPosition = MainActivity.RE_NAVIGATION_ARTICLES;
		final ListView listView = (ListView) solo.getView(R.id.navigation_list);
		assertEquals(solo.getString(R.string.title_ads), getActivity().getActionBarTitle());
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listView.performItemClick(listView.getAdapter().getView(articlesPosition, null, null),articlesPosition, listView.getAdapter().getItemId(articlesPosition));
			}
		});
		Condition titleChangedCondition = new Condition() {
			@Override
			public boolean isSatisfied() {
				return solo.getString(R.string.title_articles).equals(getActivity().getActionBarTitle());
			}
		};
		assertTrue(solo.waitForCondition(titleChangedCondition, 2000));
	}

	@SmallTest
	public void testAdsGridLayoutPresent() {
		GridView gridView = (GridView) solo.getView(R.id.ads_gridview);
		assertNotNull("Couldn't find ads gridView!", gridView);
		assertTrue("GridView is empty!", gridView.getChildCount() > 0);
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}


}

