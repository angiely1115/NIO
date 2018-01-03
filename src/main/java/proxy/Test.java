package proxy;

import java.io.FileOutputStream;
import java.lang.reflect.Proxy;

import sun.misc.ProxyGenerator;

/**
 * 
 * @author Sam
 *
 */
public class Test {

	public static void main(String[] args) {
		//入口  Java  程序 分为  编码器  编译器  运行器 手写的入口在这里  这里就是创建了一份运行时期我们看不到的代理对象
		People people=(People)Proxy.newProxyInstance(People.class.getClassLoader(), new Class[]{People.class}, new ProxyHandler(new ZhangSan()) );
		try {
			//接口对象接
			people.eat();
		} catch (Throwable e) {
			e.printStackTrace();
		}
     System.out.println("动态代理类："+people.getClass().getName());
     createProxyClassFile();
	}
   //将运行时期的动态代理对象 搞出来 存到我们的项目路径
	public static void createProxyClassFile(){
	 byte[] data=ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{People.class});
	 //字节数组通过流的形式保存项目文件
	 try {
		FileOutputStream out =new FileOutputStream("$Proxy0.class");
		out.write(data);
		out.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
}
