package com.zawadz88.realestate.task;

import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.model.Article;
import com.zawadz88.realestate.model.ArticleEssential;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * A task responsible for downloading an {@link com.zawadz88.realestate.model.Article}
 *
 * @author Piotr Zawadzki
 */
public class ArticleDownloadTask extends BaseDownloadTask {

	private ArticleService service;

    /**
     * Essential information about an article containing article's ID and article's section ID
     */
	private final ArticleEssential articleEssential;

    /**
     * Downloaded article, null if task did not succeed
     */
	private Article article;

	public ArticleDownloadTask(ArticleEssential articleEssential) {
		super(RealEstateApplication.DOWNLOAD_ARTICLE_TAG_PREFIX + articleEssential.getArticleId());
		this.articleEssential = articleEssential;
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer(SERVER_URL)
				.build();

		setService(restAdapter.create(ArticleService.class));
	}

	@Override
	protected void doInBackgroundSafe() throws Exception {
        Article [] articles = service.getArticle(articleEssential.getSectionId(), articleEssential.getArticleId());
        if (articles != null && articles.length > 0) {
            this.article = articles[0];
        }
	}

    public ArticleEssential getArticleEssential() {
        return articleEssential;
    }

    public Article getArticle() {
        return article;
    }

	public void setService(final ArticleService service) {
		this.service = service;
	}

	public interface ArticleService {
		@GET("/article/{sectionId}.{articleId}.1")
        Article [] getArticle(@Path("sectionId") long sectionId, @Path("articleId") long articleId);//API returns an array...
	}
}
