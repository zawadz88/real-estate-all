package com.zawadz88.realestate;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import com.zawadz88.realestate.api.AsyncTaskListener;
import com.zawadz88.realestate.api.DownloadTaskResultDelegate;
import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.api.eventbus.ArticleDownloadEvent;
import com.zawadz88.realestate.api.eventbus.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.api.model.Article;
import com.zawadz88.realestate.api.model.ArticleEssential;
import com.zawadz88.realestate.api.task.BaseDownloadTask;
import com.zawadz88.realestate.api.task.ArticleDownloadTask;
import com.zawadz88.realestate.api.task.ArticleListDownloadTask;
import com.zawadz88.realestate.util.DeviceUtils;
import de.greenrobot.event.EventBus;

import java.util.*;

/**
 *
 * @author Piotr Zawadzki
 */
public class RealEstateApplication extends Application implements AsyncTaskListener {

    public static final String DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX = "ArticleListDownloadTask:";
    public static final String DOWNLOAD_ARTICLE_TAG_PREFIX = "ArticleDownloadTask:";

    /**
     * Map containing currently executing {@link com.zawadz88.realestate.api.task.BaseDownloadTask}s.
     * Tasks are mapped with tags that are unique for a given download
     */
	private Map<String, BaseDownloadTask> mDownloadTasks = (Map<String, BaseDownloadTask>) Collections.synchronizedMap(new HashMap<String, BaseDownloadTask>());

	private DownloadTaskResultDelegate mTaskResultDelegate;

    /**
     * Map of article essential lists grouped by {@link com.zawadz88.realestate.api.model.ArticleCategory} names
     */
	private Map<String, List<ArticleEssential>> mArticleEssentialListsByCategory = new HashMap<String, List<ArticleEssential>>();

    /**
     * Map o {@link com.zawadz88.realestate.api.model.Article}s grouped by article IDs
     */
    private Map<Long, Article> mArticlesByIdMap = new HashMap<Long, Article>();

    /**
     * Map containing numbers of lastly fetched article essential pages for {@link com.zawadz88.realestate.api.model.ArticleCategory} name
     */
    private Map<String, Integer> mArticleEssentialCurrentPageNumbersByCategory = new HashMap<String, Integer>();

    /**
     * Map containing <i>loading more</i> flags for {@link com.zawadz88.realestate.api.model.ArticleCategory} names.
     * A <i>loading more</i> flag is set to {@code true} if the app is loading a next page of articles for a given category.
     */
    private Map<String, Boolean> mArticleEssentialLoadingMoreFlagByCategory = new HashMap<String, Boolean>();

    /**
     * Map containing <i>end of items reached</i> flags for {@link com.zawadz88.realestate.api.model.ArticleCategory} names.
     * A <i>end of items reached</i> flag is set to {@code true} if there is no more items to be fetched for a given category
     */
    private Map<String, Boolean> mArticleEssentialEndOfItemsReachedFlagByCategory = new HashMap<String, Boolean>();

	@Override
	public void onCreate() {
		super.onCreate();
		mTaskResultDelegate = new DownloadTaskResultDelegate(this);
	}

	@Override
	public synchronized void onTaskSuccessful(final BaseDownloadTask task) {
		mDownloadTasks.remove(task.getTag());
		mTaskResultDelegate.onTaskSuccessful(task);
	}

	@Override
	public synchronized void onTaskFailed(final BaseDownloadTask task, final Exception exception) {
		mDownloadTasks.remove(task.getTag());
		mTaskResultDelegate.onTaskFailed(task, exception);
	}

	@Override
	public synchronized void onTaskCancelled(final BaseDownloadTask task) {
		mDownloadTasks.remove(task.getTag());
		mTaskResultDelegate.onTaskCancelled(task);
	}

	public void setTaskResultDelegate(final DownloadTaskResultDelegate taskResultDelegate) {
		this.mTaskResultDelegate = taskResultDelegate;
	}

	@SuppressLint("NewApi")
    public synchronized void startTask(final BaseDownloadTask task) {
		final String tag = task.getTag();
		if (!mDownloadTasks.containsKey(tag)) {
			task.setListener(this);
			mDownloadTasks.put(tag, task);
			if (DeviceUtils.isEqualOrHigherThanHoneycomb()) {
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				task.execute();
			}
		}
	}

    /**
     * Checks if a task with a given task is currently executing
     * @param tag tag associated with a given tag
     * @return {@code true} if task is currently executing
     */
	public synchronized boolean isExecutingTask(final String tag) {
		return mDownloadTasks.containsKey(tag);
	}

    /**
     * Get a list of {@link com.zawadz88.realestate.api.model.ArticleEssential}s for a given category name
     * @param categoryName name of the category
     * @return
     */
	public synchronized List<ArticleEssential> getArticleEssentialListByCategory(final String categoryName) {
		List<ArticleEssential> articleEssentialList = mArticleEssentialListsByCategory.get(categoryName);
		if (articleEssentialList == null) {
			articleEssentialList = new ArrayList<ArticleEssential>();
			mArticleEssentialListsByCategory.put(categoryName, articleEssentialList);
		}
		return articleEssentialList;
	}

    /**
     *
     * @param categoryName
     * @return
     */
    public synchronized int getCurrentlyLoadedPageNumberForCategory(final String categoryName) {
        int result = -1;
        Integer currentNumber = mArticleEssentialCurrentPageNumbersByCategory.get(categoryName);
        if (currentNumber != null) {
            result = currentNumber;
        }
        return result;
    }

	/**
	 *
	 * @param categoryName
	 * @param pageNumber
	 */
	public synchronized void setCurrentlyLoadedPageNumberForCategory(final String categoryName, final int pageNumber) {
		mArticleEssentialCurrentPageNumbersByCategory.put(categoryName, pageNumber);
	}

    /**
     *
     * @param categoryName
     * @return
     */
    public synchronized boolean getLoadingMoreFlagForCategory(final String categoryName) {
        boolean result = false;
        Boolean loadingMoreItems = mArticleEssentialLoadingMoreFlagByCategory.get(categoryName);
        if (loadingMoreItems != null) {
            result = loadingMoreItems;
        }
        return result;
    }

    /**
     *
     * @param loadingMoreItems
     * @param categoryName
     */
    public synchronized  void setLoadingMoreFlagForCategory(final boolean loadingMoreItems, final String categoryName) {
        mArticleEssentialLoadingMoreFlagByCategory.put(categoryName, loadingMoreItems);
    }

    /**
     *
     * @param categoryName
     * @return
     */
    public synchronized boolean getEndOfItemsReachedFlagForCategory(final String categoryName) {
        boolean result = false;
        Boolean endOfItemsReached = mArticleEssentialEndOfItemsReachedFlagByCategory.get(categoryName);
        if (endOfItemsReached != null) {
            result = endOfItemsReached;
        }
        return result;
    }

    /**
     *
     * @param endOfItemsReached
     * @param categoryName
     */
    public synchronized  void setEndOfItemsReachedFlagForCategory(final boolean endOfItemsReached, final String categoryName) {
        mArticleEssentialEndOfItemsReachedFlagByCategory.put(categoryName, endOfItemsReached);
    }

    /**
     *
     * @param articleId
     * @return
     */
    public synchronized Article getArticleById(final long articleId) {
        return mArticlesByIdMap.get(articleId);
    }

	public synchronized void addArticle(final Article article) {
		mArticlesByIdMap.put(article.getArticleId(), article);
	}
}