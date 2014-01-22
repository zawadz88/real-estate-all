package com.zawadz88.realestate.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * POJO containing article's information
 *
 * @author Piotr Zawadzki
 */
public class Article implements Serializable {

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
     * Article's content. Can contain HTML tags.
     */
	@SerializedName("c")
	private String content;

    /**
     * Web URL of the article
     */
    @SerializedName("u")
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", sectionId=" + sectionId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
