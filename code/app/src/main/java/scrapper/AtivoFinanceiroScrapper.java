package scrapper;

import business.AtivoFinanceiro;
import business.CFD;

import java.util.List;

public interface AtivoFinanceiroScrapper {

	boolean isRunning();

	void setRunning(boolean running);

	void stop();

	void start();
}