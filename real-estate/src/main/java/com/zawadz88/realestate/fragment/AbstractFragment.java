package com.zawadz88.realestate.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.zawadz88.realestate.RealEstateApplication;

/**
 * Created by Piotr on 28.12.13.
 */
public class AbstractFragment extends Fragment {

    protected RealEstateApplication mApplication;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mApplication = (RealEstateApplication) getActivity().getApplication();
    }

}
