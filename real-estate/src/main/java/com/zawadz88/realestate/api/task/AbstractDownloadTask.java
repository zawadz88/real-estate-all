package com.zawadz88.realestate.api.task;

import android.os.AsyncTask;
import android.util.Log;
import com.zawadz88.realestate.api.AsyncTaskListener;
import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.util.Constants;

/**
 * Created: 16.11.13
 *
 * @author Zawada
 */
public abstract class AbstractDownloadTask extends AsyncTask<Void, Void, Void> {

	protected static final String SERVER_URL = "http://gazeta.app.gazeta.pl";

	protected AsyncTaskListener taskListener;
	protected TaskResult taskResult = TaskResult.SUCCESSFUL;
	protected boolean cancelled;
	private Exception exception;
	protected boolean containsErrors;

	protected String tag;

	protected AbstractDownloadTask(final String tag) {
		this.tag = tag;
	}


	@Override
	protected final Void doInBackground(Void... params) {
		try	{
			doInBackgroundSafe();
		} catch (Exception e)	{
			Log.e(Constants.TAG, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "null", e);
			containsErrors = true;
			this.exception = e;
		}
		return null;
	}

	protected abstract void doInBackgroundSafe() throws Exception;

	@Override
	protected void onPostExecute(Void params) {
		if (taskListener != null) {
			if (cancelled) {
				taskListener.onTaskCancelled(this);
			} else if (containsErrors) {
				taskListener.onTaskFailed(this, this.exception);
			} else {
				taskListener.onTaskSuccessful(this);
			}
		}
	};

	@Override
	protected void onCancelled() {
		if (taskListener != null) {
			taskListener.onTaskCancelled(this);
		}
	}

	public void cancel() {
		cancelled = true;
		onCancelled();
	}

	public boolean isCancelledOverride() {
		return cancelled;
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
