package com.umpay.hfmng.job;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
/**
 * 用于连接保存核减数据的数据库
 * @author lz
 *
 */
public class TradeReduceDBManager { 

	private String user = ""; //用户名 
	private String password = ""; //密码 
	private String host = ""; //主机 
	private String database = ""; //数据库名字
	private String url =""; 
	private Connection con = null; 
	Statement stmt; 
	/** 
	 * 根据主机、数据库名称、数据库用户名、数据库用户密码取得连接。 
	 * @param host String 
	 * @param database String 
	 * @param user String 
	 * @param password String 
	 */ 
	public TradeReduceDBManager(String host, String database, String user, String password) { 
		this.host = host; 
		this.database = database; 
		this.user = user; 
		this.password = password; 
		//显示中文 
		this.url = "jdbc:postgresql://" + host + "/" + database + "?useUnicode=true&characterEncoding=GB2312"; 
		try { 
			Class.forName("org.postgresql.Driver"); 
		} 
		catch (ClassNotFoundException e) { 
			System.err.println("class not found:" + e.getMessage()); 
		} 
		try { 
			con = DriverManager.getConnection(this.url, this.user, this.password); 
			//连接类型为ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY 
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
		} 
		catch (SQLException a) { 
			System.err.println("sql exception:" + a.getMessage()); 
		} 
	} 
	/** 
	 * 返回取得的连接 
	 */ 
	public Connection getCon() { 
		return con; 
	} 
	/** 
	 * 执行一条简单的查询语句 
	 * 返回取得的结果集 
	 */ 
	public ResultSet executeQuery(String sql) { 
		ResultSet rs = null; 
		try { 
			rs = stmt.executeQuery(sql); 
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * 执行一条简单的更新语句
	 * 执行成功则返回true
	 */
	public boolean executeUpdate(String sql) {
		boolean v = false;
		try {
			v = stmt.executeUpdate(sql) > 0 ? true : false;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			return v;
		}
	}
	 /**   
     * 关闭数据库连接   
     *    
     * @throws SQLException   
     */   
    public void close() throws SQLException {    
        con.close();    
    } 
	public static void main(String[] args){
		ResultSet rs;
		TradeReduceDBManager exe = new TradeReduceDBManager("10.10.38.137:5432","postgres","postgres","10658008");
		rs =  exe.executeQuery("select merid,sum(mutenum) as mutenum ,sum(muteamt) as muteamt from hfsettle.thfmsbasedata where localflag=1 and goodsid='ALL' and stlmonth='201201' group by merid");
		try{
			while(rs.next()){
				System.out.println(rs.getString("merid") + "    " + rs.getDouble("mutenum")+ "    " + rs.getDouble("muteamt"));
			}
        }catch (Exception e){
        }
    }
}
