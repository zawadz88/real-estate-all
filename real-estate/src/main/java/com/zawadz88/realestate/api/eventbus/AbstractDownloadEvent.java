package com.zawadz88.realestate.api.eventbus;

import com.zawadz88.realestate.api.TaskResult;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class AbstractDownloadEvent {

	protected final TaskResult result;
    protected final Exception exception;

	public AbstractDownloadEvent(final TaskResult result, final Exception exception) {
		this.result = result;
        this.exception = exception;
	}

	public boolean isSuccessful() {
		return TaskResult.SUCCESSFUL.equals(result);
	}

	public TaskResult getResult() {
		return result;
	}

    public Exception getException() {
        return exception;
    }
}
