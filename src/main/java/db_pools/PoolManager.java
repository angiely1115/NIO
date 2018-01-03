package db_pools;

/**
 * 内部类单例模式来对外提供连接池对象 
 * 严格保证线程安全机制
 * @author Sam
 *
 */
public class PoolManager {
	private static class createPool {
		private static MyPoolImpl poolImpl = new MyPoolImpl();
	}

	/**
	 * jvm当中类加器加载字节码是一个严格互斥理论模型 类加载器原理 完美的实现线程安全
	 * 
	 * @return
	 */
	public static MyPoolImpl getInstace() {
		return createPool.poolImpl;
	}
}
