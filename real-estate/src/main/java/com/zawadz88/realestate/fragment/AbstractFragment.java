package com.zawadz88.realestate.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.zawadz88.realestate.service.ContentHolder;
import com.zawadz88.realestate.RealEstateApplication;

/**
 * Base class for other real estate fragments
 *
 * @author Piotr Zawadzki
 */
public class AbstractFragment extends Fragment {

    protected RealEstateApplication mApplication;

	protected enum ViewState {
        LOADING, CONTENT, EMPTY, NO_INTERNET, ERROR
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mApplication = (RealEstateApplication) getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ContentHolder getContentHolder() {
        return mApplication.getContentHolder();
    }
}
