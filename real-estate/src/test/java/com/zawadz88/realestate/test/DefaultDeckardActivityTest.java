package com.zawadz88.realestate.test;

import static org.junit.Assert.assertTrue;

import com.zawadz88.realestate.activity.RealEstateActivity;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;

import com.zawadz88.realestate.test.category.DefaultTestCategory;

@RunWith(RobolectricTestRunner.class)
public class DefaultDeckardActivityTest {

  @Test
  @Category(DefaultTestCategory.class)
  public void testSomething() throws Exception {
    Activity activity = Robolectric.buildActivity(RealEstateActivity.class).create().get();
	System.out.println("testSomething");
    assertTrue(activity != null);
  }
  
}
