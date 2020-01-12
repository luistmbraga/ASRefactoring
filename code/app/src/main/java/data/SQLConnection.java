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
		Statement st = this.conn.createStatement();

		Integer r = st.executeUpdate(update);

		st.close();

		return r;
	}

	public ResultSet executeQuery(String query) throws SQLException{

		Statement st = this.conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		st.close();

		return rs;
	}


}