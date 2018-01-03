package ThreadPool.threadPackg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
/**
 * ReentrantLock
     一个可重入的互斥锁定 Lock，它具有与使用 synchronized 方法和语句所访问的隐式监视器锁定相同的一些基本行为和语义，
     但功能更强大。他们两个的区别见：http://blog.csdn.net/fw0124/article/details/6672522
  ReentrantLock 将由最近成功获得锁定，并且还没有释放该锁定的线程所拥有。
     当锁定没有被另一个线程所拥有时，调用 lock 的线程将成功获取该锁定并返回。
    如果当前线程已经拥有该锁定，此方法将立即返回。
    可以使用 isHeldByCurrentThread() 和 getHoldCount() 方法来检查此情况是否发生。
     此类的构造方法接受一个可选的公平参数。
    当设置为 true时，在多个线程的争用下，
    这些锁定倾向于将访问权授予等待时间最长的线程。否则此锁定将无法保证任何特定访问顺序。
     与采用默认设置（使用不公平锁定）相比，使用公平锁定的程序在许多线程访问时表现为很低的总体吞吐量（即速度很慢，常常极其慢），
     但是在获得锁定和保证锁定分配的均衡性时差异较小。不过要注意的是，公平锁定不能保证线程调度的公平性。
    因此，使用公平锁定的众多线程中的一员可能获得多倍的成功机会，这种情况发生在其他活动线程没有被处理并且目前并未持有锁定时。
    还要注意的是，未定时的 tryLock 方法并没有使用公平设置。因为即使其他线程正在等待，只要该锁定是可用的，此方法就可以获得成功。
     建议总是 立即实践，使用 try 块来调用 lock，在之前/之后的构造中，最典型的代码如下：
 * @author sam
 *
 */
public class MyReentrantLock extends Thread {
	TestReentrantLock lock;  
    private int id;  
  
    public MyReentrantLock(int i, TestReentrantLock test) {  
        this.id = i;  
        this.lock = test;  
    }  
  
    public void run() {  
        lock.print(id);  
    }  
     /**
      * ①newSingleThreadExecutor
		单个线程的线程池，即线程池中每次只有一个线程工作，单线程串行执行任务
		②newFixedThreadExecutor(n)
		固定数量的线程池，没提交一个任务就是一个线程，直到达到线程池的最大数量，然后后面进入等待队列直到前面的任务完成才继续执行
		③newCacheThreadExecutor（推荐使用）
		可缓存线程池，当线程池大小超过了处理任务所需的线程，那么就会回收部分空闲（一般是60秒无执行）的线程，当有任务来时，又智能的添加新线程来执行。
		④newScheduleThreadExecutor
		大小无限制的线程池，支持定时和周期性的执行线程
      * @param args
      */
    public static void main(String args[]) {  
        ExecutorService service = Executors.newCachedThreadPool(); 
        TestReentrantLock lock = new TestReentrantLock();  
        for (int i = 0; i < 20; i++) {  
            service.submit(new MyReentrantLock(i, lock));  
        }  
        service.shutdown();  
    }  
  
}  
 /**
  * ReentrantLock 将由最近成功获得锁，并且还没有释放该锁的线程所拥有。当锁没有被另一个线程所拥有时，
  * 调用 lock 的线程将成功获取该锁并返回。如果当前线程已经拥有该锁，此方法将立即返回。
  * 可以使用 isHeldByCurrentThread() 和 getHoldCount() 方法来检查此情况是否发生。 
  * 此类的构造方法接受一个可选的公平 参数。当设置为 true 时，在多个线程的争用下，
  * 这些锁倾向于将访问权授予等待时间最长的线程。否则此锁将无法保证任何特定访问顺序。
  * 与采用默认设置（使用不公平锁）相比，使用公平锁的程序在许多线程访问时表现为很低的总体吞吐量（即速度很慢，常常极其慢），
  * 但是在获得锁和保证锁分配的均衡性时差异较小。不过要注意的是，公平锁不能保证线程调度的公平性。
  * 因此，使用公平锁的众多线程中的一员可能获得多倍的成功机会，这种情况发生在其他活动线程没有被处理并且目前并未持有锁时。
  * 还要注意的是，未定时的 tryLock 方法并没有使用公平设置。
  * 因为即使其他线程正在等待，只要该锁是可用的，此方法就可以获得成功。 
  * 建议总是 立即实践，使用 lock 块来调用 try，在之前/之后的构造中，最典型的代码如下： 
  * @author Sam
  *
  */
class TestReentrantLock {  
    private ReentrantLock lock = new ReentrantLock();  
    public void print(int str) {  
        try {  
            lock.lock();  
            System.out.println(str + "获得");  
            Thread.sleep((int) (Math.random() * 1000));  
        }catch (Exception e) {  
            e.printStackTrace();  
        }finally {  
            System.out.println(str + "释放");  
            lock.unlock();  
        }  
    }  
  
}
