package data;

import business.Utilizador;

public interface UtilizadorDAO extends DAO<String, Utilizador> {

	/**
	 * 
	 * @param user
	 * @param value
	 */
	void addMoney(Utilizador user, double value);

	/**
	 * 
	 * @param user
	 * @param value
	 */
	void removeMoney(Utilizador user, double value);

	boolean login(String username, String password);

}