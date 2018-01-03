package proxy.define;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import proxy.People;
import proxy.ProxyHandler;
import proxy.ZhangSan;

public class MyTest {

	public static void main(String[] args) {
		System.out.println("===========自定义中的jdk方法AOP编程================");
		try {
			People people = (People) MyProxy.createProxyInstance(
					People.class.getClassLoader(), People.class,
					new MyProxyHandler(new ZhangSan()));

			try {
				people.eat();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("动态代理类："+People.class.getName());
		System.out.println("===========JDK自带的jdk方法AOP编程================");
		try {
			People people2= (People) Proxy.newProxyInstance(
					People.class.getClassLoader(), new Class[]{People.class},
					new ProxyHandler(new ZhangSan()));

			try {
				people2.eat();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
