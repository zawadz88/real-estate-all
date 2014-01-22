package com.zawadz88.realestate.task.util;

import com.zawadz88.realestate.task.BaseDownloadTask;

/**
 * Listener interface listening on {@link com.zawadz88.realestate.task.BaseDownloadTask}s
 *
 * @author Piotr Zawadzki
 */
public interface AsyncTaskListener {

    /**
     * Called when task succeeded
     * @param task
     */
	void onTaskSuccessful(final BaseDownloadTask task);

    /**
     * Called when task failed
     * @param task
     * @param exception exception associated with the failure
     */
	void onTaskFailed(final BaseDownloadTask task, final Exception exception);

    /**
     * Called when task was cancelled
     * @param task
     */
	void onTaskCancelled(final BaseDownloadTask task);
	
}
