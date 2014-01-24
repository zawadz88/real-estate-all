package com.zawadz88.realestate.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.zawadz88.realestate.ArticleActivity;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.model.ArticleCategory;
import com.zawadz88.realestate.model.Section;
import de.greenrobot.event.EventBus;

/**
 * Fragment displaying lists of articles in a {@link android.support.v4.view.ViewPager}
 *
 * @author Piotr Zawadzki
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
        setRetainInstance(false);
        mArticlesPager.setSaveEnabled(false);
		mArticlesPager.setAdapter(new ArticlesGridPagerAdapter(getActivity().getApplicationContext(), getChildFragmentManager()));

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.article_tabs);
		tabs.setViewPager(mArticlesPager);

		return view;
	}

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

	/**
	 * Method called when an {@link com.zawadz88.realestate.fragment.ArticlesGridFragment.ArticleItemSelectedEvent}
	 * is posted from EventBus.
	 * @param ev posted event
	 */
    public void onEventMainThread(ArticlesGridFragment.ArticleItemSelectedEvent ev) {
        Intent intent = new Intent(ArticlesSectionFragment.this.getActivity(), ArticleActivity.class);
        intent.putExtra(ArticlesGridFragment.EXTRA_POSITION_TAG, ev.getPosition());
        intent.putExtra(ArticlesGridFragment.EXTRA_CATEGORY_TAG, ev.getCategory());
        startActivity(intent);
    }

	/**
	 * A {@link android.support.v4.view.ViewPager} adapter containing all possible article categories.
	 */
    private static class ArticlesGridPagerAdapter extends FragmentPagerAdapter {

		private Context context;

		private ArticleCategory[] categories = ArticleCategory.values();

		public ArticlesGridPagerAdapter(Context context, FragmentManager fm) {
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
