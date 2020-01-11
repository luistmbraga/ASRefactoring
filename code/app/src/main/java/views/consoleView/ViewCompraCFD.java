package views.consoleView;

import business.AtivoFinanceiro;
import business.CFD;
import business.EESTrading;
import business.Utilizador;

public class ViewCompraCFD extends ConsoleView {
    private AtivoFinanceiro ativo;
    private CFD cfd;

    public ViewCompraCFD(EESTrading trading, Utilizador utilizador,ConsoleViewMediator mediator, AtivoFinanceiro ativoFinanceiro) {
        super(trading, utilizador , mediator );
        this.ativo = ativoFinanceiro;
    }

    @Override
    public void render() {
        cfd = new CFD();
        cfd.setUtilizador(utilizador);
        cfd.setAtivoFinanceiro(ativo);
        setValorPagar();
        setTProfit();
        setStopLoss();

        if (refreshPage()) return;
        finalCFDInformation();
        mediator.changeView(UTILIZADOR);
    }

    private void finalCFDInformation() {
        if(trading.buy(utilizador, cfd)){
            printMessage("CFD bought sucessfully", '#');
        } else {
            printMessage("CFD not bought", '#');
        }
    }

    private boolean refreshPage() {
        boolean yes;
        if(isUpdated()){
            yes = yesOrNoQuestion("O valor atual do CFD foi alterado, quer dar refresh?");
            if(yes){
                render();
                return true;
            }
        }
        return false;
    }

    private void setStopLoss() {
        boolean yes;
        yes = yesOrNoQuestion("Deseja definir um Stop Loss?");
        if(yes){
            System.out.print("Introduza o valor: ");
            double stoploss = getDouble();
            if(!cfd.setStopLoss(stoploss))
                printMessage("Valor de StopLoss inválido: "  + stoploss);
        } else cfd.setStopLoss(0);
    }

    private void setTProfit() {
        boolean yes = yesOrNoQuestion("Deseja definir um Top Profit?");
        if(yes){
            System.out.print("Introduza o valor: ");
            double topProfit = getDouble();
            if(!cfd.setTopProfit(topProfit))
                printMessage("Valor de TopProfit inválido: " + topProfit);
        } else cfd.setTopProfit(0);
    }

    private void setValorPagar() {
        layout(utilizador.getUsername() + " CFD: " + ativo.getCompany() +  " " + ativo.getValue() + " $");
        System.out.print("Valor de compra: ");
        double valor = getDouble();
        cfd.setBoughtValue(valor);
        cfd.setUnits(valor / ativo.getValue());
        printMessage("Juros aplicados - " + valor*(trading.getFee()) + " (" + valor*(1-trading.getFee()) + ") ", '/');
    }
}
