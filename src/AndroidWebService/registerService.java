package AndroidWebService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

;
public class registerService extends HttpServlet {

	private String sqlqurey;
	private Connection conn;
	private Statement stm = null;
	private ResultSet result = null;
	private int resultNum = 0;
	private PrintWriter outPrintWriter;
	private PreparedStatement pstm = null;
	private static final long serialVersionUID = 1L;

	/* 定义数组大小 */
	private static int COUNT = 40;

	/**
	 * @param args
	 */
	public registerService() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
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

	private JSONObject json(HttpServletRequest request)
			throws UnsupportedEncodingException {
		// 解析Request中的body内容
		String length = request.getHeader("content-length");

		byte[] buffer = new byte[Integer.parseInt(length)];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ServletInputStream stream = request.getInputStream();
			int len = stream.read(buffer);
			bos.write(buffer, 0, len);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String bodyDate = new String(bos.toByteArray(), "UTF-8");
		JSONObject jsonObject = JSONObject.fromObject(bodyDate);
		return jsonObject;

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonObject = json(request);
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			conn = (Connection) DriverManager
					.getConnection(
							"jdbc:postgresql://10.2.3.222:5432/ars?currentSchema=public",
							"postgres", "csuduc");
			stm = (Statement) conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			// String[] cityCodeArray = new String[COUNT];
			// ArrayList aList=new ArrayList();
			String cityString = "";
			sqlqurey = "select codeid from t5citys where city1='"
					+ jsonObject.getString("provinceSelected")
					+ "' and city2='" + jsonObject.getString("citiesSelected")
					+ "' and city3='" + jsonObject.getString("areaSelecteds")
					+ "' ";
			ResultSet city = stm.executeQuery(sqlqurey);

			ResultSetMetaData m = city.getMetaData();

			int columns = m.getColumnCount();

			// 显示表格内容
			while (city.next()) {
				for (int i = 1; i <= columns; i++) {
					cityString += city.getString(i);
				}
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
			sqlqurey = "insert into t1user (username,paswd,soilmos,corpclass,lai,disease,servstarttime,servendtime,locno) values('"
					+ jsonObject.getString("username")
					+ "','"
					+ jsonObject.getString("password")
					+ "','"
					+ jsonObject.getString("soilmos")
					+ "','"
					+ jsonObject.getString("corpclass")
					+ "','"
					+ jsonObject.getString("lai")
					+ "','"
					+ jsonObject.getString("disease")
					+ "','"
					+ jsonObject.getString("beginDate")
					+ "','"
					+ jsonObject.getString("endDate")
					+ "',"
					+ "\'{"
					+ cityString + "}\'" + ") ";// 查询表语句
			System.out.println(sqlqurey);
			JSONObject aJson = new JSONObject(); // 对象{}
			if (!stm.execute(sqlqurey)) {
				aJson.put("result", "success");

			} else {
				aJson.put("result", "fail");
			}

			response.setContentType("text/html;charset=UTF-8");// 这句必须放在下一句之前
			outPrintWriter = response.getWriter();
			outPrintWriter.write(aJson.toString());
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
	}
}
// </span>