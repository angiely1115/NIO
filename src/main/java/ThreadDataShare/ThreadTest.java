package ThreadDataShare;

/**
 * 
 * 有一个这样场景 IBM以前一道非常经典的线程面试 要求： 子线程 循环跑30次，暂停，然后转到主线程跑40次，接着 子线程
 * 循环跑30次，暂停，然后转到主线程跑40次 如此往复 一共这样交替50次。
 * 
 * @author Sam
 *
 */
public class ThreadTest {
	public static void main(String[] args) {
		// 子线程部分要求：是50轮换 每个轮换循环跑30次
		new Thread(new Runnable() {

			public void run() {
				for (int i = 1; i <= 50; i++) {
					// 子线程要搞的业务
					for (int j = 1; j <= 30; j++) {
						System.out.println("  ==============子线程运行第： " + i
								+ "轮，第： " + j + " 次！");
					}
				}

			}
		}).start();

		// 50轮回 主线程部分
		for (int i = 1; i <= 50; i++) {
			//  主线程要搞的业务
			for (int j = 1; j <= 40; j++) {
				System.out.println("主线程运行第  ： " + i + "轮，第： " + j + " 次！");
			}

		}
		
	}
}