package business;

public class Moeda extends AtivoFinanceiro {

    public Moeda(String name,double value){
        super(name,value,"Moeda");
    }

	/**
	 * vou criar um sem parametros porque no scrapper estava a dar erro
	 */
	public Moeda() {
		// TODO - implement Moeda.Moeda
		throw new UnsupportedOperationException();
	}

}