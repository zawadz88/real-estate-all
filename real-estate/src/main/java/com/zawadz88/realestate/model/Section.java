package com.zawadz88.realestate.model;

import com.zawadz88.realestate.R;

/**
 * Enum containing sections of the application
 *
 * @author Piotr Zawadzki
 */
public enum Section {
	ADS(0, R.string.section_title_ads), ARTICLES(1, R.string.section_title_articles), PROJECTS(2, R.string.section_title_projects);

    /**
     * Position in Navigation Drawer
     */
	private final int position;

    /**
     * Android's resource ID linking to a string with section's title
     */
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

    /**
     * Returns a {@link Section} whose position is equal to the one passed in the arguments
     * @param position section's position
     * @return section for that position, null if section was not found
     */
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
