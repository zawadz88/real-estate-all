package com.zawadz88.realestate.api.model;

import com.zawadz88.realestate.R;

/**
 * Created: 10.11.13
 *
 * @author Zawada
 */
public enum ArticleCategory {
	NEWEST(0, R.string.article_title_newest, "warszawa"), POPULAR(1, R.string.article_title_popular, "mostpopular"), TIPS(2, R.string.article_title_tips, "rozrywka"), LOANS(3, R.string.article_title_loans, "sport");

	private final int titleResource;
	private final int position;
	private final String name;

	private ArticleCategory(final int position, final int titleResource, final String name) {
		this.position = position;
		this.titleResource = titleResource;
		this.name = name;
	}

	public int getTitleResource() {
		return titleResource;
	}

	public int getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}
}
