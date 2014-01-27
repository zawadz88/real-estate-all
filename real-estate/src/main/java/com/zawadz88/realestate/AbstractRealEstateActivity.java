package com.zawadz88.realestate;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import com.zawadz88.realestate.model.ArticleCategory;
import com.zawadz88.realestate.service.ContentHolder;
import com.zawadz88.realestate.service.DownloadTaskResultDelegate;

/**
 * A base class to be used for all activities
 * @author Piotr Zawadzki
 */
public class AbstractRealEstateActivity extends ActionBarActivity {

    private static final String CONTENT_HOLDER_TAG = "contentHolder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(CONTENT_HOLDER_TAG) && getRealEstateApplication().isNewlyCreated()) {
            ContentHolder contentHolder = (ContentHolder) savedInstanceState.getSerializable(CONTENT_HOLDER_TAG);
            if (contentHolder != null) {
                getRealEstateApplication().setContentHolder(contentHolder);
                getRealEstateApplication().setTaskResultDelegate(new DownloadTaskResultDelegate(contentHolder));
            }
        }
        getRealEstateApplication().setNewlyCreated(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CONTENT_HOLDER_TAG, getRealEstateApplication().getContentHolder());
        super.onSaveInstanceState(outState);
    }

    public RealEstateApplication getRealEstateApplication() {
        return (RealEstateApplication) getApplicationContext();
    }
}
