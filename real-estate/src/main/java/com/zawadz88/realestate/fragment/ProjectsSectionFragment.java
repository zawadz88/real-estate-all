package com.zawadz88.realestate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.api.model.Section;

/**
 * Created: 04.11.13
 *
 * @author Zawada
 */
public class ProjectsSectionFragment extends AbstractSectionFragment {

	private static final String LAST_KNOWN_SCROLL_POSITION = "scrollPosition";

	private GridView mProjectsGridView;

	public static ProjectsSectionFragment newInstance() {
		ProjectsSectionFragment fragment = new ProjectsSectionFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_SECTION, Section.PROJECTS);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_default_grid, container, false);

		mProjectsGridView = (GridView) view.findViewById(R.id.ads_gridview);
		mProjectsGridView.setAdapter(new AdsAdapter());

		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(LAST_KNOWN_SCROLL_POSITION)) {
			int position = savedInstanceState.getInt(LAST_KNOWN_SCROLL_POSITION);
			mProjectsGridView.smoothScrollToPosition(position);
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		int position = mProjectsGridView.getFirstVisiblePosition();
		outState.putInt(LAST_KNOWN_SCROLL_POSITION, position);
	}

	private class AdsAdapter extends BaseAdapter {


		private String[] elements = new String[] {"e asad asde", "fsdfadsfdfdsa sdf dsafdsf dsfd fsdfffff", "lorem ipsum", " asd sdsa", "iiiii", "sfdf saaaaaaaaaaaaaaaaaaaj", "aaaaa", "bbb dsa sadsadabb", "ccccc", "dd sdaa asdsa asd asd as sadsa dsa dsad sa dsaddd", "eeeee", "ffffff", "gggggg", "hhhh", "iiiii", "jjjjassssssssssssssssssssssssss saaaaaaaaaaaaaaaaaaaj", "aaaaa", "bbb dsa sadsadabb", "ccccc", "dd sdaa asdsa asd asd as sadsa dsa dsad sa dsaddd", "eeeee", "ffffff", "gggggg", "hhhh", "iiiii", "jjjjassssssssssssssssssssssssss saaaaaaaaaaaaaaaaaaaj"} ;

		@Override
		public int getCount() {
			return elements.length;  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public Object getItem(final int position) {
			return elements[position];  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public long getItemId(final int position) {
			return 0;  //To change body of implemented methods use File | Settings | File Templates.
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			View view;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				LayoutInflater inflater = (LayoutInflater) ProjectsSectionFragment.this.getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.section_gridview_item, parent, false);
			} else {
				view = convertView;
			}
			((TextView)view.findViewById(R.id.gridview_item_title)).setText(elements[position]);

			int modPos = position % 6;
			int resId = 0;
			switch (modPos) {
				case 0:
					resId = R.drawable.sample2;
					break;
				case 1:
					resId = R.drawable.offer_sample;
					break;
				case 2:
					resId = R.drawable.sample3;
					break;
				case 3:
					resId = R.drawable.sample4;
					break;
				case 4:
					resId = R.drawable.sample5;
					break;
				case 5:
					resId = R.drawable.sample1;
					break;
			}
			((ImageView)view.findViewById(R.id.gridview_item_image)).setImageResource(resId);
			//((ImageView)view.findViewById(R.id.gridview_item_image)).setImageResource(R.drawable.offer_sample);
			return view;  //To change body of implemented methods use File | Settings | File Templates.
		}
	}
}
