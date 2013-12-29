package com.zawadz88.realestate.api.task;

import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.model.Article;
import com.zawadz88.realestate.api.model.ArticleEssential;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created: 16.11.13
 *
 * @author Zawada
 */
public class ArticleDownloadTask extends AbstractDownloadTask {

	private final ArticleService service;
	private final ArticleEssential articleEssential;
	private Article article;

	public ArticleDownloadTask(ArticleEssential articleEssential) {
		super(RealEstateApplication.DOWNLOAD_ARTICLE_TAG_PREFIX + articleEssential.getArticleId());
		this.articleEssential = articleEssential;
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer(SERVER_URL)
				.build();

		service = restAdapter.create(ArticleService.class);
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

    public interface ArticleService {
		@GET("/article/{sectionId}.{articleId}.1")
        Article [] getArticle(@Path("sectionId") long sectionId, @Path("articleId") long articleId);//API returns an array...
	}
}
