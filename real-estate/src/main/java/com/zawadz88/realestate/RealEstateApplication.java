package com.zawadz88.realestate;

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

	public static final String DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG = "ArticleListDownloadTask";

	private EventBus mBus;

	private Map<String, AbstractDownloadTask> mDownloadTasks = (Map<String, AbstractDownloadTask>) Collections.synchronizedMap(new HashMap<String, AbstractDownloadTask>());

	private List<ArticleEssential> mArticleEssentialList = new ArrayList<ArticleEssential>();

	@Override
	public void onCreate() {
		super.onCreate();
		mBus = EventBus.getDefault();
	}

	@Override
	public synchronized void onTaskSuccessful(final AbstractDownloadTask task) {
		mDownloadTasks.remove(task.getTag());
		if (task instanceof ArticleListDownloadTask) {
			mArticleEssentialList.addAll(((ArticleListDownloadTask) task).getArticleList());
			mBus.post(new ArticleEssentialDownloadEvent(TaskResult.SUCCESSFUL));
		}
	}

	@Override
	public synchronized void onTaskFailed(final AbstractDownloadTask task, final Exception exception) {
		mDownloadTasks.remove(task.getTag());
		mBus.post(new ArticleEssentialDownloadEvent(TaskResult.FAILED));
	}

	@Override
	public synchronized void onTaskCancelled(final AbstractDownloadTask task) {
		mDownloadTasks.remove(task.getTag());
	}

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

	public List<ArticleEssential> getArticleEssentialList() {
		return mArticleEssentialList;
	}

}
