package com.zawadz88.realestate.activity;

import android.app.Activity;
import android.os.Bundle;
import com.zawadz88.realestate.R;

public class RealEstateActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawableResource(R.drawable.re_app_background_gradient);
    setContentView(R.layout.deckard);
  }
}
