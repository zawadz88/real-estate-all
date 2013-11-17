package com.zawadz88.realestate.model;

/**
 * Created: 16.11.13
 *
 * @author Zawada
 */
public class ArticleEssential {

	private int articleId;

	private int sectionId;

	private String title;

	private String imageUrl;

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(final int articleId) {
		this.articleId = articleId;
	}

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(final int sectionId) {
		this.sectionId = sectionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
