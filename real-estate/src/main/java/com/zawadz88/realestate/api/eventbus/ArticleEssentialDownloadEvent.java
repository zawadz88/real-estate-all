package com.zawadz88.realestate.api.eventbus;

import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.api.model.ArticleEssential;

import java.util.List;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
public class ArticleEssentialDownloadEvent extends AbstractDownloadEvent {

	private final String categoryName;
    private final List<ArticleEssential> downloadedArticles;

	public ArticleEssentialDownloadEvent(final TaskResult result, final String categoryName, final List<ArticleEssential> articleEssentialList) {
		super(result);
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
