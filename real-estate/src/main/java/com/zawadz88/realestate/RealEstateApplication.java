package com.zawadz88.realestate;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import com.zawadz88.realestate.task.util.AsyncTaskListener;
import com.zawadz88.realestate.service.DownloadTaskResultDelegate;
import com.zawadz88.realestate.task.BaseDownloadTask;
import com.zawadz88.realestate.service.ContentHolder;
import com.zawadz88.realestate.util.DeviceUtils;

import java.util.*;

/**
 *
 * @author Piotr Zawadzki
 */
public class RealEstateApplication extends Application implements AsyncTaskListener {

    public static final String DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX = "ArticleListDownloadTask:";
    public static final String DOWNLOAD_ARTICLE_TAG_PREFIX = "ArticleDownloadTask:";

    /**
     * Map containing currently executing {@link com.zawadz88.realestate.task.BaseDownloadTask}s.
     * Tasks are mapped with tags that are unique for a given download
     */
	private Map<String, BaseDownloadTask> mDownloadTasks = (Map<String, BaseDownloadTask>) Collections.synchronizedMap(new HashMap<String, BaseDownloadTask>());

	private DownloadTaskResultDelegate mTaskResultDelegate;
	private ContentHolder mContentHolder;

    /**
     * A flag indicating if the application has been just created. This should be set to false in onCreate of all activities.
     * Used mainly to replace the {@link com.zawadz88.realestate.service.ContentHolder} instance with the one stored in the {@code savedInstanceState} Bundle
     */
    private boolean mNewlyCreated = true;

	@Override
	public void onCreate() {
		super.onCreate();
		setContentHolder(new ContentHolder());
		setTaskResultDelegate(new DownloadTaskResultDelegate(mContentHolder));
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

	public ContentHolder getContentHolder() {
		return mContentHolder;
	}

	public void setContentHolder(final ContentHolder contentHolder) {
		this.mContentHolder = contentHolder;
	}

	/**
	 * Adds a task to activity's currently executing tasks, starts it and sets this activity as its listener
	 * @param task
	 * @return true if task was added to currently executing tasks and started, false otherwise
	 */
	@SuppressLint("NewApi")
    public synchronized boolean startTask(final BaseDownloadTask task) {
        boolean started = false;
		final String tag = task.getTag();
		if (!mDownloadTasks.containsKey(tag)) {
			started = true;
			task.setListener(this);
			mDownloadTasks.put(tag, task);
			if (DeviceUtils.isEqualOrHigherThanHoneycomb()) {
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				task.execute();
			}
		}
		return started;
	}

    /**
     * Checks if a task with a given task is currently executing
     * @param tag tag associated with a given tag
     * @return {@code true} if task is currently executing
     */
	public synchronized boolean isExecutingTask(final String tag) {
		return mDownloadTasks.containsKey(tag);
	}

    public boolean isNewlyCreated() {
        return mNewlyCreated;
    }

    public void setNewlyCreated(boolean newlyCreated) {
        this.mNewlyCreated = newlyCreated;
    }
}