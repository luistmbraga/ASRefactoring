package data;

import business.CFD;
import business.CFDVendido;
import business.Utilizador;

import java.util.List;


public interface CFDDAO extends DAO<Integer, CFD> {

	/**
	 * 
	 * @param cfd
	 */
	double sell(CFDVendido cfd);

	/**
	 * 
	 * @param id
	 */
	double getValue(int id);

	List<CFD> get(Utilizador user);

	List<CFD> getPortfolio(Utilizador u);

	List<CFDVendido> getVendidos(Utilizador u);
}