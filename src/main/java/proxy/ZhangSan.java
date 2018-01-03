package proxy;

public class ZhangSan implements People {

   /**
    * 核心方法
    */
	public void eat() throws Throwable {
		System.out.println("吃饭的时候喜欢看《JAVA高级开发》");
	}


	public void sleep() throws Throwable {

		System.out.println("睡觉的时候喜欢磨牙！");
	}


	public void dadoudou() throws Throwable {
       System.out.println("喜欢和美女一起打豆豆！");
	}

}