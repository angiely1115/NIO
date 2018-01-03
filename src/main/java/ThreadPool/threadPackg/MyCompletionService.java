package ThreadPool.threadPackg;

/**
 * CompletionService我们现在在Java中使用多线程通常不会直接用Thread对象了，
 * 而是会用到java.util.concurrent包下的ExecutorService类来初始化一个线程池供我们使用。
 * 之前我一直习惯自己维护一个list保存submit的callable task所返回的Future对象。
 * 在主线程中遍历这个list并调用Future的get()方法取到Task的返回值。
 * 但是，我在很多地方会看到一些代码通过CompletionService包装ExecutorService，然后调用其take()方法去取Future对象。
 * 以前没研究过这两者之间的区别。今天看了源代码之后就明白了。
 * 这两者最主要的区别在于submit的task不一定是按照加入自己维护的list顺序完成的。
 * 从list中遍历的每个Future对象并不一定处于完成状态，这时调用get()方法就会被阻塞住，
 * 如果系统是设计成每个线程完成后就能根据其结果继续做后面的事，这样对于处于list后面的但是先完成的线程就会增加了额外的等待时间。
 * 而CompletionService的实现是维护一个保存Future对象的BlockingQueue。
 * 只有当这个Future对象状态是结束的时候，才会加入到这个Queue中，take()方法其实就是Producer-Consumer中的Consumer。
 * 它会从Queue中取出Future对象，如果Queue是空的，就会阻塞在那里，直到有完成的Future对象加入到Queue中。
 * 所以，先完成的必定先被取出。这样就减少了不必要的等待时间。ExecutorCompletionService 类提供了此方法的一个实现。
 */
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyCompletionService implements Callable<String> {
	private int id;

	public MyCompletionService(int i) {
		this.id = i;
	}
	@Override
	public String call() throws Exception {
		 Integer time = (int) (Math.random() * 1000);  
	        try {  
	            System.out.println("id为      " +this.id + " 业务开始.....");  
	            Thread.sleep(time);  
	            System.out.println("id为      " +this.id + " 业务处理完成!");  
	        }catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return this.id + ":" + time;  
	}

	public static void main(String[] args) throws Exception {
		/**
		 * Executors
newFixedThreadPool（固定大小线程池）
创建一个可重用固定线程集合的线程池，以共享的无界队列方式来运行这些线程（只有要请求的过来，就会在一个队列里等待执行）。
如果在关闭前的执行期间由于失败而导致任何线程终止，那么一个新线程将代替它执行后续的任务（如果需要）。

newCachedThreadPool（无界线程池，可以进行自动线程回收）
创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
对于执行很多短期异步任务的程序而言，这些线程池通常可提高程序性能。调用 execute 将重用以前构造的线程（如果线程可用）。
如果现有线程没有可用的，则创建一个新线程并添加到池中。终止并从缓存中移除那些已有 60 秒钟未被使用的线程。
因此，长时间保持空闲的线程池不会使用任何资源。注意，可以使用 ThreadPoolExecutor 构造方法创建具有类似属性但细节不同（例如超时参数）的线程池。

newSingleThreadExecutor（单个后台线程）
创建一个使用单个 worker 线程的 Executor，以无界队列方式来运行该线程。
（注意，如果因为在关闭前的执行期间出现失败而终止了此单个线程，那么如果需要，一个新线程将代替它执行后续的任务）。
可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。与其他等效的 newFixedThreadPool(1) 不同，
可保证无需重新配置此方法所返回的执行程序即可使用其他的线程。
		 */
		ExecutorService service = Executors.newCachedThreadPool();
		CompletionService<String> completion = new ExecutorCompletionService<String>(service);
		for (int i = 0; i < 10; i++) {
			completion.submit(new MyCompletionService(i));
		}

		for (int i = 0; i < 10; i++) {
			String[] future =completion.take().get().split(":");
			System.out.println("拿到id为      "+future[0]+" 花费时间为："+future[1]);
		}
		service.shutdown();
	}

	


}
