package AndroidWebService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import net.sf.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class webService extends HttpServlet {

	private String sqlqurey, sqlupdate, deviceid, statement;
	private Connection conn;
	private Statement stm = null;
	private ResultSet result = null;
	private PrintWriter outPrintWriter;
	private PreparedStatement pstm = null;
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
	public webService() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			conn = (Connection) DriverManager.getConnection("jdbc:postgresql://10.2.3.222:5432/ars?currentSchema=public", "postgres", "csuduc");
//					.getConnection("jdbc:postgresql://10.2.3.222:5432/ars?currentSchema=public"
//							+ "user=postgres&password=csuduc&useUnicode=true&characterEncoding=utf-8");
			stm = (Statement) conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			sqlqurey = "select * from t1user  ";// 查询表语句
			result = stm.executeQuery(sqlqurey);
			if (result != null) {// 下面用到servlet
				response.setContentType("text/html;charset=UTF-8");// 这句必须放在下一句之前
				outPrintWriter = response.getWriter();
				JSONObject aJson = new JSONObject(); // 对象{}
				while (result.next()) {// 从mysql选取数据
					// 获得客户端发来的参数信息
					//对象序列化为json串
					User aUser = new User();
					aUser.setUsername(result.getString(1));
					aUser.setPaswd(result.getString(2));
					aJson.put("user", aUser);
					outPrintWriter.write(aJson.toString());
				}

			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		outPrintWriter.flush();
		outPrintWriter.close();
		destroy();
		// response.getWriter().write("link sucessfully!");
	}

	public void Json() {

	}

	// 释放资源
	public void destroy() {
		super.destroy();
		try {
			if (result != null) {
				result.close();// 关闭数据库
				result = null;
			}

			if (stm != null) {
				stm.close();
				stm = null;
			}
			if (pstm != null) {
				pstm.close();
				pstm = null;
			}
			if (conn != null) {
				conn.close();// 关闭后Statement，PreparedStatement也会关闭
				conn = null;
			}

		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

}
// </span>