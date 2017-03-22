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
public class updateUserInfoService extends HttpServlet {

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
	public updateUserInfoService() {
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
			conn = (Connection) DriverManager.getConnection("jdbc:postgresql://10.2.3.222:5432/ars?currentSchema=public", "postgres", "csuduc");
			response.setContentType("text/html;charset=UTF-8");// 这句必须放在下一句之前
			outPrintWriter = response.getWriter();
			JSONObject aJson = new JSONObject(); // 对象{}
			stm = (Statement) conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			boolean ifDouHao = false;//判断拼接时是否需要逗号
			StringBuffer stringBuffer = new StringBuffer("update t1user set ");
			if(jsonObject.has("paswd")){
				stringBuffer.append(" paswd = '"+jsonObject.get("paswd")+"' ");
				ifDouHao = true;
			}
			if(jsonObject.has("producttype")){
				if(ifDouHao)
					stringBuffer.append(",");
				stringBuffer.append(" producttype = '"+jsonObject.get("producttype")+"' ");
				ifDouHao = true;
			}
			if(jsonObject.has("beginDate")){
				if(ifDouHao)
					stringBuffer.append(",");
				stringBuffer.append(" servstarttime = '"+jsonObject.get("beginDate")+"' ");
				ifDouHao = true;
			}
			if(jsonObject.has("endDate")){
				if(ifDouHao)
					stringBuffer.append(",");
				stringBuffer.append(" servendtime = '"+jsonObject.get("endDate")+"' ");
				ifDouHao = true;
			}
			stringBuffer.append(" where id = "+jsonObject.get("id"));// 查询表语句
			sqlqurey = stringBuffer.toString();
			System.out.println(sqlqurey);
			if(stm.executeUpdate(sqlqurey)==1)
			{
				aJson.put("result", "success");
			}
			else {
				aJson.put("result", "fail");
			}
			outPrintWriter.write(aJson.toString());
			destroy();
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