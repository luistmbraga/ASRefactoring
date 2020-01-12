package data;

import java.sql.ResultSet;

public interface DBConnection {

	void connect();

	void disconnect();

	public Integer executeUpdate(String update);

	public ResultSet executeQuery(String query);
}