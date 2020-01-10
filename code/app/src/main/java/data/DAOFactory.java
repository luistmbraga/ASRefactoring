package data;

public class DAOFactory {

	private static DAOFactory daoFactory = new DAOFactory();
	private DAOFactory(){}

	public static DAOFactory getFactory() {
		return daoFactory;
	}

	public CFDDAO newCFDDAO() {
		return new CFDDAOConcrete();
	}

	public UtilizadorDAO newUtilizadorDAO() {
		return new UtilizadorDAOConcrete();
	}

	public AtivoFinanceiroDAO newAtivoFinanceiroDAO() {
		return new AtivoFincanceiroDAOConcrete();
	}

}