package com.zawadz88.realestate.test;

import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.TaskResult;
import com.zawadz88.realestate.api.eventbus.ArticleEssentialDownloadEvent;
import com.zawadz88.realestate.api.model.ArticleCategory;
import com.zawadz88.realestate.api.task.ArticleListDownloadTask;
import de.greenrobot.event.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import retrofit.RetrofitError;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created: 17.11.13
 *
 * @author Zawada
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
public class ArticleListDownloadTaskTest {

	public static final int FIRST_PAGE_NUMBER = 0;
	public static final RetrofitError NETWORK_ERROR = RetrofitError.networkError("UNKNOWN", null);

	private RealEstateApplication mApplication;
	private EventListener mEventListener;
	private ArticleCategory mCategory;

	private DownloadAssert downloadAssert;

	public ArticleListDownloadTaskTest(final String categoryName) {
		this.mCategory = ArticleCategory.valueOf(categoryName);
	}

	@ParameterizedRobolectricTestRunner.Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { ArticleCategory.NEWEST.name() }, { ArticleCategory.POPULAR.name() }, { ArticleCategory.LOANS.name() }, { ArticleCategory.TIPS.name() } };
		return Arrays.asList(data);
	}

	@Before
	public void setUp() throws Exception {
		Robolectric.getBackgroundScheduler().pause();
		Robolectric.getUiThreadScheduler().pause();
		realEstateApplication();
		registeredEventListener();
	}

	@After
	public void tearDown() {
		  mEventListener.unregister();
	}

	@Test
	@Config(reportSdk = 9)//we have to use SDK < Honeycomb since Asynctask.executeOnExecutor is not supported by Robolectric...
	public void testGetFirstPageOfArticlesSuccessfully() throws Exception {
		//Given
		ArticleListDownloadTask asyncTask = downloadTask();

		//When
		addTaskToExecutionQueue(asyncTask);

		//Then
		thenDownload().isExecutingForCategory();

		startExecutingTasks();

		thenDownload().finishedExecuting().fetchedArticles().emittedTaskSuccessfulEvent();
	}

	@Test
	@Config(reportSdk = 9)//we have to use SDK < Honeycomb since Asynctask.executeOnExecutor is not supported by Robolectric...
	public void testFailedDownloadEmitsFailedTaskResult() throws Exception {
		//Given
		ArticleListDownloadTask asyncTask = errorThrowingDownloadTask();
		//When
		addTaskToExecutionQueue(asyncTask);

		//Then
		startExecutingTasks();
		thenDownload().finishedExecuting().didNotFetchArticles().emittedTaskFailedEvent();
	}

	private DownloadAssert thenDownload() {
		return new DownloadAssert(mCategory, mApplication, mEventListener);
	}


	private void addTaskToExecutionQueue(final ArticleListDownloadTask asyncTask) {
		mApplication.startTask(asyncTask);
	}

	/**
	 * Util method telling Robolectric to start executing AsyncTasks which were previously paused
	 */
	private void startExecutingTasks() {
		Robolectric.runBackgroundTasks();
		Robolectric.runUiThreadTasks();
	}

	private void registeredEventListener() {
		mEventListener = new EventListener();
		mEventListener.register();
	}

	private ArticleListDownloadTask downloadTask() {
		return new ArticleListDownloadTask(mCategory.getName(), FIRST_PAGE_NUMBER);
	}

	private ArticleListDownloadTask errorThrowingDownloadTask() {
		ArticleListDownloadTask asyncTask = downloadTask();
		ArticleListDownloadTask.ArticleListService mockService = mock(ArticleListDownloadTask.ArticleListService.class);
		when(mockService.getArticleEssentialList(anyString(), anyInt())).thenThrow(NETWORK_ERROR);
		asyncTask.setService(mockService);
		return asyncTask;
	}

	private void realEstateApplication() {
		mApplication = (RealEstateApplication) Robolectric.getShadowApplication().getApplicationContext();
	}

	public static class DownloadAssert {
		private final ArticleCategory category;
		private final RealEstateApplication application;
		private final EventListener eventListener;

		public DownloadAssert(final ArticleCategory category, final RealEstateApplication application, final EventListener eventListener) {
			this.category = category;
			this.application = application;
			this.eventListener = eventListener;
		}

		public DownloadAssert isExecutingForCategory() {
			assertTrue("Task not executing!", application.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX + category.getName()));
			return this;
		}

		public DownloadAssert finishedExecuting() {
			await().until(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return !application.isExecutingTask(RealEstateApplication.DOWNLOAD_ARTICLE_ESSENTIAL_LIST_TAG_PREFIX + category.getName());
				}
			});
			return this;
		}

		public DownloadAssert fetchedArticles() {
			assertTrue("Task did not succeed!", !application.getArticleEssentialListByCategory(category.getName()).isEmpty());
			return this;
		}

		public DownloadAssert didNotFetchArticles() {
			assertTrue("Task did not succeed!", application.getArticleEssentialListByCategory(category.getName()).isEmpty());
			return this;
		}

		public DownloadAssert emittedTaskSuccessfulEvent() {
			await().until(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return TaskResult.SUCCESSFUL.equals(eventListener.getResult());
				}
			});
			assertTrue("Did not receive notification!", TaskResult.SUCCESSFUL.equals(eventListener.getResult()));
			return this;
		}

		public DownloadAssert emittedTaskFailedEvent() {
			await().until(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return TaskResult.FAILED.equals(eventListener.getResult());
				}
			});
			assertTrue("Did not receive notification of emittedTaskFailedEvent task!", TaskResult.FAILED.equals(eventListener.getResult()));
			return this;
		}
	}

	/**
	 * Helper listener that is to listen for emitted EventBus' ArticleEssentialDownloadEvent events
	 */
	private static class EventListener {

		private TaskResult result;

		public void register() {
			EventBus.getDefault().register(this);
		}

		public void unregister() {
			EventBus.getDefault().unregister(this);
		}

		public void onEventMainThread(ArticleEssentialDownloadEvent ev) {
			result = ev.getResult();
		}

		private TaskResult getResult() {
			return result;
		}
	}

}
