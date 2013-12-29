package com.zawadz88.realestate.api.eventbus;

import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.api.model.Article;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class ArticleDownloadEvent extends AbstractDownloadEvent {

	private final long articleId;
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
