package com.zawadz88.realestate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.eventbus.ArticleDownloadEvent;
import com.zawadz88.realestate.api.model.Article;
import com.zawadz88.realestate.api.model.ArticleEssential;
import com.zawadz88.realestate.api.task.ArticleDownloadTask;
import de.greenrobot.event.EventBus;

/**
 * Created by Piotr on 28.12.13.
 */
public class ArticleFragment extends AbstractFragment {

    public static final String ARTICLE_ESSENTIAL = "articleEssential";
    private TextView mTitleView;
    private TextView mContentView;

    public static ArticleFragment newInstance(final ArticleEssential articleEssential) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTICLE_ESSENTIAL, articleEssential);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_article, container, false);
        final ArticleEssential articleEssential = (ArticleEssential) getArguments().getSerializable(ARTICLE_ESSENTIAL);
        mTitleView = (TextView) view.findViewById(R.id.title);
        mContentView = (TextView) view.findViewById(R.id.content);
        mTitleView.setText(articleEssential.getTitle());
        Article article = mApplication.getArticleById(articleEssential.getArticleId());
        if (article != null) {
            displayArticle(article);
        } else if (mApplication.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_TAG_PREFIX + articleEssential.getArticleId())) {
            //TODO show loading view
        } else {
            //TODO show loading view
            ArticleDownloadTask downloadTask = new ArticleDownloadTask(articleEssential);
            mApplication.startTask(downloadTask);
        }
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

    public void onEventMainThread(ArticleDownloadEvent ev) {
        final ArticleEssential articleEssential = (ArticleEssential) getArguments().getSerializable(ARTICLE_ESSENTIAL);

        if (ev.getArticleId() == articleEssential.getArticleId()) {
            if (ev.isSuccessful()) {
                Article article = ev.getDownloadedArticle();
                displayArticle(article);
            } else {
                //TODO show no internet view
            }
        }
    }

    private void displayArticle(Article article) {
        mContentView.setText(article.getContent());
    }
}
