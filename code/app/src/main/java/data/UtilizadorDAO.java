package data;

import business.AtivoFinanceiro;
import business.CFD;
import business.Utilizador;

import java.util.List;

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