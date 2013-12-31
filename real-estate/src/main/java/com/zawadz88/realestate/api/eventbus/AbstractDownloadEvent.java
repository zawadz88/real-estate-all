package com.zawadz88.realestate.api.eventbus;

import com.zawadz88.realestate.api.TaskResult;

/**
 * An event that is passed via EventBus that is to be used a base class for all download events
 * Created: 17.11.13
 *
 * @author Piotr Zawadzki
 */
public class AbstractDownloadEvent {

    /**
     * Indicates whether download was successful or not
     */
	protected final TaskResult result;

    /**
     * If download is not successful, this will contain the {@link Exception} thrown (should be an instance of {@link retrofit.RetrofitError}, null if successful)
     */
    protected final Exception exception;

	public AbstractDownloadEvent(final TaskResult result, final Exception exception) {
		this.result = result;
        this.exception = exception;
	}

    /**
     * @return true if download was successful, false otherwise
     */
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
