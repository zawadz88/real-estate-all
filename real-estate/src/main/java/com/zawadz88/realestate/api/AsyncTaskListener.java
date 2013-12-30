package com.zawadz88.realestate.api;

import com.zawadz88.realestate.api.task.AbstractDownloadTask;

/**
 * Listener interface listening on {@link com.zawadz88.realestate.api.task.AbstractDownloadTask}s
 *
 * @author Piotr Zawadzki
 */
public interface AsyncTaskListener {

    /**
     * Called when task succeeded
     * @param task
     */
	void onTaskSuccessful(final AbstractDownloadTask task);

    /**
     * Called when task failed
     * @param task
     * @param exception exception associated with the failure
     */
	void onTaskFailed(final AbstractDownloadTask task, final Exception exception);

    /**
     * Called when task was cancelled
     * @param task
     */
	void onTaskCancelled(final AbstractDownloadTask task);
	
}
