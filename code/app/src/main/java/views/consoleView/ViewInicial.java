package views.consoleView;

import business.EESTrading;
import business.Utilizador;
import views.IView;

public class ViewInicial extends ConsoleView {

    public ViewInicial(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator) {
        super(trading, utilizador, mediator);
    }

    @Override
    public void render() {
        layout("Menu Inicial");
        System.out.println("1.Login");
        System.out.println("2.Registar");
        System.out.println("3.Sair");

        int option = getSelectedOption();
        switch (option){
            case 1:
                mediator.changeView(LOGIN);
                return ;
            case 2:
                mediator.changeView(REGISTAR);
                return ;
            case 3: System.exit(0);
            default: System.out.println("Não é uma opção válida: " + option);
        }
        mediator.changeView(INICIAL);
    }
}
