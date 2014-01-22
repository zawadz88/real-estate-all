package com.zawadz88.realestate.api;

import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.eventbus.ArticleDownloadEvent;
import com.zawadz88.realestate.api.eventbus.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.api.model.Article;
import com.zawadz88.realestate.api.model.ArticleEssential;
import com.zawadz88.realestate.api.task.ArticleDownloadTask;
import com.zawadz88.realestate.api.task.ArticleListDownloadTask;
import com.zawadz88.realestate.api.task.BaseDownloadTask;
import de.greenrobot.event.EventBus;

import java.util.List;

/**
 * Delegate for posting events to EventBus based on received tasks.
 * Created: 22.01.14
 *
 * @author Piotr Zawadzki
 */
public class DownloadTaskResultDelegate implements AsyncTaskListener {

	private final RealEstateApplication mApplication;

	/**
	 * Default event bus
	 */
	private final EventBus mBus;

	public DownloadTaskResultDelegate(final RealEstateApplication realEstateApplication) {
		mApplication = realEstateApplication;
		mBus = EventBus.getDefault();
	}

	@Override
	public void onTaskSuccessful(final BaseDownloadTask task) {
		if (task instanceof ArticleListDownloadTask) {
			ArticleListDownloadTask articleListDownloadTask = (ArticleListDownloadTask) task;
			List<ArticleEssential> articleEssentialList = mApplication.getArticleEssentialListByCategory(articleListDownloadTask.getCategory());
			articleEssentialList.addAll(articleListDownloadTask.getArticleList());
			mApplication.setCurrentlyLoadedPageNumberForCategory(articleListDownloadTask.getCategory(), articleListDownloadTask.getPageNumber());
			mBus.post(new ArticleEssentialDownloadEvent(TaskResult.SUCCESSFUL, null, articleListDownloadTask.getCategory(), articleListDownloadTask.getArticleList()));
		} else if (task instanceof ArticleDownloadTask) {
			ArticleDownloadTask articleDownloadTask = (ArticleDownloadTask) task;
			Article article = articleDownloadTask.getArticle();
			mApplication.addArticle(article);
			mBus.post(new ArticleDownloadEvent(TaskResult.SUCCESSFUL, null, article.getArticleId(), article));
		}
	}

	@Override
	public void onTaskFailed(final BaseDownloadTask task, final Exception exception) {
		if (task instanceof ArticleListDownloadTask) {
			mBus.post(new ArticleEssentialDownloadEvent(TaskResult.FAILED, exception, ((ArticleListDownloadTask) task).getCategory(), null));
		} else if (task instanceof ArticleDownloadTask) {
			ArticleDownloadTask articleDownloadTask = (ArticleDownloadTask) task;
			mBus.post(new ArticleDownloadEvent(TaskResult.FAILED, exception, articleDownloadTask.getArticleEssential().getArticleId(), null));
		}
	}

	@Override
	public void onTaskCancelled(final BaseDownloadTask task) {

	}

}
