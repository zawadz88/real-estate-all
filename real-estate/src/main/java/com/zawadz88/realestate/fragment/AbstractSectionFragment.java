package com.zawadz88.realestate.fragment;

import android.app.Activity;
import com.zawadz88.realestate.api.model.Section;

/**
 * Base fragment for displaying content for one of sections
 *
 * @author Piotr Zawadzki
 */
public class AbstractSectionFragment extends AbstractGridFragment {
	public static final String SECTION_FRAGMENT_TAG = "sectionFragment";
	/**
	 * Fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION = "section";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof SectionAttachedListener) {
			((SectionAttachedListener) activity).onSectionAttached((Section)getArguments().getSerializable(ARG_SECTION));
			getActivity().supportInvalidateOptionsMenu();//need to invalidate on activity's first start, because onCreateOptionsMenu(..) got executed before onSectionAttached(..)
		} else {
			throw new IllegalStateException("Activity must implement SectionAttachedListener!");
		}
	}

    /**
     * Listener interface that listens for when a section fragment gets attached to an activity
     */
	public static interface SectionAttachedListener {

        /**
         * Called when a section fragment gets attached to an activity
         * @param section section of the fragment that gets attached
         */
		void onSectionAttached(Section section);
	}

}
