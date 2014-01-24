package com.zawadz88.realestate.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zawadz88.realestate.ArticleActivity;
import com.zawadz88.realestate.R;
import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.event.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.model.ArticleCategory;
import com.zawadz88.realestate.model.ArticleEssential;
import com.zawadz88.realestate.task.ArticleListDownloadTask;
import com.zawadz88.realestate.util.DeviceUtils;
import de.greenrobot.event.EventBus;

import java.util.List;

/**
 * Fragment displaying a list of articles
 *
 * @author Piotr Zawadzki
 */
public class ArticlesGridFragment extends AbstractGridFragment implements AbsListView.OnScrollListener {

    public static final String EXTRA_POSITION_TAG = "position";
    public static final String EXTRA_CATEGORY_TAG = "category";

    private static final String ARTICLE_CATEGORY_TAG = "articleCategory";
    private static final String NUM_COLUMNS_TAG = "numColumns";
	private static final String LAST_KNOWN_SCROLL_POSITION = "scrollPosition";
	private static final int VISIBLE_ITEM_THRESHOLD = 3;

	private View mLoadingMoreView;

	private ArticleCategory mCategory;
    private int mLastKnownScrollPosition;

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
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_default_grid, container, false);

		mGridView = (GridView) view.findViewById(R.id.ads_gridview);
		mLoadingMoreView = view.findViewById(R.id.loading_more_view);
        mLoadingView = view.findViewById(R.id.list_loading);
        mEmptyView = view.findViewById(R.id.list_empty);
        mNoInternetLayout = (LinearLayout) view.findViewById(R.id.no_internet_layout);
        mNoInternetLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(DeviceUtils.isOnline(mApplication)) {
                    downloadFirstPage();
                }
            }
        });

        if (getArguments().getInt(NUM_COLUMNS_TAG) > 0) {
            mGridView.setNumColumns(getArguments().getInt(NUM_COLUMNS_TAG));
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                mLastKnownScrollPosition = position;
                EventBus.getDefault().post(new ArticleItemSelectedEvent(mCategory, position));
            }
        });

		return view;
	}

    @Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        mLastKnownScrollPosition  = 0;
		if (savedInstanceState != null && savedInstanceState.containsKey(LAST_KNOWN_SCROLL_POSITION)) {
			mLastKnownScrollPosition = savedInstanceState.getInt(LAST_KNOWN_SCROLL_POSITION);;
		} else if (getArguments() != null) {
            mLastKnownScrollPosition = getArguments().getInt(LAST_KNOWN_SCROLL_POSITION);
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
    public void onStart() {
        super.onStart();
        List<ArticleEssential> articles = getContentHolder().getArticleEssentialListByCategory(mCategory.getName());

        if (articles.isEmpty()) {
            if (getContentHolder().getEndOfItemsReachedFlagForCategory(mCategory.getName())) {
                setGridViewState(ViewState.EMPTY);
            } else {
                downloadFirstPage();
            }
        } else {
            setGridViewState(ViewState.CONTENT);
            mGridView.setAdapter(new ArticlesAdapter());
            scrollToPosition(mLastKnownScrollPosition, false);
            mGridView.setOnScrollListener(this);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!getContentHolder().getLoadingMoreFlagForCategory(mCategory.getName()) && visibleItemCount != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_ITEM_THRESHOLD) && !getContentHolder().getEndOfItemsReachedFlagForCategory(mCategory.getName())) {
			// visibleItemCount != totalItemCount is needed for when new items are added this is false
            if (mApplication.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX + this.mCategory.getName())) {
                getContentHolder().setLoadingMoreFlagForCategory(true, mCategory.getName());
            } else if (DeviceUtils.isOnline(mApplication)) { //load next page
                getContentHolder().setLoadingMoreFlagForCategory(true, mCategory.getName());
                ArticleListDownloadTask downloadTask = new ArticleListDownloadTask(this.mCategory.getName(), getContentHolder().getCurrentlyLoadedPageNumberForCategory(mCategory.getName()) + 1);
				mApplication.startTask(downloadTask);
			} else {
                showNoInternetToastMessage();
            }
		}

		if (firstVisibleItem == 0 && visibleItemCount == 0 && totalItemCount == 0) {
			return;
		}
        // show loading view if last cell is visible and there should be more items available
		if (firstVisibleItem + visibleItemCount == totalItemCount && !getContentHolder().getEndOfItemsReachedFlagForCategory(mCategory.getName())) {
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

	/**
	 * Method called when an {@link com.zawadz88.realestate.event.ArticleEssentialDownloadEvent}
	 * is posted from EventBus.
	 * @param ev posted event
	 */
	public void onEventMainThread(ArticleEssentialDownloadEvent ev) {
        getContentHolder().setLoadingMoreFlagForCategory(false, mCategory.getName());
        if (mLoadingMoreView.getVisibility() == View.VISIBLE) {
            mLoadingMoreView.setVisibility(View.GONE);
        }
        if (ev.getCategoryName().equals(mCategory.getName())) {
            List<ArticleEssential> articles = getContentHolder().getArticleEssentialListByCategory(mCategory.getName());
            if (ev.isSuccessful()) {
                List<ArticleEssential> downloadedArticles = ev.getDownloadedArticles();
                if (downloadedArticles == null || downloadedArticles.isEmpty()) {
                    getContentHolder().setEndOfItemsReachedFlagForCategory(true, mCategory.getName());
                    if (articles.isEmpty()) {
                        setGridViewState(ViewState.EMPTY);
                    } else {
                        //do nothing, list's data set was not changed
                    }
                } else {
                    setGridViewState(ViewState.CONTENT);
                    if (mGridView.getAdapter() == null) {
                        mGridView.setAdapter(new ArticlesAdapter());
                        mGridView.setOnScrollListener(this);
                    } else {
                        ((BaseAdapter) mGridView.getAdapter()).notifyDataSetChanged();
                    }
                }
            } else {
                if (articles == null || articles.isEmpty()) {
                    setGridViewState(ViewState.NO_INTERNET);
                } else {
                    showNoInternetToastMessage();
                }
            }
        }
	}

	/**
	 * Method called when an {@link com.zawadz88.realestate.ArticleActivity.ArticleSwipedEvent}
	 * is posted from EventBus.
	 * @param ev posted event
	 */
    public void onEventMainThread(ArticleActivity.ArticleSwipedEvent ev) {
        scrollToPosition(ev.getNewPosition(), true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void scrollToPosition(final int position, final boolean animated) {
        ListAdapter gridAdapter = mGridView.getAdapter();
        if (gridAdapter != null) {
            if (gridAdapter.getCount() > 0 && position < gridAdapter.getCount()) {
                if (DeviceUtils.isEqualOrHigherThanHoneycomb() && animated) {
                    mGridView.smoothScrollToPositionFromTop(position, 0);
                } else {
                    mGridView.setSelection(position);
                }
            }
        }
    }

	/**
	 * Download first page of article essentials for this fragment's {@link com.zawadz88.realestate.model.ArticleCategory}
	 */
    private void downloadFirstPage() {
        setGridViewState(ViewState.LOADING);
        ArticleListDownloadTask downloadTask = new ArticleListDownloadTask(this.mCategory.getName(), 0);
        mApplication.startTask(downloadTask);
    }

	/**
	 * A {@link android.widget.GridView} adapter managing a list of article essentials
	 */
	private class ArticlesAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return getContentHolder().getArticleEssentialListByCategory(mCategory.getName()).size();
		}

		@Override
		public Object getItem(final int position) {
			return getContentHolder().getArticleEssentialListByCategory(mCategory.getName()).get(position);
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
			ArticleEssential articleEssentialItem = getContentHolder().getArticleEssentialListByCategory(mCategory.getName()).get(position);
			((TextView)view.findViewById(R.id.gridview_item_title)).setText(articleEssentialItem.getTitle());


			final ImageView imageView = (ImageView)view.findViewById(R.id.gridview_item_image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

			Picasso.with(getActivity())
					.load(articleEssentialItem.getImageUrl())
					.placeholder(R.drawable.robot_icon)
					.error(R.drawable.robot_icon)
                    .fit()
					.into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }

                        @Override
                        public void onError() {

                        }
                    });
			return view;
		}
	}

	/**
	 * An EventBus event posted when a {@link android.widget.GridView} item was selected.
	 */
    public static final class ArticleItemSelectedEvent {

		/**
		 * Fragment's category
		 */
        private ArticleCategory category;

		/**
		 * Position selected
		 */
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
