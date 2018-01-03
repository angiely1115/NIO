package db_pools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 自定义连接数据库管道Bean
 * 重点：原生的Connection上面没有复用的标志
 *     Sam老司机自己封装设计isBusy来解决线程被占用的问题
 * 总结：这个思想就封装思想  也就是扩展功能
 * @author Sam
 *
 */
public class PooledConnection {
	//表示繁忙的标志   复用的标志 线程安全
	private boolean isBusy = false;
   //真正的管道 用来操作数据的 java.sql.Connection
	private Connection connection;
   public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public boolean isBusy() {
		return isBusy;
	}
	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
	
	//构造方法  其他的程序调用它的时候 是要创建并且给它初始化组件
	public PooledConnection(Connection connection,boolean isBusy){
		this.connection = connection;
		this.isBusy = isBusy;
	}
	//自定义写要释放方法
	public void close(){
		this.isBusy = false;
	}
	// 自己写了 查询功能
	public ResultSet queryBysql(String sql){
		Statement sm =null;
		ResultSet rs=null;
		try {
			sm=connection.createStatement();
			rs =sm.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
}
