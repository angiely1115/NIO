package ThreadPool.ParkingLot;

import java.util.concurrent.Semaphore;

public class Car implements Runnable {
	/**
	 * 一个计数信号量。从概念上讲，信号量维护了一个许可集。如有必要，在许可可用前会阻塞每一个 acquire()，
	 * 然后再获取该许可。每个 release() 添加一个许可，从而可能释放一个正在阻塞的获取者。
	 * 但是，不使用实际的许可对象，Semaphore 只对可用许可的号码进行计数，并采取相应的行动。
	 */
	private final Semaphore parkingSlot;  
	  
    private int carNo;  
 
    /** 
    * @param parkingSlot 
    * @param carName 
    */  
    public Car(Semaphore parkingSlot, int carNo) {  
         this.parkingSlot = parkingSlot;  
         this.carNo = carNo;  
    }  
 
    public void run() {  
 
         try {  
              parkingSlot.acquire();  
              parking();  
              sleep(300);  
              parkingSlot.release();  
              leaving();  
 
         } catch (InterruptedException e) {  
              // TODO Auto-generated catch block  
              e.printStackTrace();  
         }  
 
    }  
    //泊车
    private void parking() {  
         System.out.println(String.format("%d号车泊车", carNo));  
    }  
 
    private void leaving() {  
         System.out.println(String.format("%d号车离开车位", carNo));  
    }  
 
    private static void sleep(long millis) {  
         try {  
              Thread.sleep(millis);  
         } catch (InterruptedException e) {  
              // TODO Auto-generated catch block  
              e.printStackTrace();  
 
         }  
    }  
}
