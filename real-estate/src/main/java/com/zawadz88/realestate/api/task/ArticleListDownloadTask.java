package com.zawadz88.realestate.api.task;

import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.model.ArticleEssential;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

import java.util.List;

/**
 * Created: 16.11.13
 *
 * @author Zawada
 */
public class ArticleListDownloadTask extends AbstractDownloadTask {

	private final ArticleListService service;
	private String category;
	private int pageNumber;
	private List<ArticleEssential> articleList;

	public ArticleListDownloadTask(String category, int pageNumber) {
		super(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG);
		this.category = category;
		this.pageNumber = pageNumber;
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer("http://gazeta.app.gazeta.pl")
				.build();

		service = restAdapter.create(ArticleListService.class);
	}

	@Override
	protected void doInBackgroundSafe() throws Exception {
//		List<ArticleEssential> list = new ArrayList<ArticleEssential>();

//		for (int i = 0; i < 20; i++) {
//			ArticleEssential item1 = new ArticleEssential();
//			item1.setArticleId(i);
//			item1.setSectionId(100 + i);
//			item1.setTitle("Article" + i);
//			item1.setImageUrl("image url " + i);
//			list.add(item1);
//		}
		this.articleList = service.getArticleEssentialList(this.category, this.pageNumber);
	}

	public List<ArticleEssential> getArticleList() {
		return articleList;
	}

	public interface ArticleListService {
		@GET("/index/{category}/{pageNumber}")
		List<ArticleEssential> getArticleEssentialList(@Path("category") String category, @Path("pageNumber") int pageNumber);
	}
}
