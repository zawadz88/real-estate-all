package com.zawadz88.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import com.zawadz88.realestate.event.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.fragment.ArticleFragment;
import com.zawadz88.realestate.fragment.ArticlesGridFragment;
import com.zawadz88.realestate.model.ArticleCategory;
import com.zawadz88.realestate.model.ArticleEssential;
import com.zawadz88.realestate.service.ContentHolder;
import com.zawadz88.realestate.task.ArticleListDownloadTask;
import com.zawadz88.realestate.util.DeviceUtils;
import de.greenrobot.event.EventBus;

import java.util.List;

/**
 * Activity displaying an {@link com.zawadz88.realestate.model.Article}.
 * It shows how to create an activity with a view pager containing a list of items and with a fragment that has the same list
 * and how to glue them together with each other and also load content.
 *
 * @author Piotr Zawadzki
 */
public class ArticleActivity extends AbstractRealEstateActivity implements ViewPager.OnPageChangeListener {

    private static final String PAGE_ITEM_TAG = "pageItem";
    private static final int LOAD_MORE_ITEMS_THRESHOLD = 3;

    private ViewPager mArticleViewPager;

    /**
     * Position of the article in the {@link android.support.v4.view.ViewPager}
     */
    private int mCurrentPosition;

    /**
     * Category of the article
     */
    private ArticleCategory mCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.activity_article);
        if (getIntent().getExtras() == null || getIntent().getSerializableExtra(ArticlesGridFragment.EXTRA_CATEGORY_TAG) == null) {
            finish();
            return;
        }
        mCategory = (ArticleCategory) getIntent().getSerializableExtra(ArticlesGridFragment.EXTRA_CATEGORY_TAG);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PAGE_ITEM_TAG, 0);
        } else {
            mCurrentPosition = getIntent().getIntExtra(ArticlesGridFragment.EXTRA_POSITION_TAG, 0);
        }

		initActionBar();

        if (findViewById(R.id.article_list_container) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ArticlesGridFragment articlesGridFragment = ArticlesGridFragment.newInstance(mCategory, 1, mCurrentPosition);
            fragmentManager.beginTransaction()
                    .replace(R.id.article_list_container, articlesGridFragment, null)
                    .commit();
        }

        mArticleViewPager = (ViewPager) findViewById(R.id.articles_viewpager);
        mArticleViewPager.setSaveEnabled(false);
        List<ArticleEssential> articleEssentialList = getContentHolder().getArticleEssentialListByCategory(mCategory.getName());
        mArticleViewPager.setAdapter(new ArticlesPagerAdapter(articleEssentialList, getSupportFragmentManager()));
        if (mCurrentPosition >=0 && mCurrentPosition < articleEssentialList.size()) {
            mArticleViewPager.setCurrentItem(mCurrentPosition);
        }
        mArticleViewPager.setOnPageChangeListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_ITEM_TAG, mArticleViewPager.getCurrentItem());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
        supportInvalidateOptionsMenu();
        int count = mArticleViewPager.getAdapter().getCount();
        if (i > count - LOAD_MORE_ITEMS_THRESHOLD && !getContentHolder().getEndOfItemsReachedFlagForCategory(mCategory.getName()) && !getContentHolder().getLoadingMoreFlagForCategory(mCategory.getName())) {
            if (getRealEstateApplication().isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX + this.mCategory.getName())) {
                getContentHolder().setLoadingMoreFlagForCategory(true, mCategory.getName());
            } else if (DeviceUtils.isOnline(getApplicationContext())) { //load next page
                getContentHolder().setLoadingMoreFlagForCategory(true, mCategory.getName());
                ArticleListDownloadTask downloadTask = new ArticleListDownloadTask(this.mCategory.getName(), getContentHolder().getCurrentlyLoadedPageNumberForCategory(mCategory.getName()) + 1);
                getRealEstateApplication().startTask(downloadTask);
            } else {
                //TODO show a toast, without duplicating a toast from gridview for tablets in landscape
                //showNoInternetToastMessage();
            }
        }
        EventBus.getDefault().post(new ArticleSwipedEvent(i));
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(mCategory.getTitleResource());
	}

	public ContentHolder getContentHolder() {
		return getRealEstateApplication().getContentHolder();
	}

	/**
	 * Method called when an {@link com.zawadz88.realestate.fragment.ArticlesGridFragment.ArticleItemSelectedEvent}
	 * is posted from EventBus.
	 * @param ev posted event
	 */
	public void onEventMainThread(ArticlesGridFragment.ArticleItemSelectedEvent ev) {
		if (ev.getCategory().getName().equals(mCategory.getName())) {
			int position = ev.getPosition();
			if (position < mArticleViewPager.getAdapter().getCount()) {
				mArticleViewPager.setCurrentItem(position, true);
			}
		}
	}

	/**
	 * Method called when an {@link com.zawadz88.realestate.event.ArticleEssentialDownloadEvent}
	 * is posted from EventBus.
	 * @param ev posted event
	 */
    public void onEventMainThread(ArticleEssentialDownloadEvent ev) {
        getContentHolder().setLoadingMoreFlagForCategory(false, mCategory.getName());
        if (ev.getCategoryName().equals(mCategory.getName())) {
            if (ev.isSuccessful()) {
                List<ArticleEssential> downloadedArticles = ev.getDownloadedArticles();
                if (downloadedArticles == null || downloadedArticles.isEmpty()) {
					getContentHolder().setEndOfItemsReachedFlagForCategory(true, mCategory.getName());
                } else {
                    mArticleViewPager.getAdapter().notifyDataSetChanged();
                }
            } else {
                //TODO show some message?
                    //showNoInternetToastMessage();
            }
        }
    }

    /**
     * A {@link android.support.v4.view.ViewPager} adapter managing a list of articles
     */
    private static class ArticlesPagerAdapter extends FragmentStatePagerAdapter {

        private List<ArticleEssential> mArticleEssentialList;

        public ArticlesPagerAdapter(List<ArticleEssential> articleEssentialList, FragmentManager fm) {
            super(fm);
            this.mArticleEssentialList = articleEssentialList;
        }

        @Override
        public Fragment getItem(int index) {
            return ArticleFragment.newInstance(mArticleEssentialList.get(index));
        }

        @Override
        public int getCount() {
            return mArticleEssentialList.size();
        }

    }

    /**
     * EventBus event posted {@link android.support.v4.view.ViewPager} swiped to a different page
     */
    public static final class ArticleSwipedEvent {

        /**
         * Position to which view pager swiped
         */
        private final int newPosition;

        public ArticleSwipedEvent(int position) {
            this.newPosition = position;
        }

        public int getNewPosition() {
            return newPosition;
        }

        @Override
        public String toString() {
            return "ArticleSwipedEvent{" +
                    "newPosition=" + newPosition +
                    '}';
        }
    }

}