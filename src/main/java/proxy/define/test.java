package proxy.define;

import java.util.Map;
import java.util.WeakHashMap;

public class test {
	private static Map loaderToCache = new WeakHashMap();
	public static void main(String[] args) {
		//Proxy 的重要静态变量1234567891011
		// 映射表：用于维护类装载器对象到其对应的代理类缓存
		  // 标记：用于标记一个动态代理类正在被创建中private static Object pendingGenerationMarker = new Object();  // 同步表：记录已经被创建的动态代理类类型，主要被方法 isProxyClass 进行相关的判断private static Map proxyClasses = Collections.synchronizedMap(new WeakHashMap());  // 关联的调用处理器引用protected InvocationHandler h;
	}

}
