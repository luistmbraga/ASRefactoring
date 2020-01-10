package views.consoleView;

import business.EESTrading;
import business.Utilizador;

public class ViewDepositar extends ConsoleView {
    public ViewDepositar(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator) {
        super(trading, utilizador, mediator);
    }

    @Override
    public void render() {
        layout("Depositar - " + utilizador.getMoney() + "$");
        System.out.print("Inserir dinheiro: ");
        double valor = getDouble();
        trading.deposit(utilizador, valor);
        mediator.changeView(UTILIZADOR);
    }
}
