package ThreadPool.threadPackg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class MySemaphore extends Thread {
	private Semaphore position;  
    private int id;  
  
    public MySemaphore(int i, Semaphore s) {  
        this.id = i;  
        this.position = s;  
    }  
	@Override
	public void run() {
		 try {  
	            //position.availablePermits()  :返回此信号量中当前可用的许可数。
	            if (position.availablePermits() > 0) {  
	                System.out.println("顾客[" + this.id + "]进入厕所，有空位");  
	            }else {  
	                System.out.println("顾客[" + this.id + "]进入厕所，没空位，排队");  
	            }  
	            //position.acquire():从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断。
	            position.acquire();  
	            System.out.println("顾客[" + this.id + "]获得坑位");  
	            //使用中...  
	            Thread.sleep((int) (Math.random() * 1000));  
	            System.out.println("顾客[" + this.id + "]使用完毕");  
	            //厕所使用完之后释放  
	            position.release();  
	        }catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	}
	 public static void main(String args[]) { 
		   /**
		    * ③newCacheThreadExecutor（推荐使用）
		    * 可缓存线程池，当线程池大小超过了处理任务所需的线程，
		    * 那么就会回收部分空闲（一般是60秒无执行）的线程，当有任务来时，又智能的添加新线程来执行。
		    */
	        ExecutorService list = Executors.newCachedThreadPool();  
	        Semaphore position = new Semaphore(4);//只有4个厕所  
	        //有十个人  
	        for (int i = 0; i < 20; i++) { 
	        	//Future<?> submit(Runnable task) 
	            //提交一个 Runnable 任务用于执行，并返回一个表示该任务的 Future。 
	            list.submit(new MySemaphore(i + 1, position));  
	        }  
	        list.shutdown(); 
	        /**
	         * void acquireUninterruptibly(int permits) 
                                 从此信号量获取给定数目的许可，在提供这些许可前一直将线程阻塞。 
	         */
	        position.acquireUninterruptibly(4);  
	        System.out.println("使用完毕，需要清扫了");  
	        position.release(4);  
	    }  
}
