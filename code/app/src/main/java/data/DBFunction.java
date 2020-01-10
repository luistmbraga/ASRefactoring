package data;

import java.sql.ResultSet;

public interface DBFunction<T> {
    T apply(ResultSet resultSet) throws Exception;
}
