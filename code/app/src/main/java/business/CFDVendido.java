package business;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;

public class CFDVendido extends CFD {
    private LocalDateTime dataVenda;
    private double soldValue;

    public CFDVendido(CFD cfd, LocalDateTime data, double value){
        super(cfd);
        this.dataVenda = data;
        this.soldValue = value;
    }

    public CFDVendido(CFD cfd, double value){
        super(cfd);
        this.dataVenda = LocalDateTime.now();
        this.soldValue = value;
    }

    public double getSoldValue() {
        return soldValue;
    }

    public void setSoldValue(double soldValue) {
        this.soldValue = soldValue;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    @Override
    public double getProfit(){
        return getSoldValue() - getBoughtValue();
    }

    @Override
    public String toString(){
        NumberFormat formatter = new DecimalFormat("#0.00");
        return getAtivoFinanceiro().getCompany() + " vendido ( " + formatter.format(getSoldValue()) + " ) em " +
                dataVenda.getYear() + "-" + dataVenda.getMonth() + "-" + dataVenda.getDayOfMonth();
    }
}
