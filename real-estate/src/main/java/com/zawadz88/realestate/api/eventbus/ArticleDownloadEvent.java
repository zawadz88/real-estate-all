package com.zawadz88.realestate.api.eventbus;

import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.api.model.Article;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class ArticleDownloadEvent extends AbstractDownloadEvent {

	private final int articleId;
    private final Article downloadedArticle;

	public ArticleDownloadEvent(final TaskResult result, final int articleId, final Article article) {
		super(result);
		this.articleId = articleId;
        this.downloadedArticle = article;
	}

    public int getArticleId() {
        return articleId;
    }

    public Article getDownloadedArticle() {
        return downloadedArticle;
    }
}
