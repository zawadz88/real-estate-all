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

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import com.jayway.android.robotium.solo.Solo;
import com.zawadz88.realestate.model.Section;

public class AdActivityTest extends ActivityInstrumentationTestCase2<AdActivity> {

    /**
     * The first constructor parameter must refer to the package identifier of the
     * package hosting the activity to be launched, which is specified in the AndroidManifest.xml
     * file.  This is not necessarily the same as the java package name of the class - in fact, in
     * some cases it may not match at all.
     */
    public AdActivityTest() {
        super(AdActivity.class);
    }

	@SmallTest
	public void testActivityNotNull() throws Exception {
		assertNotNull(getActivity());
	}

	@SmallTest
	public void testTitleIsSet() {
		Solo solo = new Solo(getInstrumentation(), getActivity());
		AdActivity adsActivity = (AdActivity) solo.getCurrentActivity();
		CharSequence title = adsActivity.getSupportActionBar().getTitle();
		assertEquals(solo.getString(Section.ADS.getTitleResourceId()), title);
		solo.finishOpenedActivities();
	}

}

