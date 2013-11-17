package com.zawadz88.realestate.task.eventbus;

import com.zawadz88.realestate.task.util.TaskResult;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class AbstractDownloadEvent {

	private final TaskResult result;

	public AbstractDownloadEvent(final TaskResult result) {
		this.result = result;
	}

	public boolean isSuccessful() {
		return TaskResult.SUCCESSFUL.equals(result);
	}

	public TaskResult getResult() {
		return result;
	}
}
