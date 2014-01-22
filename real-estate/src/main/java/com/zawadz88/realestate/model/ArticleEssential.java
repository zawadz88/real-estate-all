package com.zawadz88.realestate.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * POJO containing essential information about an article, e.g. to present in a list of articles without redundant information.
 *
 * @author Piotr Zawadzki
 */
public class ArticleEssential implements Serializable {
    /**
     * Article's unique identifier
     */
    @SerializedName("x")
    private long articleId;

    /**
     * Section's identifier
     */
    @SerializedName("s")
    private long sectionId;

    /**
     * Article's title
     */
    @SerializedName("t")
    private String title;

    /**
     * URL of the image associated with the article
     */
	@SerializedName("li")
	private String imageUrl;

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(final long articleId) {
		this.articleId = articleId;
	}

	public long getSectionId() {
		return sectionId;
	}

	public void setSectionId(final long sectionId) {
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
