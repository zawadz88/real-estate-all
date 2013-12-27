package com.zawadz88.realestate;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import com.zawadz88.realestate.api.eventbus.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.api.model.ArticleEssential;
import com.zawadz88.realestate.api.task.AbstractDownloadTask;
import com.zawadz88.realestate.api.task.ArticleListDownloadTask;
import com.zawadz88.realestate.api.AsyncTaskListener;
import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.util.DeviceUtils;
import de.greenrobot.event.EventBus;

import java.util.*;

public class RealEstateApplication extends Application implements AsyncTaskListener {

	public static final String DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX = "ArticleListDownloadTask:";

	private EventBus mBus;

	private Map<String, AbstractDownloadTask> mDownloadTasks = (Map<String, AbstractDownloadTask>) Collections.synchronizedMap(new HashMap<String, AbstractDownloadTask>());

	private Map<String, List<ArticleEssential>> mArticleEssentialListsByCategory = new HashMap<String, List<ArticleEssential>>();
	private Map<String, Integer> mArticleEssentialCurrentPageNumbersByCategory = new HashMap<String, Integer>();

	@Override
	public void onCreate() {
		super.onCreate();
		mBus = EventBus.getDefault();
	}

	@Override
	public synchronized void onTaskSuccessful(final AbstractDownloadTask task) {
		mDownloadTasks.remove(task.getTag());
		if (task instanceof ArticleListDownloadTask) {
			ArticleListDownloadTask articleListDownloadTask = (ArticleListDownloadTask) task;

			List<ArticleEssential> articleEssentialList = getArticleEssentialListByCategory(articleListDownloadTask.getCategory());

			articleEssentialList.addAll(articleListDownloadTask.getArticleList());
			mArticleEssentialCurrentPageNumbersByCategory.put(articleListDownloadTask.getCategory(), articleListDownloadTask.getPageNumber());
			mBus.post(new ArticleEssentialDownloadEvent(TaskResult.SUCCESSFUL, articleListDownloadTask.getCategory(), articleListDownloadTask.getArticleList()));
		}
	}

	@Override
	public synchronized void onTaskFailed(final AbstractDownloadTask task, final Exception exception) {
		mDownloadTasks.remove(task.getTag());
		if (task instanceof ArticleListDownloadTask) {
			mBus.post(new ArticleEssentialDownloadEvent(TaskResult.FAILED, ((ArticleListDownloadTask) task).getCategory(), null));
		}
	}

	@Override
	public synchronized void onTaskCancelled(final AbstractDownloadTask task) {
		mDownloadTasks.remove(task.getTag());
	}

	@SuppressLint("NewApi")
    public synchronized void startTask(final AbstractDownloadTask task) {
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

	public synchronized boolean isExecutingTask(final String tag) {
		return mDownloadTasks.containsKey(tag);
	}

	public synchronized List<ArticleEssential> getArticleEssentialListByCategory(final String categoryName) {
		List<ArticleEssential> articleEssentialList = mArticleEssentialListsByCategory.get(categoryName);
		if (articleEssentialList == null) {
			articleEssentialList = new ArrayList<ArticleEssential>();
			mArticleEssentialListsByCategory.put(categoryName, articleEssentialList);
		}
		return articleEssentialList;
	}

	public synchronized int getCurrentlyLoadedPageNumberForCategory(final String categoryName) {
		int result = -1;
		Integer currentNumber = mArticleEssentialCurrentPageNumbersByCategory.get(categoryName);
		if (currentNumber != null) {
			result = currentNumber;
		}
		return result;
	}

}
