package com.zawadz88.realestate.api.task;

import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.model.ArticleEssential;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

import java.util.List;

/**
 * Task responsible for downloading a list articles' essential information
 *
 * @author Piotr Zawadzki
 */
public class ArticleListDownloadTask extends AbstractDownloadTask {

	private ArticleListService service;

    /**
     * Name of the {@link com.zawadz88.realestate.api.model.ArticleCategory} for which the list is being downloaded
     */
	private String category;

    /**
     * Number of the page of articles
     */
	private int pageNumber;

    /**
     * Downloaded article list, null if task did not succeed
     */
	private List<ArticleEssential> articleList;

	public ArticleListDownloadTask(String category, int pageNumber) {
		super(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX + category);
		this.category = category;
		this.pageNumber = pageNumber;
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer(SERVER_URL)
				.build();

		setService(restAdapter.create(ArticleListService.class));
	}

	@Override
	protected void doInBackgroundSafe() throws Exception {
		this.articleList = service.getArticleEssentialList(this.category, this.pageNumber);
	}

	public List<ArticleEssential> getArticleList() {
		return articleList;
	}

	public String getCategory() {
		return category;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setService(final ArticleListService service) {
		this.service = service;
	}

	public interface ArticleListService {
		@GET("/index/{category}/{pageNumber}")
		List<ArticleEssential> getArticleEssentialList(@Path("category") String category, @Path("pageNumber") int pageNumber);
	}
}
