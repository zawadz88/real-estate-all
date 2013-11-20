package com.zawadz88.realestate.api.eventbus;

import com.zawadz88.realestate.api.TaskResult;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class ArticleEssentialDownloadEvent extends AbstractDownloadEvent {

	private final String categoryName;

	public ArticleEssentialDownloadEvent(final TaskResult result, final String categoryName) {
		super(result);
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}
}
