package views.consoleView;

import business.AtivoFinanceiro;
import business.AtivoFinanceiroFavorito;
import business.EESTrading;
import business.Utilizador;

import java.util.List;


public class ViewFavoritos extends ConsoleView {
    private List<AtivoFinanceiroFavorito> favoritos;


    public ViewFavoritos(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator) {
        super(trading, utilizador, mediator);
        this.favoritos = utilizador.getFavoritos();
    }

    @Override
    public void render() {
        System.out.println("0. Retroceder");
        printPage(0, favoritos);
        int option = -1;
        while (option < 0 || (option > favoritos.size() && favoritos.size() > 0)){
            option = getOptionInPage(favoritos);
        }
        if(option == 0) {
            mediator.changeView(UTILIZADOR);
            return ;
        }
        mediator.changeView(ATIVO_FINANCEIRO,favoritos.get(option-1));
    }
}
