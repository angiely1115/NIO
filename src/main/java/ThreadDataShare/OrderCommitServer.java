package ThreadDataShare;

import java.util.Random;

/**
 * 服务类 他接受客端提交过来的订单
 * 
 * @author chenjg
 *
 */
public class OrderCommitServer {
	private static int money;

	public static void main(String[] args) {
		// 承受成千上万个并发线程请求 每一个线程是维护一个订单
		for (int i = 0; i < 4; i++) {
			new Thread(new Runnable() {
				//
				public void run() {
					// 指定锁是同一个对象 看到的对象是不一样 重锁的方式 效率非常低下
					synchronized (OrderCommitServer.class) {
						// 具体业务 每一个线程过来经过服务器中多个模块做处理 并发 线程在这里挂起
						money = new Random().nextInt(10000);
						System.out
								.println("当前处理客户端的线程是：" + Thread.currentThread().getName() + "后台显示订单" + "金额为：" + money);
						new A_Mode().getOrderMoney();
						new B_Mode().getOrderMoney();
					}
				}
			}).start();
		}

	}

	// 仓库查询模块 A
	static class A_Mode {
		public void getOrderMoney() {// 线程一 携带的内容是8319 挂起 线程3 进来之后 3745 挂起
			System.out.println(Thread.currentThread().getName() + "进入A模块处理完毕之后，当前金额为：" + money);
		}
	}

	// 账号余额模块 这里直接查对接三方接口 B
	static class B_Mode {
		public void getOrderMoney() {
			System.out.println(Thread.currentThread().getName() + "进入B模块处理完毕之后，当前金额为：" + money);
		}
	}
}
