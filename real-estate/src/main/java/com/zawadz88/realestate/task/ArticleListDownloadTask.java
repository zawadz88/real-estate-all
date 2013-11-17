package com.zawadz88.realestate.task;

import android.os.SystemClock;
import android.util.SparseArray;
import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.model.ArticleEssential;
import com.zawadz88.realestate.task.util.AsyncTaskListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 16.11.13
 *
 * @author Zawada
 */
public class ArticleListDownloadTask extends AbstractDownloadTask {

	private String category;
	private int pageNumber;
	private List<ArticleEssential> articleList;

	public ArticleListDownloadTask(String category, int pageNumber) {
		super(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG);
		this.category = category;
		this.pageNumber = pageNumber;
	}

	@Override
	protected void doInBackgroundSafe() throws Exception {
		List<ArticleEssential> list = new ArrayList<ArticleEssential>();
		//TODO fetch from API
		for (int i = 0; i < 20; i++) {
			ArticleEssential item1 = new ArticleEssential();
			item1.setArticleId(i);
			item1.setSectionId(100 + i);
			item1.setTitle("Article" + i);
			item1.setImageUrl("image url " + i);
			list.add(item1);
		}
		SystemClock.sleep(3000);
		this.articleList = list;
	}

	public List<ArticleEssential> getArticleList() {
		return articleList;
	}
}
