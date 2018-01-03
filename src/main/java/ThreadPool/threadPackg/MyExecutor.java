package ThreadPool.threadPackg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutor extends Thread{

    public void run() {  
        try {  
            System.out.println("[" +Thread.currentThread().getName() + "] start....");  
            Thread.sleep((int) (Math.random() * 10000));  
            System.out.println("[" + Thread.currentThread().getName()+ "] end.");  
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static void main(String args[]) {  
        ExecutorService service = Executors.newFixedThreadPool(4);  
        for (int i = 0; i < 10; i++) {  
            service.execute(new MyExecutor());  
            // service.submit(new MyExecutor(i));  
        }  
        System.out.println("submit finish");  
        service.shutdown();  
    }  
}
