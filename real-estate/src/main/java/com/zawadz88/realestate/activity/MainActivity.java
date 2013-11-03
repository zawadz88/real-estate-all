package com.zawadz88.realestate.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.zawadz88.realestate.R;

public class MainActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawableResource(R.drawable.re_app_background_gradient);
    setContentView(R.layout.activity_main);
  }
}
