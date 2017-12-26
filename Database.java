package crawlerNews;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Database {
	static Connection conn;
	static Statement st;
	public static final String author = "Cheetah";

	public static boolean InsertProduct(List<String> tempList) {
		try {
		
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updatetime = dateFormat.format(now);
			tempList.add(updatetime);
			tempList.add(author);
			
			//需要执行的插入语句，包括表名，列名（保持和mysql数据一致）
			String insql = "INSERT INTO newstitle(title,publishdate,updatetime,author) VALUES(?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(insql);

			int i;
			for (i = 0; i < tempList.size(); i++) {
				String strvalue = tempList.get(i);
				ps.setString(i + 1, strvalue);
			}
			int result = ps.executeUpdate();
			ps.close();
			if(result>0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void setConn(){
		conn = getConnection();
	}
	public static void closeConn(){
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){
		Connection con = null;
		String DRIVER = "com.mysql.jdbc.Driver";
		String DBUSER = "root";
		String DBPASS = "123456";
		String DBURL = "jdbc:mysql://localhost:3306/crawler?useUnicode=true&amp;characterEncoding=utf-8&amp;serverTimezone=UTC&amp;useSSL=false";
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);
		} catch (Exception e) {
			System.out.println("数据库连接失败"+e.getMessage());
		}
		
		return con;
	}
}
