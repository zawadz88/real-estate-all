package com.zawadz88.realestate.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.zawadz88.realestate.R;

/**
 * Base class for fragments containing a {@link android.widget.GridView}
 *
 * @author Piotr Zawadzki
 */
public class AbstractGridFragment extends AbstractFragment {

    protected static final long MINIMUM_TOAST_INTERVAL = 10000L;

    protected GridView mGridView;
    protected View mLoadingView;
    protected View mEmptyView;
    protected LinearLayout mNoInternetLayout;

    protected String mNetworkErrorTitle;
    protected long mLastToastTimeStamp;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNetworkErrorTitle = getString(R.string.network_error_title);
    }

    protected void setGridViewState(final ViewState newState) {
        switch (newState) {
            case LOADING:
                mGridView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                mNoInternetLayout.setVisibility(View.GONE);
                break;
            case CONTENT:
                mGridView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mNoInternetLayout.setVisibility(View.GONE);
                break;
            case EMPTY:
                mGridView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.GONE);
                mNoInternetLayout.setVisibility(View.GONE);
                break;
            case NO_INTERNET:
                mGridView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mNoInternetLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    protected void showNoInternetToastMessage() {
        if (System.currentTimeMillis() > mLastToastTimeStamp + MINIMUM_TOAST_INTERVAL) {
            mLastToastTimeStamp = System.currentTimeMillis();
            Toast.makeText(mApplication, mNetworkErrorTitle, Toast.LENGTH_SHORT).show();
        }
    }

}
