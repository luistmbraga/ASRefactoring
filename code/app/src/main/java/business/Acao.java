package business;

public class Acao extends AtivoFinanceiro {
    public Acao(String name,double value){
        super(name,value,"Acao");
    }
    public Acao(){
        super("", 0, "Acao");
    }
}