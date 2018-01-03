package ThreadPool;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolManager implements ThreadPool {
	// 线程池中默认线程的个数为5
	private static int workerNum = 5;
	private static int maxWorkernun = 150;
	// 工作线程数组
	WorkThread[] workThrads;
	// 在执行的任务数量 volatile
	private static volatile int executeTaskNumber = 0;
	// 任务队列，作为一个缓冲,List线程不安全
	private BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
	private static ThreadPoolManager threadPool;
	private AtomicLong threadNum = new AtomicLong();

	// 线程池参数配置
	private ThreadPoolManager() {
		this(workerNum);
	}

	// 配置线程池默认大小 最大数量 外部读取进来的workerNum2
	private ThreadPoolManager(int workerNum2) {
		if (workerNum2 > 0) {
			workerNum = workerNum2;
		}
		// 工作人员车间 5 个收费管理员 5个为位置
		workThrads = new WorkThread[workerNum];

		for (int i = 0; i < workerNum; i++) {
			// 每个空间的对象初始化
			workThrads[i] = new WorkThread();
			workThrads[i].setName("ThreadPool-worker" + threadNum.incrementAndGet());
			System.out.println("初始化线数: " + (i + 1) + "/" + (workerNum) + "  ----当前线程名称是：" + workThrads[i].getName());
			workThrads[i].start();
		}
	}

	// 实现源码当中 如何提交一个线程最大int类型数量返回一个线程池
	public static ThreadPool getThreadPool() {
		return getThreadPool(ThreadPoolManager.workerNum);
		// Executors 工厂方式获取一个线程池 100
		// Executors.newFixedThreadPool(100);
	}
    //自定义的方法获取线程池
	public static ThreadPool getThreadPool(int workerNum) {
		if (workerNum <= 0) {
			workerNum = ThreadPoolManager.workerNum;
		}
		if (threadPool == null) {
			threadPool = new ThreadPoolManager(workerNum);
		}
		return threadPool;
	}

	// 接收任务
	@Override
	public void execute(Runnable task) {
		synchronized (taskQueue) {
			try {
				taskQueue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			taskQueue.notifyAll();
		}

	}

	@Override
	public String toString() {
		return "当前工作线程数量为:" + workerNum + "  已经完成任务数:" + executeTaskNumber + "  等待任务数:" + getWaitTaskNumber();
	}

	@Override
	public void execute(Runnable[] tasks) {
		synchronized (taskQueue) {
			for (Runnable task : tasks) {
				try {
					taskQueue.put(task);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			taskQueue.notifyAll();
		}

	}

	@Override
	public void execute(List<Runnable> tasks) {
		synchronized (taskQueue) {
			for (Runnable task : tasks) {
				try {
					taskQueue.put(task);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			taskQueue.notifyAll();
		}

	}

	@Override
	public int getExecuteTaskNumber() {
		// TODO Auto-generated method stub
		return executeTaskNumber;
	}

	@Override
	public int getWaitTaskNumber() {
		// TODO Auto-generated method stub
		return taskQueue.size();
	}

	@Override
	public int getWorkThreadNumber() {
		// TODO Auto-generated method stub
		return workerNum;
	}

	// 定义一个工作线程类型 定义这个停车场的收费管理员 （多个）处理多台泊车（Task）
	private class WorkThread extends Thread {
		// 是否应该休息
		private boolean isRuning = true;

		@Override
		public void run() {
			// 接受每一个任务对象（车）
			Runnable car = null;
			while (isRuning) {
				// 排队准备收费的队列进行同步 杆子就相当于一个同步
				synchronized (taskQueue) {
					while (isRuning && taskQueue.isEmpty()) {
						try {
							taskQueue.wait(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					if (!taskQueue.isEmpty()) {
						try {
							// 取出队列首元素并且删除取出的元素
							car =taskQueue.take();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (car != null) {
						// 这就代表当前汽车是一个真的
						car.run();
					}
					executeTaskNumber++;
					car = null;
				}
			}

		}

		// 收费线程如何停止工作
		public void stopWorker() {
			isRuning = false;
		}

	}

	// 对线程池任务控制窗口
	@Override
	public void destroy() {
		while (!taskQueue.isEmpty()) {
			// 不断的检查到任务队列当中存在任务
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < workerNum; i++) {
			workThrads[i].stopWorker();
			workThrads[i] = null;
		}
	}

}
