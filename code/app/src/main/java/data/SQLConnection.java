package data;

import business.AtivoFinanceiro;

import java.sql.*;
import java.util.function.Consumer;

public class SQLConnection implements DBConnection{

	private Connection conn;

	public java.sql.Connection getConn() {
		return conn;
	}

	public SQLConnection(){
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = null;
		}
		catch (ClassNotFoundException e){}
	}

	public void connect() {
		try{
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradingplatform?useTimezone=true&serverTimezone=UTC","root","1234");
		}
		catch (SQLException e){
			e.printStackTrace(); System.out.println("Não conseguiu conectar!");}
	}

	public void disconnect() {
		try{
			this.conn.close();
		}
		catch(SQLException e){e.printStackTrace();}
	}
}