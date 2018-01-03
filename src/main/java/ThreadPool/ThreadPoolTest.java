package ThreadPool;

import java.util.ArrayList;
import java.util.List;

public class ThreadPoolTest {

	public static void main(String[] args) {
		ThreadPool t = ThreadPoolManager.getThreadPool(4);
		List<Runnable> taskList = new ArrayList<Runnable>();
		for (int i = 0; i < 100; i++) {
			taskList.add(new Car());
		}
		t.execute(taskList);
		System.out.println(t);
		// 所有线程都执行完成才destory
		t.destroy();
		System.out.println(t);

	}

	// 任务类
	static class Car implements Runnable {
		private static volatile int i = 1;

		@Override
		public void run() {// 执行任务
			System.out.println("当前处理的线程是： " + Thread.currentThread().getName() + " 执行任务 ：Car" + (i++) + " 完成");
		}
	}
}
