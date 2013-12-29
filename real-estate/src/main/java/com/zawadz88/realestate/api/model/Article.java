package com.zawadz88.realestate.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created: 16.11.13
 *
 * @author Zawada
 */
public class Article implements Serializable {

	@SerializedName("x")
	private long articleId;

	@SerializedName("s")
	private long sectionId;

	@SerializedName("t")
	private String title;

	@SerializedName("c")
	private String content;

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

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}
}
