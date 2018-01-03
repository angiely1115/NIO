package db_pools;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import com.mysql.jdbc.Driver;

public class MyPoolImpl implements IMyPool {
	// 所有的习惯配置在源码当中都有对应的代码相对应
	// jdbc参数对象
	// 读取各位老铁配置初始化参数 jdbc
	private static String driver = null;
	private static String url = null;
	private static String user = null;
	private static String password = null;
	// 限制连接池中的管道数量参数
	private static int initCount = 3;
	private static int stepSize = 10;
	private static int poolMaxSize = 150;
	private static Vector<PooledConnection> pooledConnections = new Vector<PooledConnection>();

	// 构造方法
	public MyPoolImpl() {
		init();
	}

	private void init() {
		InputStream in = MyPoolImpl.class.getClassLoader().getResourceAsStream("jdbcPool.properties");
		Properties properties = new Properties();
		try {
			// 将我们的字节信息key - value 形式化
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对jdbc参数进行初始化
		driver = properties.getProperty("jdbcDriver");
		url = properties.getProperty("jdbcurl");
		user = properties.getProperty("userName");
		password = properties.getProperty("password");
		// 限制管道数量参数初始化 习惯优于默认配置的思想
		// initCount = -1 参数不合法
		if (Integer.valueOf(properties.getProperty("initCount")) > 0) {
			//initCount = Integer.valueOf(properties.getProperty("initCount"));
		}
		if (Integer.valueOf(properties.getProperty("stepSize")) > 0) {
			//stepSize = Integer.valueOf(properties.getProperty("stepSize"));
		}
		if (Integer.valueOf(properties.getProperty("poolMaxSize")) > 0) {
			poolMaxSize = Integer.valueOf(properties.getProperty("poolMaxSize"));
		}
		// DriverManager 大管家用来注册 驱动实例
		try {
			// 获取那个数据库的驱动对象
			Driver dbDriver = (Driver) Class.forName(driver).newInstance();
			DriverManager.registerDriver(dbDriver);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 初始化连接池中的管道数量
		//createConnections(initCount);
	}

	// 这肯定是对外界提供管道对象
	@Override
	public PooledConnection getConnection() {
		if (pooledConnections.size() == 0) {
			System.out.println("数据库连接池架构没有正常初始化，将启动补刀机制初始化！");
			createConnections(initCount);
		}
		PooledConnection connection = getRealConnection();
		// 多线程的环境 线程拿到这个对象 引用堆当中实例 可能被抢走
		// 线程死了
		while (connection == null) {
			createConnections(stepSize);
			connection = getRealConnection();
			try {
				//人为的减少竞争
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
       //返回这个对象给上层服务
		return connection;
	}
    //没有被其他线程占用
	//第二个 管道实例集合当中 是不是有可能超时
	private synchronized PooledConnection getRealConnection() {
		for (PooledConnection conn:pooledConnections){
			if(!conn.isBusy()){
				Connection connection =conn.getConnection();
				   try {
					   //发送一个测试命令  是否得到回应 
					if(!connection.isValid(2000)){
						  //替换管道对象
						connection =DriverManager.getConnection(url, user, password);
					   }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				   conn.setBusy(true);
					//返回给上层应用使用
					return conn;   
			}
		}
		return null;
	}

	// 创建的方法行为应该被连接池对象监控起来
	@Override
	public void createConnections(int count) {
		if (poolMaxSize > 0 && pooledConnections.size() + count <= poolMaxSize) {
			for (int i = 0; i < count; i++) {
				try {
					Connection connection = (Connection) DriverManager.getConnection(url, user, password);
					// 构造自定义的额管道
					PooledConnection pooledConnection = new PooledConnection(connection, false);
					// 注册到 连接池集合对象当中取
					pooledConnections.add(pooledConnection);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		} else {
			System.out.println("创建失败，创建管道参数非法！");
		}

	}

}
