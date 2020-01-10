package views.consoleView;

import business.CFD;
import business.EESTrading;
import business.Utilizador;

public class ViewCFDPossuido extends ConsoleView {
    private CFD cfd;

    public ViewCFDPossuido(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator,  CFD cfd ) {
        super(trading, utilizador, mediator);
        this.cfd = cfd;
    }

    @Override
    public void render() {
        layout(cfd.getName() + " - " + cfd.getValue() + "$");
        System.out.print("Top Profit: " + (cfd.getTopProfit() == null ? "--" : cfd.getTopProfit()));
        System.out.println(" Stop Loss: " + (cfd.getStopLoss() == null ? "--" : cfd.getStopLoss()));
        System.out.println("1.Vender");
        System.out.println("2.Definir Stop Loss");
        System.out.println("3.Definir Top Profit");
        System.out.println("4.Retroceder");

        int option = getSelectedOption();
        if(isUpdated()){
            boolean yes = yesOrNoQuestion("O valor do CFD foi alterado, quer dar refresh?");
            if(yes){
                render();
                return;
            }
        }
        switch (option){
            case 1:
                trading.sell(cfd);
                mediator.changeView(MEUS_CFDS);
                return;
            case 2:
                System.out.print("Introduza o Stop Loss: ");
                double valor = getDouble();
                if(trading.setCFDStopLoss(cfd, valor)){
                    mediator.changeView( CFD_POSSUIDO);
                    return;
                }else{
                    System.out.println("O stop loss tem de ser menor que o valor do CFD");
                    break;
                }
            case 3:
                System.out.print("Introduza o Top Profit: ");
                double topProfit = getDouble();
                if(trading.setCFDTopProfit(cfd, topProfit)){
                    mediator.changeView(CFD_POSSUIDO);
                    return ;
                }else{
                    System.out.println("O top profit tem de ser maior que o valor do CFD");
                    break;
                }
            case 4:
                mediator.changeView(MEUS_CFDS);
                return ;
            default:
                System.out.println("Não é uma opção válida: " + option);
                render();
                return ;
        }
        mediator.changeView(MEUS_CFDS);
    }
}
