package com.zawadz88.realestate.test;

import com.zawadz88.realestate.RealEstateApplication;
import com.zawadz88.realestate.api.DownloadTaskResultDelegate;
import com.zawadz88.realestate.api.task.BaseDownloadTask;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static com.zawadz88.realestate.test.RealEstateApplicationTest.ApplicationAssembler.application;
import static org.mockito.Mockito.*;

/**
 * Created: 22.01.14
 *
 * @author Zawada
 */
@RunWith(RobolectricTestRunner.class)
public class RealEstateApplicationTest {

	private ApplicationAssembler mApplicationAssembler;
	private RealEstateApplication mApplication;

	@Before
	public void setUp() {
		mApplicationAssembler = application();
		Robolectric.getBackgroundScheduler().pause();
		Robolectric.getUiThreadScheduler().pause();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testTaskGetsAddedToQueue() {
		//Given
		mApplication = mApplicationAssembler.build();
		BaseDownloadTask dummyTask = dummySuccessfulTask();

		//When
		mApplication.startTask(dummyTask);

		//Then
		thenApplication().isExecutingTaskWithTag(dummyTask.getTag());
	}

	@Test
	@Config(reportSdk = 9)//we have to use SDK < Honeycomb since Asynctask.executeOnExecutor is not supported by Robolectric...
	public void testSuccessfulTaskGetsRemovedFromQueue() {
		//Given
		mApplication = mApplicationAssembler.build();
		BaseDownloadTask dummyTask = dummySuccessfulTask();

		//When
		mApplication.startTask(dummyTask);
		startExecutors();

		//Then
		thenApplication().finishedExecutingTaskWithTag(dummyTask.getTag());
	}

	@Test
	@Config(reportSdk = 9)//we have to use SDK < Honeycomb since Asynctask.executeOnExecutor is not supported by Robolectric...
	public void testFailedTaskGetsRemovedFromQueue() {
		//Given
		mApplication = mApplicationAssembler.build();
		BaseDownloadTask dummyTask = dummyFailingTask();

		//When
		mApplication.startTask(dummyTask);
		startExecutors();

		//Then
		thenApplication().finishedExecutingTaskWithTag(dummyTask.getTag());
	}

	@Test
	@Config(reportSdk = 9)//we have to use SDK < Honeycomb since Asynctask.executeOnExecutor is not supported by Robolectric...
	public void testCancelledTaskGetsRemovedFromQueue() {
		//Given
		mApplication = mApplicationAssembler.build();
		BaseDownloadTask dummyTask = dummySuccessfulTask();

		//When
		mApplication.startTask(dummyTask);
		dummyTask.cancel(true);
		startExecutors();

		//Then
		thenApplication().finishedExecutingTaskWithTag(dummyTask.getTag());
	}

	@Test
	@Config(reportSdk = 9)//we have to use SDK < Honeycomb since Asynctask.executeOnExecutor is not supported by Robolectric...
	public void testApplicationGetsNotifiedOnSuccessfulTask() {
		//Given
		DownloadTaskResultDelegate dummyTaskResultDelegate = mock(DownloadTaskResultDelegate.class);
		mApplication = mApplicationAssembler.withTaskResultDelegate(dummyTaskResultDelegate).build();
		BaseDownloadTask dummyTask = dummySuccessfulTask();

		//When
		mApplication.startTask(dummyTask);
		startExecutors();

		//Then
		thenApplication().receivedOnlyOneSuccessfulTaskResult(dummyTaskResultDelegate);
	}

	@Test
	@Config(reportSdk = 9)//we have to use SDK < Honeycomb since Asynctask.executeOnExecutor is not supported by Robolectric...
	public void testApplicationGetsNotifiedOnFailedTask() {
		//Given
		DownloadTaskResultDelegate dummyTaskResultDelegate = mock(DownloadTaskResultDelegate.class);
		mApplication = mApplicationAssembler.withTaskResultDelegate(dummyTaskResultDelegate).build();
		BaseDownloadTask dummyTask = dummyFailingTask();

		//When
		mApplication.startTask(dummyTask);
		startExecutors();

		//Then
		thenApplication().receivedOnlyOneFailedTaskResult(dummyTaskResultDelegate);
	}

	@Test
	@Config(reportSdk = 9)//we have to use SDK < Honeycomb since Asynctask.executeOnExecutor is not supported by Robolectric...
	public void testApplicationGetsNotifiedOnCancelledTask() {
		//Given
		DownloadTaskResultDelegate dummyTaskResultDelegate = mock(DownloadTaskResultDelegate.class);
		mApplication = mApplicationAssembler.withTaskResultDelegate(dummyTaskResultDelegate).build();
		BaseDownloadTask dummyTask = dummySuccessfulTask();

		//When
		mApplication.startTask(dummyTask);
		dummyTask.cancel(true);
		startExecutors();

		//Then
		thenApplication().receivedOnlyOneCancelledTaskResult(dummyTaskResultDelegate);
	}

	/**
	 * Util method which tells Robolectric to start executing AsyncTasks
	 */
	private void startExecutors() {
		Robolectric.runBackgroundTasks();
		Robolectric.runUiThreadTasks();
	}

	private BaseDownloadTask dummySuccessfulTask() {
		BaseDownloadTask dummyTask = new BaseDownloadTask("DUMMY_TAG");
		return dummyTask;
	}

	private BaseDownloadTask dummyFailingTask() {
		BaseDownloadTask dummyTask = new BaseDownloadTask("DUMMY_TAG") {
			@Override
			protected void doInBackgroundSafe() throws Exception {
				throw new Exception("DUMMY_EXCEPTION");
			}
		};
		return dummyTask;
	}

	private ApplicationAssert thenApplication() {
		return new ApplicationAssert(mApplication);
	}

	public static class ApplicationAssembler {

		private RealEstateApplication application;

		public static ApplicationAssembler application() {
			return new ApplicationAssembler();
		}

		public ApplicationAssembler() {
			application = (RealEstateApplication) Robolectric.getShadowApplication().getApplicationContext();
		}

		public ApplicationAssembler withTaskResultDelegate(final DownloadTaskResultDelegate delegate) {
			application.setTaskResultDelegate(delegate);
			return this;
		}

		public RealEstateApplication build() {
			return application;
		}
	}

	public static class ApplicationAssert {
		private final RealEstateApplication application;

		public ApplicationAssert(final RealEstateApplication mApplication) {
			this.application = mApplication;
		}

		public ApplicationAssert isExecutingTaskWithTag(final String tagName) {
			Assert.assertTrue("Task not added to the queue!", application.isExecutingTask(tagName));
			return this;
		}

		public ApplicationAssert finishedExecutingTaskWithTag(final String tagName) {
			await().until(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return !application.isExecutingTask(tagName);
				}
			});
			return this;
		}

		public ApplicationAssert receivedOnlyOneSuccessfulTaskResult(final DownloadTaskResultDelegate dummyTaskResultDelegate) {
			verify(dummyTaskResultDelegate, times(1)).onTaskSuccessful(any(BaseDownloadTask.class));
			verifyNoMoreInteractions(dummyTaskResultDelegate);
			return this;
		}

		public ApplicationAssert receivedOnlyOneFailedTaskResult(final DownloadTaskResultDelegate dummyTaskResultDelegate) {
			verify(dummyTaskResultDelegate, times(1)).onTaskFailed(any(BaseDownloadTask.class), any(Exception.class));
			verifyNoMoreInteractions(dummyTaskResultDelegate);
			return this;
		}

		public ApplicationAssert receivedOnlyOneCancelledTaskResult(final DownloadTaskResultDelegate dummyTaskResultDelegate) {
			verify(dummyTaskResultDelegate, times(1)).onTaskCancelled(any(BaseDownloadTask.class));
			verifyNoMoreInteractions(dummyTaskResultDelegate);
			return this;
		}
	}

}
