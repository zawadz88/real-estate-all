package com.zawadz88.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import com.zawadz88.realestate.api.model.ArticleCategory;
import com.zawadz88.realestate.api.model.ArticleEssential;
import com.zawadz88.realestate.fragment.ArticleFragment;
import com.zawadz88.realestate.fragment.ArticlesGridFragment;
import de.greenrobot.event.EventBus;

import java.util.List;

/**
 * Created by Piotr on 27.12.13.
 */
public class ArticleActivity extends ActionBarActivity {

    private ViewPager mArticleViewPager;

    private int mCurrentPosition;
    private ArticleCategory mCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        if (getIntent().getExtras() == null || getIntent().getSerializableExtra(ArticlesGridFragment.EXTRA_CATEGORY_TAG) == null) {
            finish();
            return;
        }
        mCategory = (ArticleCategory) getIntent().getSerializableExtra(ArticlesGridFragment.EXTRA_CATEGORY_TAG);
        mCurrentPosition = getIntent().getIntExtra(ArticlesGridFragment.EXTRA_POSITION_TAG, 0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(mCategory.getTitleResource());

        if (findViewById(R.id.article_list_container) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ArticlesGridFragment articlesGridFragment = ArticlesGridFragment.newInstance(mCategory, 1);
            fragmentManager.beginTransaction()
                    .replace(R.id.article_list_container, articlesGridFragment, null)
                    .commit();
        }

        mArticleViewPager = (ViewPager) findViewById(R.id.articles_viewpager);
        List<ArticleEssential> articleEssentialList = ((RealEstateApplication)getApplication()).getArticleEssentialListByCategory(mCategory.getName());
        mArticleViewPager.setAdapter(new ArticlesPagerAdapter(articleEssentialList, getSupportFragmentManager()));
        if (mCurrentPosition >=0 && mCurrentPosition < articleEssentialList.size()) {
            mArticleViewPager.setCurrentItem(mCurrentPosition);
        }
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

    public void onEventMainThread(ArticlesGridFragment.ArticleItemSelectedEvent ev) {
        //TODO do stuff...
        System.out.println("event: " + ev);
    }

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

}