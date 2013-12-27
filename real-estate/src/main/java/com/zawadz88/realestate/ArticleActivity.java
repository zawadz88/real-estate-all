package com.zawadz88.realestate;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import com.zawadz88.realestate.api.model.ArticleCategory;
import com.zawadz88.realestate.fragment.ArticlesGridFragment;

/**
 * Created by Piotr on 27.12.13.
 */
public class ArticleActivity extends ActionBarActivity {

    private int mCurrentPosition;
    private ArticleCategory mCategory;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        if (getIntent().getExtras() == null || getIntent().getSerializableExtra(ArticlesGridFragment.EXTRA_CATEGORY_TAG) == null) {
            finish();
            return;
        }
        mCategory = (ArticleCategory) getIntent().getSerializableExtra(ArticlesGridFragment.EXTRA_CATEGORY_TAG);
        mCurrentPosition = getIntent().getIntExtra(ArticlesGridFragment.EXTRA_POSITION_TAG, 0);

        if (findViewById(R.id.article_list_container) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ArticlesGridFragment articlesGridFragment = ArticlesGridFragment.newInstance(mCategory, 1);
            fragmentManager.beginTransaction()
                    .replace(R.id.article_list_container, articlesGridFragment, null)
                    .commit();
        }

    }
}