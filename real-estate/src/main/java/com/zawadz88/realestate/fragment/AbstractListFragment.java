package com.zawadz88.realestate.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.model.Section;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class AbstractListFragment extends Fragment {

	protected RealEstateApplication mApplication;

	protected boolean loadingMoreItems = false;
	protected boolean loading = false;
	protected boolean endOfItemsReached = false;
	protected boolean connectionAvailable = true;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mApplication = (RealEstateApplication) getActivity().getApplication();
	}
}
