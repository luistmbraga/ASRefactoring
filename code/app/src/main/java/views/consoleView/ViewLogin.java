package views.consoleView;

import business.EESTrading;
import business.Utilizador;

public class ViewLogin extends ConsoleView {
    public ViewLogin(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator) {
        super(trading, utilizador, mediator);
    }

    @Override
    public void render() {
        layout("Login");
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        Utilizador user = trading.login(username, password);
        if(user != null){
            utilizador.deconstruct(user);
            mediator.changeView(UTILIZADOR);
        }
        else{
            System.out.println("Credenciais incorretas");
            mediator.changeView(INICIAL);
        }
    }
}
