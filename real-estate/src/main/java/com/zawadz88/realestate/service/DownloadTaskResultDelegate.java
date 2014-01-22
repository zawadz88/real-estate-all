package com.zawadz88.realestate.service;

import com.zawadz88.realestate.event.ArticleDownloadEvent;
import com.zawadz88.realestate.event.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.model.Article;
import com.zawadz88.realestate.model.ArticleEssential;
import com.zawadz88.realestate.task.ArticleDownloadTask;
import com.zawadz88.realestate.task.ArticleListDownloadTask;
import com.zawadz88.realestate.task.BaseDownloadTask;
import com.zawadz88.realestate.task.util.AsyncTaskListener;
import com.zawadz88.realestate.task.util.TaskResult;
import de.greenrobot.event.EventBus;

import java.util.List;

/**
 * Delegate for posting events to EventBus based on received tasks.
 * Created: 22.01.14
 *
 * @author Piotr Zawadzki
 */
public class DownloadTaskResultDelegate implements AsyncTaskListener {

	private final ContentHolder mContentHolder;

	/**
	 * Default event bus
	 */
	private final EventBus mBus;

	public DownloadTaskResultDelegate(final ContentHolder contentHolder) {
		mContentHolder = contentHolder;
		mBus = EventBus.getDefault();
	}

	@Override
	public void onTaskSuccessful(final BaseDownloadTask task) {
		if (task instanceof ArticleListDownloadTask) {
			ArticleListDownloadTask articleListDownloadTask = (ArticleListDownloadTask) task;
			List<ArticleEssential> articleEssentialList = mContentHolder.getArticleEssentialListByCategory(articleListDownloadTask.getCategory());
			articleEssentialList.addAll(articleListDownloadTask.getArticleList());
			mContentHolder.setCurrentlyLoadedPageNumberForCategory(articleListDownloadTask.getCategory(), articleListDownloadTask.getPageNumber());
			mBus.post(new ArticleEssentialDownloadEvent(TaskResult.SUCCESSFUL, null, articleListDownloadTask.getCategory(), articleListDownloadTask.getArticleList()));
		} else if (task instanceof ArticleDownloadTask) {
			ArticleDownloadTask articleDownloadTask = (ArticleDownloadTask) task;
			Article article = articleDownloadTask.getArticle();
			mContentHolder.addArticle(article);
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
