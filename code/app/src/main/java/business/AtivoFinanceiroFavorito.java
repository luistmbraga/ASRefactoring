package business;

public class AtivoFinanceiroFavorito extends AtivoFinanceiro {
    private double valueToNotify;

    public AtivoFinanceiroFavorito(AtivoFinanceiro a, double v){
        super(a);
        this.valueToNotify = v;
    }

    public double getValueToNotify() {
        return valueToNotify;
    }

    public boolean reachedThreshold(double value){
        return (valueToNotify >= getValue() && valueToNotify < value) ||
                (valueToNotify < getValue() && valueToNotify >= value);
    }

    @Override
    public boolean equals(Object o){
        return super.equals(o);
    }

    @Override
    public String toString(){
        return super.toString() + " Threshold: " + getValueToNotify();
    }
}
