package com.zawadz88.realestate.api.model;

import com.zawadz88.realestate.R;

/**
 * Created: 10.11.13
 *
 * @author Zawada
 */
public enum ArticleCategory {
	NEWEST(0, R.string.article_title_newest), POPULAR(1, R.string.article_title_popular), TIPS(2, R.string.article_title_tips), LOANS(3, R.string.article_title_loans);

	private final int titleResource;
	private final int position;

	private ArticleCategory(final int position, final int titleResource) {
		this.position = position;
		this.titleResource = titleResource;
	}

	public int getTitleResource() {
		return titleResource;
	}

	public int getPosition() {
		return position;
	}
}
