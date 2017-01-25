package AndroidWebService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import net.sf.json.JSONObject;;
public class jsonTest {

	/**
	 * @param args
	 */
	public jsonTest() {
		super();
	}

	public static void toJson() {
					// 获得客户端发来的参数信息
					User aUser = new User();
					aUser.setUsername("qinman");
					aUser.setPaswd("123");
					JSONObject json= JSONObject.fromObject(aUser);
					System.out.println(json);
	}

	public static void name() {
		toJson();
	}
}
