package data;


import java.sql.*;


public class SQLConnection implements DBConnection{

	private Connection conn;

	public SQLConnection(){
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn = null;
		}
		catch (ClassNotFoundException e){}
	}

	public void connect() {
		try{
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tradingplatform?useTimezone=true&serverTimezone=UTC","root","admin");
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

	public Integer executeUpdate(String update) throws SQLException{

		return this.conn.createStatement().executeUpdate(update);
	}

	public ResultSet executeQuery(String query) throws SQLException{

		return this.conn.createStatement().executeQuery(query);
	}


}