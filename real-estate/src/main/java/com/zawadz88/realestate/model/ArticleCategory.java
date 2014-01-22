package com.zawadz88.realestate.model;

import com.zawadz88.realestate.R;

/**
 * Category of the article. Articles are displayed inside of a ViewPager
 * and grouped by a category, containing a set of articles published under the same category.
 *
 * @author Piotr Zawadzki
 */
public enum ArticleCategory {
	NEWEST(0, R.string.article_title_newest, "warszawa"), POPULAR(1, R.string.article_title_popular, "mostpopular"), TIPS(2, R.string.article_title_tips, "rozrywka"), LOANS(3, R.string.article_title_loans, "sport");

    /**
     * Android's resource ID linking to a string with category's title
     */
	private final int titleResource;

    /**
     * Position of the category
     */
	private final int position;

    /**
     * Unique name of the category used when querying the API.
     */
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
