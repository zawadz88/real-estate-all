package com.zawadz88.realestate.api.eventbus;

import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.api.model.ArticleEssential;

import java.util.List;

/**
 * An event that is passed via EventBus when a list of {@link com.zawadz88.realestate.api.model.ArticleEssential} was done downloading.
 *
 * @author Piotr Zawadzki
 */
public class ArticleEssentialDownloadEvent extends AbstractDownloadEvent {

    /**
     * Name of the {@link com.zawadz88.realestate.api.model.ArticleCategory} for which a list was being fetched
     */
	private final String categoryName;

    /**
     * A list of downloaded article's, null if download failed or was cancelled
     */
    private final List<ArticleEssential> downloadedArticles;

	public ArticleEssentialDownloadEvent(final TaskResult result, final Exception exception, final String categoryName, final List<ArticleEssential> articleEssentialList) {
		super(result, exception);
		this.categoryName = categoryName;
        this.downloadedArticles = articleEssentialList;
	}

	public String getCategoryName() {
		return categoryName;
	}

    public List<ArticleEssential> getDownloadedArticles() {
        return downloadedArticles;
    }

    @Override
    public String toString() {
        return "ArticleEssentialDownloadEvent{" +
                "categoryName='" + categoryName + '\'' +
                ", downloadedArticles=" + downloadedArticles +
                '}';
    }
}
