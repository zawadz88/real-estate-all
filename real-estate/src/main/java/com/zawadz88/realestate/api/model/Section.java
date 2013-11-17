package com.zawadz88.realestate.api.model;

import com.zawadz88.realestate.R;

/**
 * Created: 04.11.13
 *
 * @author Zawada
 */
public enum Section {
	ADS(0, R.string.section_title_ads), ARTICLES(1, R.string.section_title_articles), PROJECTS(2, R.string.section_title_projects);

	private final int position;
	private final int titleResourceId;

	private Section(final int position, final int titleResourceId) {
		this.position = position;
		this.titleResourceId = titleResourceId;
	}

	public int getPosition() {
		return position;
	}

	public int getTitleResourceId() {
		return titleResourceId;
	}

	public static Section getSectionForPosition(final int position) {
		Section result = null;
		for (Section section : values()) {
			if (section.getPosition() == position) {
				result = section;
				break;
			}
		}
		return  result;
	}
}
