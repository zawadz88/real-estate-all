package com.zawadz88.realestate.task.util;

import com.zawadz88.realestate.task.AbstractDownloadTask;

public interface AsyncTaskListener {

	void onTaskSuccessful(final AbstractDownloadTask task);
	void onTaskFailed(final AbstractDownloadTask task, final Exception exception);
	void onTaskCancelled(final AbstractDownloadTask task);
	
}
