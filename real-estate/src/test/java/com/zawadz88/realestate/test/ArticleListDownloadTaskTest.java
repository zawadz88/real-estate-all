package com.zawadz88.realestate.test;

import static junit.framework.Assert.*;
import static com.jayway.awaitility.Awaitility.*;
import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.model.ArticleCategory;
import com.zawadz88.realestate.api.task.ArticleListDownloadTask;
import com.zawadz88.realestate.api.eventbus.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.api.TaskResult;
import de.greenrobot.event.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.Callable;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
@RunWith(RobolectricTestRunner.class)
public class ArticleListDownloadTaskTest {

	@Before
	public void setUp() throws Exception {
		Robolectric.getBackgroundScheduler().pause();
		Robolectric.getUiThreadScheduler().pause();
	}

	@Test
	@Config(reportSdk = 9)//we have to use SDK < Honeycomb since Asynctask.executeOnExecutor is not supported by Robolectric...
	public void testNormalFlow() throws Exception {

		final RealEstateApplication application = (RealEstateApplication) Robolectric.getShadowApplication().getApplicationContext();

		final EventListener eventListener = new EventListener();
		eventListener.register();
		ArticleListDownloadTask asyncTask = new ArticleListDownloadTask(ArticleCategory.POPULAR.getName(), 0);

		application.startTask(asyncTask);


		assertTrue("Task not started!", application.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX));

		Robolectric.runBackgroundTasks();

		Robolectric.runUiThreadTasks();
		await().until(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return !application.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX);
			}
		});

		assertTrue("Task did not succeed!", !application.getArticleEssentialListByCategory(ArticleCategory.POPULAR.getName()).isEmpty());

		await().until(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return TaskResult.SUCCESSFUL.equals(eventListener.getResult());
			}
		});


		assertTrue("Did not receive notification!", TaskResult.SUCCESSFUL.equals(eventListener.getResult()));

	}

	private static class EventListener {

		private TaskResult result;

		public void register() {
			EventBus.getDefault().register(this);
		}

		public void onEventMainThread(ArticleEssentialDownloadEvent ev) {
			result = ev.getResult();
		}

		private TaskResult getResult() {
			return result;
		}
	}
}
