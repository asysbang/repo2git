package repo2git;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloneExecutor {

	private ExecutorService newFixedThreadPool;

	private Runnable task;

	private static CloneExecutor sInstance;

	private CloneExecutor() {
		init();
	}

	public void init() {
		newFixedThreadPool = Executors.newFixedThreadPool(10);
	}

	public static CloneExecutor getInstance() {
		if (sInstance == null) {
			sInstance = new CloneExecutor();
		}
		return sInstance;
	}

	public void setTaskList() {
		newFixedThreadPool.submit(new CloneRunnable("aaa"));
	}

	private class CloneRunnable implements Runnable {

		private String mCmd;

		CloneRunnable(String cmd) {
			mCmd = cmd;
		}

		@Override
		public void run() {
			System.out.println("=======" + mCmd);
		}

	}

}
