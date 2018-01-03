package ThreadPool.ParkingLot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ParkingCars {
	//流动的车有
	private static final int NUMBER_OF_CARS = 50;
    //停车位
	private static final int NUMBER_OF_PARKING_SLOT = 30;

	public static void main(String[] args) {

		/**
		 * 采用FIFO：先入先出队列 设置true
		 */
		Semaphore parkingSlot = new Semaphore(NUMBER_OF_PARKING_SLOT, true);
		/**
		 * 创建固定大小的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小。
		 * 线程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充一个新线程。
		 * newCachedThreadPool：创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，
		 * 那么就会回收部分空闲（60秒不执行任务）的线程，当任务数增加时，此线程池又可以智能的添加新线程来处理任务。
		 * 此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者说JVM）能够创建的最大线程大小。
		 */
		ExecutorService service = Executors.newCachedThreadPool();

		for (int carNo = 1; carNo <= NUMBER_OF_CARS; carNo++) {
			service.execute(new Car(parkingSlot, carNo));
		}

		sleep(3000);

		service.shutdown();

		/*
		 * 输出还有几个可以用的资源数
		 */
		System.out.println(parkingSlot.availablePermits() + " 个停车位可以用!");
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
