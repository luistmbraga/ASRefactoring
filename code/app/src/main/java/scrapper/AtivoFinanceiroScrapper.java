package scrapper;

public interface AtivoFinanceiroScrapper {

	boolean isRunning();

	void setRunning(boolean running);

	void stop();

	void start();
}