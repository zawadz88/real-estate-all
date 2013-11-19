package com.zawadz88.realestate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.eventbus.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.api.model.ArticleCategory;
import com.zawadz88.realestate.api.model.ArticleEssential;
import com.zawadz88.realestate.api.task.ArticleListDownloadTask;
import de.greenrobot.event.EventBus;

import java.util.List;

/**
 * Created: 04.11.13
 *
 * @author Zawada
 */
public class ArticlesGridFragment extends AbstractListFragment implements AbsListView.OnScrollListener {

	private static final String ARTICLE_CATEGORY_TAG = "articleCategory";
	private static final String LAST_KNOWN_SCROLL_POSITION = "scrollPosition";
	private static final int VISIBLE_ITEM_THRESHOLD = 3;

	private GridView mArticlesGridView;
	private View mLoadingMoreView;

	private ArticleCategory mCategory;

	public static ArticlesGridFragment newInstance(final ArticleCategory category) {
		ArticlesGridFragment fragment = new ArticlesGridFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARTICLE_CATEGORY_TAG, category);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mCategory = (ArticleCategory) getArguments().getSerializable(ARTICLE_CATEGORY_TAG);
		EventBus.getDefault().register(this);
		//TODO remove
		Picasso.with(getActivity()).setDebugging(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_default_grid, container, false);

		mArticlesGridView = (GridView) view.findViewById(R.id.ads_gridview);
		mLoadingMoreView = view.findViewById(R.id.loading_more_view);

		List<ArticleEssential> articles = this.mApplication.getArticleEssentialListByCategory(mCategory.getName());

		if (articles.isEmpty()) {
			//TODO show loading view
			if (mApplication.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX + this.mCategory.getName())) {
				//wait for notification
			} else {
				ArticleListDownloadTask downloadTask = new ArticleListDownloadTask(this.mCategory.getName(), 0);
				mApplication.startTask(downloadTask);
			}
		} else {
			mArticlesGridView.setAdapter(new AdsAdapter());
			mArticlesGridView.setOnScrollListener(this);
		}

		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(LAST_KNOWN_SCROLL_POSITION)) {
			int position = savedInstanceState.getInt(LAST_KNOWN_SCROLL_POSITION);
			mArticlesGridView.smoothScrollToPosition(position);
		}
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mArticlesGridView != null) {
			int position = mArticlesGridView.getFirstVisiblePosition();
			outState.putInt(LAST_KNOWN_SCROLL_POSITION, position);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (!loadingMoreItems && visibleItemCount != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_ITEM_THRESHOLD) && !endOfItemsReached && connectionAvailable) {
			// visibleItemCount != totalItemCount is needed for when new items are added this is false
			loadingMoreItems = true;
			loading = true;
			//refreshList(); // needed to show loading view
			//TODO show loading view
			if (mApplication.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX + this.mCategory.getName())) {
				//wait for notification
			} else { //load next page
				ArticleListDownloadTask downloadTask = new ArticleListDownloadTask(this.mCategory.getName(), mApplication.getCurrentlyLoadedPageNumberForCategory(mCategory.getName()) + 1);
				mApplication.startTask(downloadTask);
			}
		}

		if (firstVisibleItem == 0 && visibleItemCount == 0 && totalItemCount == 0) {
			return;
		}

		if (firstVisibleItem + visibleItemCount == totalItemCount) {
			if (mLoadingMoreView.getVisibility() == View.GONE) {
				mLoadingMoreView.setVisibility(View.VISIBLE);
			}
		} else {
			if (mLoadingMoreView.getVisibility() == View.VISIBLE) {
				mLoadingMoreView.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	public void onEventMainThread(ArticleEssentialDownloadEvent ev) {
		loadingMoreItems = false;
		loading = false;
		if (ev.isSuccessful() && ev.getCategoryName().equals(mCategory.getName())) {
			List<ArticleEssential> articles = this.mApplication.getArticleEssentialListByCategory(mCategory.getName());

			if (articles.isEmpty()) {
				endOfItemsReached = true;
				//TODO show empty view, no articles available
			} else {
				//TODO refresh list
				if (mArticlesGridView.getAdapter() == null) {
					mArticlesGridView.setAdapter(new AdsAdapter());
					mArticlesGridView.setOnScrollListener(this);
				} else {
					((BaseAdapter) mArticlesGridView.getAdapter()).notifyDataSetChanged();
				}

			}
		} else {
			//TODO depending on number of items previously fetched show error message either in footer or in place of GridView
		}
	}

	private class AdsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mApplication.getArticleEssentialListByCategory(mCategory.getName()).size();
		}

		@Override
		public Object getItem(final int position) {
			return mApplication.getArticleEssentialListByCategory(mCategory.getName()).get(position);
		}

		@Override
		public long getItemId(final int position) {
			return 0;
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			View view;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				LayoutInflater inflater = (LayoutInflater) ArticlesGridFragment.this.getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.section_gridview_item, parent, false);
			} else {
				view = convertView;
			}
			ArticleEssential articleEssentialItem = mApplication.getArticleEssentialListByCategory(mCategory.getName()).get(position);
			((TextView)view.findViewById(R.id.gridview_item_title)).setText(articleEssentialItem.getTitle());


			ImageView imageView = (ImageView)view.findViewById(R.id.gridview_item_image);

			Picasso.with(getActivity())
					.load(articleEssentialItem.getImageUrl())
					.placeholder(R.drawable.sample3)
					.error(R.drawable.sample2)
					.into(imageView);
			return view;
		}
	}
}
