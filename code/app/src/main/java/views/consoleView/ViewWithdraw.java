package views.consoleView;

import business.EESTrading;
import business.Utilizador;

public class ViewWithdraw extends ConsoleView {
    public ViewWithdraw(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator) {
        super(trading, utilizador, mediator);
    }

    @Override
    public void render() {
        layout("Withdraw - " + utilizador.getMoney() + "$");
        System.out.print("Inserir dinheiro: ");
        double valor = getDouble();
        if (trading.withdraw(utilizador, valor)) {
            System.out.println("Dinheiro levantado com sucesso");
        } else {
            System.out.println("ERROR: Não possui essa quantidade");
        }
        mediator.changeView(UTILIZADOR);
    }
}
