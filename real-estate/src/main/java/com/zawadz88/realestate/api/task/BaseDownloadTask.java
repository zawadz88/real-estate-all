package com.zawadz88.realestate.api.task;

import android.os.AsyncTask;
import android.util.Log;
import com.zawadz88.realestate.api.AsyncTaskListener;
import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.util.Constants;

/**
 * A base class for all API download tasks
 *
 * @author Piotr Zawadzki
 */
public class BaseDownloadTask extends AsyncTask<Void, Void, Void> {

    /**
     * Base URL for API calls
     */
	protected static final String SERVER_URL = "http://gazeta.app.gazeta.pl";

    /**
     * Listener that should be notified in {@code onPostExecute}
     */
	protected AsyncTaskListener taskListener;

    /**
     * Result of the download
     */
	protected TaskResult taskResult = TaskResult.SUCCESSFUL;

	/**
	 * True, if task was cancelled
	 */
	protected boolean cancelled;

	/**
	 * True, if task which was cancelled got notified
	 */
	protected boolean cancelledTaskNotified;

    /**
     * Exception that occurred if download failed, null if task did not fail
     */
	private Exception exception;

    /**
     * Unique tag of the task. Tasks for the same resources should have the same tags
     */
	protected String tag;

	public BaseDownloadTask(final String tag) {
		this.tag = tag;
	}


	@Override
	protected final Void doInBackground(Void... params) {
		try	{
			doInBackgroundSafe();
		} catch (Exception e)	{
			Log.e(Constants.TAG, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "null", e);
			this.exception = e;
		}
		return null;
	}

    /**
     * Method to be overriden instead of {@code doInBackground}. Error handling is not necessary in this method.
     * @throws Exception
     */
	protected void doInBackgroundSafe() throws Exception {}

	@Override
	protected void onPostExecute(Void params) {
		if (taskListener != null) {
			if (exception != null) {
				taskListener.onTaskFailed(this, this.exception);
			} else {
				taskListener.onTaskSuccessful(this);
			}
		}
	};

	@Override
	protected void onCancelled() {
		handleCancelled();
	}

	@Override
	protected void onCancelled(final Void aVoid) {
		handleCancelled();
	}

	private void handleCancelled() {
		if (taskListener != null) {
			taskListener.onTaskCancelled(this);
		}
	}

	public AsyncTaskListener getListener() {
		return taskListener;
	}

	public void setListener(AsyncTaskListener listener) {
		this.taskListener = listener;
	}

	public Exception getException() {
		return exception;
	}

	public String getTag() {
		return tag;
	}
}
