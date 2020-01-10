package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.function.Consumer;

public interface DBConnection {

	void connect();

	void disconnect();

	Connection getConn();
}