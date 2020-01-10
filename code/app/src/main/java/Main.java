import business.EESTrading;
import scrapper.AtivoFinanceiroScrapper;
import scrapper.AtivoFinanceiroScrapperFactory;
import views.ViewMediator;
import views.consoleView.ConsoleViewMediator;

public class Main {
    public static void main(String[] args) {
        EESTrading trading = EESTrading.getInstance();

        AtivoFinanceiroScrapperFactory scrapperFactory = new AtivoFinanceiroScrapperFactory();
        AtivoFinanceiroScrapper actionsScrapper = scrapperFactory.newAtivoFinanceiroScrapper("jsonAcoes");
        AtivoFinanceiroScrapper cryptoScrapper = scrapperFactory.newAtivoFinanceiroScrapper("jsonCrypto");

        actionsScrapper.start();
        cryptoScrapper.start();

        ViewMediator viewManager = new ConsoleViewMediator("inicial", trading);
        viewManager.start();
    }
}
