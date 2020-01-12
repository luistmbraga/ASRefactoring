package data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBConnection {

	void connect();

	void disconnect();

	Integer executeUpdate(String update) throws Exception;

	ResultSet executeQuery(String query) throws  Exception;
}