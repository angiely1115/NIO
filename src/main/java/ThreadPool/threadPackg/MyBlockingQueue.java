package ThreadPool.threadPackg;
import java.util.concurrent.BlockingQueue;  

import java.util.concurrent.ExecutorService;  
  
import java.util.concurrent.Executors;  
  
import java.util.concurrent.LinkedBlockingQueue;  
  /**
   * BlockingQueue支持两个附加操作的 Queue，这两个操作是：检索元素时等待队列变为非空，以及存储元素时等待空间变得可用。
	BlockingQueue 不接受 null 元素。试图 add、put 或 offer 一个 null 元素时，
	某些实现会抛出 NullPointerException。null 被用作指示 poll 操作失败的警戒值。
	BlockingQueue 可以是限定容量的。它在任意给定时间都可以有一个 remainingCapacity，
	超出此容量，便无法无阻塞地 put 额外的元素。
	没有任何内部容量约束的 BlockingQueue 总是报告 Integer.MAX_VALUE 的剩余容量。
	BlockingQueue 实现主要用于生产者-使用者队列，但它另外还支持 Collection 接口。因此，举例来说，
	使用 remove(x) 从队列中移除任意一个元素是有可能的。
	然而，这种操作通常不 会有效执行，只能有计划地偶尔使用，比如在取消排队信息时。
	BlockingQueue实现是线程安全的。所有排队方法都可以使用内部锁定或其他形式的并发控制来自动达到它们的目的。
	然而，大量的 Collection 操作（addAll、containsAll、retainAll 和 removeAll）没有 必要自动执行，除非在实现中特别说明。
	因此，举例来说，在只添加了 c 中的一些元素后，addAll(c) 有可能失败（抛出一个异常）。
	BlockingQueue 实质上不 支持使用任何一种“close”或“shutdown”操作来指示不再添加任何项。
	这种功能的需求和使用有依赖于实现的倾向。例如，一种常用的策略是：对于生产者，插入特殊的 end-of-stream 或 poison 对象，
	并根据使用者获取这些对象的时间来对它们进行解释。  
   * @author sam
   *
   */
public class MyBlockingQueue extends Thread {  
  /**
   * LinkedBlockingQueue一个基于已链接节点的、范围任意的 blocking queue。
   *  此队列按 FIFO（先进先出）排序元素。队列的头部 是在队列中时间最长的元素。
   *  队列的尾部 是在队列中时间最短的元素。新元素插入到队列的尾部，并且队列获取操作会获得位于队列头部的元素。
   *  链接队列的吞吐量通常要高于基于数组的队列，但是在大多数并发应用程序中，其可预知的性能要低。
   *  可选的容量范围构造方法参数作为防止队列过度扩展的一种方法。
   *  如果未指定容量，则它等于 Integer.MAX_VALUE。除非插入节点会使队列超出容量，
   *  否则每次插入后会动态地创建链接节点。 
   */
    public static BlockingQueue<String> queue = new LinkedBlockingQueue<String>(3);  
    private int index;  
  
    public MyBlockingQueue(int i) {  
        this.index = i;  
    }  
  
    public void run() {  
        try {  
            queue.put(String.valueOf(this.index));  
            System.out.println("{" + this.index + "} in queue!");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static void main(String args[]) {  
        ExecutorService service = Executors.newCachedThreadPool();  
        for (int i = 0; i < 10; i++) {  
            service.submit(new MyBlockingQueue(i));  
        }  
  
        Thread thread = new Thread() {  
            public void run() {  
                try {  
                    while (true) {  
                        Thread.sleep((int) (Math.random() * 1000));  
                        if (MyBlockingQueue.queue.isEmpty())  
                            break;  
                        String str = MyBlockingQueue.queue.take();  
                        System.out.println(str + " has take!");  
                    }  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        };  
        service.submit(thread);  
        service.shutdown();  
    }  
  
}  