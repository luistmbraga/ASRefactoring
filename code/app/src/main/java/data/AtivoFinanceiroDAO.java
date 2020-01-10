package data;

import business.AtivoFinanceiro;
import business.CFD;
import data.DAO;

import java.util.List;

public interface AtivoFinanceiroDAO extends DAO<String,AtivoFinanceiro> {

	List<AtivoFinanceiro> getAll();
	List<CFD> getCFDs(AtivoFinanceiro ativoFinanceiro);
}