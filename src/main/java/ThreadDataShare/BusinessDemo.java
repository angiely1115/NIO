package ThreadDataShare;
/**
 * 厕所类（）
 * @author Sam
 *
 */
public class BusinessDemo {
	//内部有一个控制属 开关
	private boolean isShowSonThred=true;
	//子线程先进来 搞事
	public synchronized void sonBusiness(int i){
		while(!isShowSonThred){
			try {
				//子线程就在外面等着了 死了 
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//代表厕所没人
		for(int j=1;j<=30;j++){
			System.out.println("  ==============子线程运行第： " + i+ "轮，第： " + j + " 次！");
		}
		//厕所门打开
		isShowSonThred = false;
		//通知主线程这个哥们
		this.notify();

	}
 //主线程业务模块 synchronized隐式锁  锁对象 封装到里面 不好控制 效率非常低下
	//synchronized 时间换线程安全 （线程空间）
	public synchronized void mainBusiness(int i){
		while(isShowSonThred){
			try {
				//主线程就在外面等着了
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//代表厕所没人 主线程就去做如下业务
		for(int j=1;j<=40;j++){
			System.out.println("主线程运行第： " + i+ "轮，第： " + j + " 次！");
		}
		//厕所门打开
		isShowSonThred = true;
		//通知子线
		this.notify();
	}
}
