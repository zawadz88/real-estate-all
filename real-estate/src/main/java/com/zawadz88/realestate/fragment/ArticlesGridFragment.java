package com.zawadz88.realestate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.zawadz88.realestate.ArticleActivity;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.eventbus.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.api.model.ArticleCategory;
import com.zawadz88.realestate.api.model.ArticleEssential;
import com.zawadz88.realestate.api.task.ArticleListDownloadTask;
import com.zawadz88.realestate.util.DeviceUtils;
import de.greenrobot.event.EventBus;

import java.util.List;

/**
 * Created: 04.11.13
 *
 * @author Zawada
 */
public class ArticlesGridFragment extends AbstractListFragment implements AbsListView.OnScrollListener {

    public static final String EXTRA_POSITION_TAG = "position";
    public static final String EXTRA_CATEGORY_TAG = "category";

    private static final String ARTICLE_CATEGORY_TAG = "articleCategory";
    private static final String NUM_COLUMNS_TAG = "numColumns";
	private static final String LAST_KNOWN_SCROLL_POSITION = "scrollPosition";
	private static final int VISIBLE_ITEM_THRESHOLD = 3;

	private View mLoadingMoreView;

	private ArticleCategory mCategory;

    public static ArticlesGridFragment newInstance(final ArticleCategory category) {
        ArticlesGridFragment fragment = new ArticlesGridFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTICLE_CATEGORY_TAG, category);
        fragment.setArguments(args);
        return fragment;
    }

    public static ArticlesGridFragment newInstance(final ArticleCategory category, final int numColumns, final int startPosition) {
        ArticlesGridFragment fragment = new ArticlesGridFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARTICLE_CATEGORY_TAG, category);
        args.putInt(NUM_COLUMNS_TAG, numColumns);
        args.putInt(LAST_KNOWN_SCROLL_POSITION, startPosition);
        fragment.setArguments(args);
        return fragment;
    }

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mCategory = (ArticleCategory) getArguments().getSerializable(ARTICLE_CATEGORY_TAG);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_default_grid, container, false);

		mGridView = (GridView) view.findViewById(R.id.ads_gridview);
		mLoadingMoreView = view.findViewById(R.id.loading_more_view);
        mLoadingView = view.findViewById(R.id.list_loading);
        mEmptyView = view.findViewById(R.id.list_empty);
        mNoInternetLayout = (LinearLayout) view.findViewById(R.id.noInternetLayout);
        mNoInternetLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(DeviceUtils.isOnline(mApplication)) {
                    setGridViewState(GridViewState.LOADING);
                    ArticleListDownloadTask downloadTask = new ArticleListDownloadTask(ArticlesGridFragment.this.mCategory.getName(), 0);
                    mApplication.startTask(downloadTask);
                }
            }
        });

        if (getArguments().getInt(NUM_COLUMNS_TAG) > 0) {
            mGridView.setNumColumns(getArguments().getInt(NUM_COLUMNS_TAG));
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                EventBus.getDefault().post(new ArticleItemSelectedEvent(mCategory, position));
            }
        });

        List<ArticleEssential> articles = this.mApplication.getArticleEssentialListByCategory(mCategory.getName());

		if (articles.isEmpty()) {
            if (mApplication.getEndOfItemsReachedFlagForCategory(mCategory.getName())) {
                setGridViewState(GridViewState.EMPTY);
            } else {
                setGridViewState(GridViewState.LOADING);
                if (mApplication.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX + this.mCategory.getName())) {
                    //wait for notification
                } else {
                    ArticleListDownloadTask downloadTask = new ArticleListDownloadTask(this.mCategory.getName(), 0);
                    mApplication.startTask(downloadTask);
                }
            }
		} else {
            setGridViewState(GridViewState.CONTENT);
			mGridView.setAdapter(new ArticlesAdapter());
			mGridView.setOnScrollListener(this);
		}

		return view;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(LAST_KNOWN_SCROLL_POSITION)) {
			int position = savedInstanceState.getInt(LAST_KNOWN_SCROLL_POSITION);
			mGridView.smoothScrollToPosition(position);
		} else if (getArguments() != null) {
            mGridView.smoothScrollToPosition(getArguments().getInt(LAST_KNOWN_SCROLL_POSITION));
        }
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mGridView != null) {
			int position = mGridView.getFirstVisiblePosition();
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
        if (!mApplication.getLoadingMoreFlagForCategory(mCategory.getName()) && visibleItemCount != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_ITEM_THRESHOLD) && !mApplication.getEndOfItemsReachedFlagForCategory(mCategory.getName())) {
			// visibleItemCount != totalItemCount is needed for when new items are added this is false
            if (mApplication.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX + this.mCategory.getName())) {
                mApplication.setLoadingMoreFlagForCategory(true, mCategory.getName());
            } else if (DeviceUtils.isOnline(mApplication)) { //load next page
                mApplication.setLoadingMoreFlagForCategory(true, mCategory.getName());
                ArticleListDownloadTask downloadTask = new ArticleListDownloadTask(this.mCategory.getName(), mApplication.getCurrentlyLoadedPageNumberForCategory(mCategory.getName()) + 1);
				mApplication.startTask(downloadTask);
			} else {
                showNoInternetToastMessage();
            }
		}

		if (firstVisibleItem == 0 && visibleItemCount == 0 && totalItemCount == 0) {
			return;
		}
        // show loading view if last cell is visible and there should be more items available
		if (firstVisibleItem + visibleItemCount == totalItemCount && !mApplication.getEndOfItemsReachedFlagForCategory(mCategory.getName())) {
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
        mApplication.setLoadingMoreFlagForCategory(false, mCategory.getName());
        if (mLoadingMoreView.getVisibility() == View.VISIBLE) {
            mLoadingMoreView.setVisibility(View.GONE);
        }
        if (ev.getCategoryName().equals(mCategory.getName())) {
            List<ArticleEssential> articles = this.mApplication.getArticleEssentialListByCategory(mCategory.getName());
            if (ev.isSuccessful()) {
                List<ArticleEssential> downloadedArticles = ev.getDownloadedArticles();
                if (downloadedArticles == null || downloadedArticles.isEmpty()) {
                    mApplication.setEndOfItemsReachedFlagForCategory(true, mCategory.getName());
                    if (articles.isEmpty()) {
                        setGridViewState(GridViewState.EMPTY);
                    } else {
                        //do nothing, list's data set was not changed
                    }
                } else {
                    setGridViewState(GridViewState.CONTENT);
                    if (mGridView.getAdapter() == null) {
                        mGridView.setAdapter(new ArticlesAdapter());
                        mGridView.setOnScrollListener(this);
                    } else {
                        ((BaseAdapter) mGridView.getAdapter()).notifyDataSetChanged();
                    }
                }
            } else {
                if (articles == null || articles.isEmpty()) {
                    setGridViewState(GridViewState.NO_INTERNET);
                } else {
                    showNoInternetToastMessage();
                }
            }
        }
	}

    public void onEventMainThread(ArticleActivity.ArticleSwipedEvent ev) {
        mGridView.smoothScrollToPosition(ev.getNewPosition());
    }

	private class ArticlesAdapter extends BaseAdapter {

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

    public static final class ArticleItemSelectedEvent {

        private ArticleCategory category;
        private int position;

        public ArticleItemSelectedEvent(ArticleCategory category, int position) {
            this.category = category;
            this.position = position;
        }

        public ArticleCategory getCategory() {
            return category;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public String toString() {
            return "ArticleItemSelectedEvent{" +
                    "category=" + category +
                    ", position=" + position +
                    '}';
        }
    }
}
