package com.zawadz88.realestate.api.eventbus;

import com.zawadz88.realestate.api.TaskResult;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class ArticleEssentialDownloadEvent extends AbstractDownloadEvent {

	public ArticleEssentialDownloadEvent(final TaskResult result) {
		super(result);
	}
}
