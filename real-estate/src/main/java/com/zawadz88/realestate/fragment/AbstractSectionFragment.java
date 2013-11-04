package com.zawadz88.realestate.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.zawadz88.realestate.MainActivity;
import com.zawadz88.realestate.model.Section;

/**
 * Created: 04.11.13
 *
 * @author Zawada
 */
public class AbstractSectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	protected static final String ARG_SECTION = "section";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof SectionAttachedListener) {
			((SectionAttachedListener) activity).onSectionAttached((Section)getArguments().getSerializable(ARG_SECTION));
		} else {
			throw new IllegalStateException("Activity must implement SectionAttachedListener!");
		}
	}

	public static interface SectionAttachedListener {
		void onSectionAttached(Section section);
	}

}
