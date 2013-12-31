package com.zawadz88.realestate.api.eventbus;

import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.api.model.Article;

/**
 * An event that is passed via EventBus when an {@link com.zawadz88.realestate.api.model.Article} was done downloading.
 *
 * @author Piotr Zawadzki
 */
public class ArticleDownloadEvent extends AbstractDownloadEvent {

    /**
     * Article's unique identifier
     */
	private final long articleId;

    /**
     * Downloaded article, null if download failed or was cancelled
     */
    private final Article downloadedArticle;

	public ArticleDownloadEvent(final TaskResult result, final Exception exception, final long articleId, final Article article) {
		super(result, exception);
		this.articleId = articleId;
        this.downloadedArticle = article;
	}

    public long getArticleId() {
        return articleId;
    }

    public Article getDownloadedArticle() {
        return downloadedArticle;
    }

}
