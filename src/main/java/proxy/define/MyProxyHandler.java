package proxy.define;

import java.lang.reflect.Method;
/**
 * MyProxyHandler：调用器
 */
import proxy.People;

public class MyProxyHandler implements MyInvocationHandler {
    People h = null;
    public MyProxyHandler (People h) {
		this.h =h;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object obj)
			throws Throwable {
		before();
		//反射执行方法
		//JVM 当中隐式能找到这个接口对象的实现类
		method.invoke(h, null);
		after();
		return null;
	}
	//前置增强加入
		private void before(){
			System.out.println("自定义动态代理前置增强：要做饭了，你喜欢吃什么？");
		}
		//后置增强
			private void after(){
				System.out.println("自定义动态代理后置增强：吃完了，该收拾一下了！");
			}
}
