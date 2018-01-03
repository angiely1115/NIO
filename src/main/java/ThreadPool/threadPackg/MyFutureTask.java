package ThreadPool.threadPackg;

import java.util.concurrent.Callable;  
import java.util.concurrent.ExecutionException;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.Future;  
/**
 * Future
  Future 表示异步计算的结果。它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。
     计算完成后只能使用 get 方法来检索结果，如有必要，计算完成前可以阻塞此方法。取消则由 cancel 方法来执行。
    还提供了其他方法，以确定任务是正常完成还是被取消了。一旦计算完成，就不能再取消计算。
     如果为了可取消性而使用 Future但又不提供可用的结果，则可以声明 Future<?> 形式类型、并返回 null 作为基础任务的结果。
     这个我们在前面CompletionService已经看到了，这个Future的功能，而且这个可以在提交线程的时候被指定为一个返回对象的。
 * @author lenovo
 *
 */
public class MyFutureTask {  
    /** 
     * @param args 
     * @throws InterruptedException  
     * @throws ExecutionException 
     * @throws InterruptedException 
     * @throws ExecutionException  
     */  
    public static void main(String[] args) throws InterruptedException, ExecutionException {  
          
        final ExecutorService exe=Executors.newFixedThreadPool(3);  
        Callable<String> call=new Callable<String>(){  
            public String call() throws InterruptedException {  
                return "Thread is finished";  
            }  
        };  
        Future<String> task=exe.submit(call);  
        String obj=task.get();  
        System.out.println(obj+"进程结束");  
        System.out.println("总进程结束");  
        exe.shutdown();  
    }  
}  
class MyThreadTest implements Runnable {  
    private String str;  
    public MyThreadTest(String str) {  
        this.str = str;  
    }  
    public void run() {  
        this.setStr("allen"+str);  
    }  
    public void addString(String str) {  
        this.str = "allen:" + str;  
    }  
    public String getStr() {  
        return str;  
    }  
    public void setStr(String str) {  
        this.str = str;  
    }  
}  