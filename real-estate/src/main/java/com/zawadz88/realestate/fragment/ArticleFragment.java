package com.zawadz88.realestate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.*;
import android.widget.TextView;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.api.eventbus.ArticleDownloadEvent;
import com.zawadz88.realestate.api.model.Article;
import com.zawadz88.realestate.api.model.ArticleEssential;
import com.zawadz88.realestate.api.task.ArticleDownloadTask;
import com.zawadz88.realestate.util.DeviceUtils;
import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;

/**
 * Fragment displaying an {@link com.zawadz88.realestate.api.model.Article} fetched from the API
 *
 * @author Piotr Zawadzki
 */
public class ArticleFragment extends AbstractFragment {

    public static final String ARTICLE_ESSENTIAL = "articleEssential";
    private TextView mTitleView;
    private TextView mContentView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoInternetLayout;

    public static ArticleFragment newInstance(final ArticleEssential articleEssential) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTICLE_ESSENTIAL, articleEssential);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_article, container, false);
        final ArticleEssential articleEssential = (ArticleEssential) getArguments().getSerializable(ARTICLE_ESSENTIAL);
        mTitleView = (TextView) view.findViewById(R.id.title);
        mContentView = (TextView) view.findViewById(R.id.content);
        mTitleView.setText(articleEssential.getTitle());
        mErrorView = view.findViewById(R.id.error);
        mLoadingView = view.findViewById(R.id.content_loading);
        mNoInternetLayout = view.findViewById(R.id.no_internet_layout);
        mNoInternetLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(DeviceUtils.isOnline(mApplication)) {
                    downloadArticle(articleEssential);
                }
            }
        });
        Article article = mApplication.getArticleById(articleEssential.getArticleId());
        if (article != null) {
            displayArticle(article);
        } else {
            downloadArticle(articleEssential);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final ArticleEssential articleEssential = (ArticleEssential) getArguments().getSerializable(ARTICLE_ESSENTIAL);
        Article article = mApplication.getArticleById(articleEssential.getArticleId());
        MenuItem actionItem = menu.findItem(R.id.menu_item_share);
        if(article != null) {
            actionItem.setVisible(true);
            ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(actionItem);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, article.getUrl());
            actionProvider.setShareIntent(intent);
        } else {
            actionItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final ArticleEssential articleEssential = (ArticleEssential) getArguments().getSerializable(ARTICLE_ESSENTIAL);
        Article article = mApplication.getArticleById(articleEssential.getArticleId());
        if(article == null) {//do not share if article is not initialized
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onEventMainThread(ArticleDownloadEvent ev) {
        final ArticleEssential articleEssential = (ArticleEssential) getArguments().getSerializable(ARTICLE_ESSENTIAL);

        if (ev.getArticleId() == articleEssential.getArticleId()) {
            if (ev.isSuccessful()) {
                Article article = ev.getDownloadedArticle();
                displayArticle(article);
                if (getActivity() != null) {
                    getActivity().supportInvalidateOptionsMenu();
                }
            } else {
                if (ev.getException() instanceof RetrofitError) {
                    RetrofitError retrofitError = (RetrofitError) ev.getException();
                    if (retrofitError.isNetworkError()) {
                        setArticleState(ViewState.NO_INTERNET);
                    } else {
                        setArticleState(ViewState.ERROR);
                    }
                } else {
                    setArticleState(ViewState.ERROR);
                }
            }
        }
    }

    private void downloadArticle(ArticleEssential articleEssential) {
        setArticleState(ViewState.LOADING);
        ArticleDownloadTask downloadTask = new ArticleDownloadTask(articleEssential);
        mApplication.startTask(downloadTask);
    }

    private void displayArticle(Article article) {
        setArticleState(ViewState.CONTENT);
        mContentView.setText(article.getContent());
    }

    protected void setArticleState(final ViewState newState) {
        switch (newState) {
            case LOADING:
                mContentView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                mNoInternetLayout.setVisibility(View.GONE);
                break;
            case CONTENT:
                mContentView.setVisibility(View.VISIBLE);
                mErrorView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mNoInternetLayout.setVisibility(View.GONE);
                break;
            case ERROR:
                mContentView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.GONE);
                mNoInternetLayout.setVisibility(View.GONE);
                break;
            case NO_INTERNET:
                mContentView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mNoInternetLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
}
