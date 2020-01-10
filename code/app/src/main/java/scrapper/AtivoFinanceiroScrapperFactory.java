package scrapper;

public class AtivoFinanceiroScrapperFactory {

	public AtivoFinanceiroScrapper newAtivoFinanceiroScrapper(String scrapper) {
		switch (scrapper){
			case "jsonAcoes" : return new JSONActionsScrapper();
			case "jsonCrypto" : return new JSONCryptoScrapper();
			default: return null;
		}
	}
}