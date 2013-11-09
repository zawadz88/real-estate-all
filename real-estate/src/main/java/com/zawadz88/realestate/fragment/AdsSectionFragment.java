package com.zawadz88.realestate.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.zawadz88.realestate.MainActivity;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.model.Section;

/**
 * Created: 04.11.13
 *
 * @author Zawada
 */
public class AdsSectionFragment extends AbstractSectionFragment {

	private GridView mAdsGridView;

	public static AdsSectionFragment newInstance() {
		AdsSectionFragment fragment = new AdsSectionFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_SECTION, Section.ADS);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_ads, container, false);

		mAdsGridView = (GridView) view.findViewById(R.id.ads_gridview);
		mAdsGridView.setAdapter(new AdsAdapter());

		return view;
	}

	private class AdsAdapter extends BaseAdapter {


		private String[] elements = new String[] {"aaaaa", "bbb dsa sadsadabb", "ccccc", "dd sdaa asdsa asd asd as sadsa dsa dsad sa dsaddd", "eeeee", "ffffff", "gggggg", "hhhh", "iiiii", "jjjjassssssssssssssssssssssssss saaaaaaaaaaaaaaaaaaaj", "aaaaa", "bbb dsa sadsadabb", "ccccc", "dd sdaa asdsa asd asd as sadsa dsa dsad sa dsaddd", "eeeee", "ffffff", "gggggg", "hhhh", "iiiii", "jjjjassssssssssssssssssssssssss saaaaaaaaaaaaaaaaaaaj", "aaaaa", "bbb dsa sadsadabb", "ccccc", "dd sdaa asdsa asd asd as sadsa dsa dsad sa dsaddd", "eeeee", "ffffff", "gggggg", "hhhh", "iiiii", "jjjjassssssssssssssssssssssssss saaaaaaaaaaaaaaaaaaaj"} ;

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
				LayoutInflater inflater = (LayoutInflater) AdsSectionFragment.this.getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.gridview_item_ads, parent, false);
			} else {
				view = convertView;
			}
			((TextView)view.findViewById(R.id.gridview_item_title)).setText(elements[position]);

			int modPos = position % 6;
			int resId = 0;
			switch (modPos) {
				case 0:
					resId = R.drawable.sample4;
					break;
				case 1:
					resId = R.drawable.offer_sample;
					break;
				case 2:
					resId = R.drawable.sample1;
					break;
				case 3:
					resId = R.drawable.sample2;
					break;
				case 4:
					resId = R.drawable.sample3;
					break;
				case 5:
					resId = R.drawable.sample5;
					break;
			}
			((ImageView)view.findViewById(R.id.gridview_item_image)).setImageResource(resId);
			//((ImageView)view.findViewById(R.id.gridview_item_image)).setImageResource(R.drawable.offer_sample);
			return view;  //To change body of implemented methods use File | Settings | File Templates.
		}
	}
}
