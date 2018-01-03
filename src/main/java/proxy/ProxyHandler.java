package proxy;
//InvocationHandler：调用处理器
import java.lang.reflect.InvocationHandler;

import java.lang.reflect.Method;
//这里可以代理任何接口口的任何方法
public class ProxyHandler implements InvocationHandler {
    //传入接口对象
	People people= null;
	public ProxyHandler(People people) {
		this.people = people;
	}
	/**
	 * 所有代理被拦截方法 统一会来Invoke
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		//前置扩展模块
		before();
		//反射核心业务方法
		//实际上就是代理对象携带过来的对象
		method.invoke(people, null);
		after();
		return null;
	}
	//前置增强加入
	private void before(){
		System.out.println("要做饭了，你喜欢吃什么？");
	}
	//后置增强
		private void after(){
			System.out.println("吃完了，该收拾一下了！");
		}
}
