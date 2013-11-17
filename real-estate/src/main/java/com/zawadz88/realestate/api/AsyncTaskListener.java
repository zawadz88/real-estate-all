package com.zawadz88.realestate.api;

import com.zawadz88.realestate.api.task.AbstractDownloadTask;

public interface AsyncTaskListener {

	void onTaskSuccessful(final AbstractDownloadTask task);
	void onTaskFailed(final AbstractDownloadTask task, final Exception exception);
	void onTaskCancelled(final AbstractDownloadTask task);
	
}
