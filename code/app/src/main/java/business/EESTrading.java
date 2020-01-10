package business;

import data.AtivoFinanceiroDAO;
import data.CFDDAO;
import data.DAOFactory;
import data.UtilizadorDAO;

import java.util.List;
import java.util.Observable;

public class EESTrading extends Observable {
	private static EESTrading trading = new EESTrading();
	public static EESTrading getInstance(){
		return trading;
	}

	private double fee;
	private CFDDAO cfdDAO;
	private AtivoFinanceiroDAO ativoFinanceiroDAO;
	private UtilizadorDAO utilizadorDAO;


	public EESTrading(){
		this.fee = 0.02;
		DAOFactory daoFactory = DAOFactory.getFactory();
		cfdDAO = daoFactory.newCFDDAO();
		ativoFinanceiroDAO = daoFactory.newAtivoFinanceiroDAO();
		utilizadorDAO = daoFactory.newUtilizadorDAO();
	}

	public double getFee(){
		return fee;
	}

	public List<AtivoFinanceiro> getAtivos(){
		return ativoFinanceiroDAO.getAll();
	}

	public CFD getCFD(int id){
		return cfdDAO.get(id);
	}

	public AtivoFinanceiro getAtivo(String id){
		return ativoFinanceiroDAO.get(id);
	}

	public synchronized void putAtivosFinanceiros(List<AtivoFinanceiro> ativoFinanceiros){
		ativoFinanceiros.forEach(novo -> {
			ativoFinanceiroDAO.replace(novo.getCompany(), novo);
			List<CFD> cfds = ativoFinanceiroDAO.getCFDs(novo);
			cfds.forEach(this::applyThresholds);
		});
		setChanged();
		notifyObservers(ativoFinanceiros);
	}

	public synchronized void applyThresholds(CFD cfd){
		if(cfd.checkTopprofit()) {
			CFDVendido cfdVendido = new CFDVendido(cfd, cfd.getTopProfit());
			cfdDAO.sell(cfdVendido);
			deposit(cfd.getUtilizador(), cfd.getTopProfit());
			setChanged();
			notifyObservers(cfdVendido);
		} else if(cfd.checkStopLoss()) {
			CFDVendido cfdVendido = new CFDVendido(cfd, cfd.getStopLoss());
			cfdDAO.sell(cfdVendido);
			deposit(cfd.getUtilizador(), cfd.getStopLoss());
			setChanged();
			notifyObservers(cfdVendido);
		}
	}

	public void update(Utilizador utilizador){
		utilizadorDAO.replace(utilizador.getUsername(), utilizador);
		setChanged();
		notifyObservers();
	}

	/**
	 *
	 * @param user
	 * @param pass
	 */
	public Utilizador login(String user, String pass) {
		if(utilizadorDAO.login(user, pass)){
			Utilizador utilizador = utilizadorDAO.get(user);
			return utilizador;
		}
		return null;
	}

	/**
	 *
	 * @param utilizador
	 * @param cfd
	 */
	public boolean buy(Utilizador utilizador, CFD cfd) {
		if(cfd.getBoughtValue() <= 0) return false;
		cfd.applyFee(fee);
		cfd.setUtilizador(utilizador);
		int idCFD = cfdDAO.put(cfd);
		if(idCFD > 0){
			double value = cfd.getBoughtValue();
			return withdraw(utilizador, value);
		}
		return false;
	}

	/**
	 *
	 * @param cfd
	 */
	public void sell(CFD cfd) {
		double valorAtual = cfd.getValue();
		CFDVendido cfdVendido = new CFDVendido(cfd, valorAtual);
		cfdDAO.sell(cfdVendido);
		deposit(cfd.getUtilizador(), valorAtual);
	}

	/**
	 *
	 * @param username
	 * @param pass
	 */
	public Utilizador regist(String username, String pass) {
		Utilizador novo = new Utilizador(username, pass,0);
		String id =  utilizadorDAO.put(novo);
		System.out.println("DEBUG: Trying to regist user:" + novo.getUsername() + " " + novo.getPassword());
		if(id != null) novo = utilizadorDAO.get(username);
		else novo = null;
		return novo;
	}

	/**
	 *
	 * @param utilizador
	 * @param value
	 */
	public void deposit(Utilizador utilizador, double value) {
		if(value < 0) return;
		utilizadorDAO.addMoney(utilizador, value);
		setChanged();
		notifyObservers();
	}

	/**
	 *
	 * @param utilizador
	 * @param value
	 */
	public boolean withdraw(Utilizador utilizador, double value) {
		if(utilizador.getMoney() < value || value < 0) return false;
		utilizadorDAO.removeMoney(utilizador, value);
		setChanged();
		notifyObservers();
		return true;
	}

	public boolean setCFDTopProfit(CFD cfd, double topProfit) {
		if(cfd.setTopProfit(topProfit)){
			cfdDAO.replace(cfd.getId(), cfd);
			applyThresholds(cfd);
			return true;
		}
		return false;
	}

	public boolean setCFDStopLoss(CFD cfd, double stopLoss) {
		if(cfd.setStopLoss(stopLoss)){
			cfdDAO.replace(cfd.getId(), cfd);
			applyThresholds(cfd);
			return true;
		}
		return false;
	}

	public List<CFD> getPortfolio(Utilizador utilizador){
		return  cfdDAO.getPortfolio(utilizador);
	}

	public List<CFDVendido> getTransacoesAntigas(Utilizador utilizador){
		return  cfdDAO.getVendidos(utilizador);
	}

}