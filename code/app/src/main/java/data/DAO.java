package data;

public interface DAO<K,T> {

	K put(T obj);

	T get(K id);

	void delete(K id);

	void replace(K id, T obj);

}