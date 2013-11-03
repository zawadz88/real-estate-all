package com.zawadz88.realestate.test;

import static org.junit.Assert.assertTrue;

import com.zawadz88.realestate.activity.MainActivity;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;

import com.zawadz88.realestate.test.category.NetworkingTestCategory;

@RunWith(RobolectricTestRunner.class)
public class NetworkingDeckardActivityTest {
  
  @Test
  @Category(NetworkingTestCategory.class)
  public void testSomethingWithNetwork() throws Exception {
    Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();
	System.out.println("testSomethingWithNetwork");
    assertTrue(activity != null);
  }
}
