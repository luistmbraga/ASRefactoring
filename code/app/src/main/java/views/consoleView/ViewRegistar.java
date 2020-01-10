package views.consoleView;

import business.EESTrading;
import business.Utilizador;

public class ViewRegistar extends ConsoleView {
    public ViewRegistar(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator) {
        super(trading, utilizador, mediator);
    }

    @Override
    public void render() {
        layout("Menu Inicial");
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        Utilizador util = trading.regist(username, password);
        if(util != null){
            utilizador.deconstruct(util);
            mediator.changeView(UTILIZADOR);
        }
        else{
            System.out.println("Não foi possivel registar o utilizador");
            mediator.changeView(INICIAL);
        }
    }
}
