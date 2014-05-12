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

package com.zawadz88.realestate.test;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.test.FlakyTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import com.jayway.android.robotium.solo.Condition;
import com.jayway.android.robotium.solo.Solo;
import com.zawadz88.realestate.AdActivity_;
import com.zawadz88.realestate.MainActivity;
import com.zawadz88.realestate.model.Section;

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
		DrawerLayout drawerLayout = (DrawerLayout) solo.getView(com.zawadz88.realestate.R.id.drawer_layout);
		Fragment navigationDrawer = getActivity().getSupportFragmentManager().findFragmentById(com.zawadz88.realestate.R.id.navigation_drawer);
		FrameLayout contentLayout = (FrameLayout) solo.getView(com.zawadz88.realestate.R.id.content_container);
		assertNotNull("DrawerLayout does not exist!", drawerLayout);
		assertNotNull("navigationDrawer does not exist!", navigationDrawer);
		assertNotNull("contentLayout does not exist!", contentLayout);
	}

	@SmallTest
	public void testNavigationDrawerListNotEmpty() {                                                                                                  		ListView listView = (ListView) solo.getView(com.zawadz88.realestate.R.id.navigation_list);
		assertNotNull(listView);
		assertNotNull(listView.getAdapter());
		assertFalse(listView.getAdapter().isEmpty());
	}

	@FlakyTest
	public void testContentReplacementOnNavigationDrawerListItemClicked() {
		final int articlesPosition = Section.ARTICLES.getPosition();
		final ListView listView = (ListView) solo.getView(com.zawadz88.realestate.R.id.navigation_list);
		assertEquals(solo.getString(com.zawadz88.realestate.R.string.section_title_ads), getActivity().getActionBarTitle());
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listView.performItemClick(listView.getAdapter().getView(articlesPosition, null, null),articlesPosition, listView.getAdapter().getItemId(articlesPosition));
			}
		});
		Condition titleChangedCondition = new Condition() {
			@Override
			public boolean isSatisfied() {
				return solo.getString(com.zawadz88.realestate.R.string.section_title_articles).equals(getActivity().getActionBarTitle());
			}
		};
		assertTrue(solo.waitForCondition(titleChangedCondition, 2000));
	}

	@SmallTest
	public void testAdsGridLayoutPresent() {
		GridView gridView = (GridView) solo.getView(com.zawadz88.realestate.R.id.ads_gridview);
		assertNotNull("Couldn't find ads gridView!", gridView);
		assertTrue("GridView is empty!", gridView.getChildCount() > 0);
	}

	public void testClickOnAdsItem() {
		final int clickedItemIndex = 2;
		solo.clickInList(clickedItemIndex, 0);
		assertTrue(solo.waitForActivity(AdActivity_.class.getSimpleName()));
		assertNotNull(solo.getCurrentActivity());
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}


}

