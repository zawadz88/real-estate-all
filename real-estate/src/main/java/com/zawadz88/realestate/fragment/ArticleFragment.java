package com.zawadz88.realestate.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.api.model.ArticleEssential;

/**
 * Created by Piotr on 28.12.13.
 */
public class ArticleFragment extends Fragment {

    public static final String ARTICLE_ESSENTIAL = "articleEssential";

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
        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(articleEssential.getTitle());
        return view;
    }
}
