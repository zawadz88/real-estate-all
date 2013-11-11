package com.zawadz88.realestate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.model.ArticleCategory;
import com.zawadz88.realestate.model.Section;

import java.util.ArrayList;

/**
 * Created: 04.11.13
 *
 * @author Zawada
 */
public class ArticlesSectionFragment extends AbstractSectionFragment {

	private static final String LAST_KNOWN_SCROLL_POSITION = "scrollPosition";

	private ViewPager mArticlesPager;

	public static ArticlesSectionFragment newInstance() {
		ArticlesSectionFragment fragment = new ArticlesSectionFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_SECTION, Section.ARTICLES);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_articles, container, false);

		mArticlesPager = (ViewPager) view.findViewById(R.id.articles_viewpager);
		mArticlesPager.setAdapter(new ArticlesPagerAdapter(getActivity().getApplicationContext(), getChildFragmentManager()));

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.article_tabs);
		tabs.setViewPager(mArticlesPager);

		return view;
	}
	public static class ArticlesPagerAdapter extends FragmentPagerAdapter {

		private Context context;

		private ArticleCategory[] categories = ArticleCategory.values();

		public ArticlesPagerAdapter(Context context, FragmentManager fm) {
			super(fm);
			this.context = context;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return context.getString(categories[position].getTitleResource());
		}

		@Override
		public Fragment getItem(int index) {
			ArticleCategory category = categories[index];

			return ArticlesGridFragment.newInstance(category);
		}

		@Override
		public int getCount() {
			return categories.length;
		}

	}
}
