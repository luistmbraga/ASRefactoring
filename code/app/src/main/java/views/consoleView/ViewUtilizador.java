package views.consoleView;

import business.EESTrading;
import business.Utilizador;

public class ViewUtilizador extends ConsoleView {
    public ViewUtilizador(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator) {
        super(trading, utilizador, mediator);
    }

    @Override
    public void render() {
        layout("Olá " + utilizador.getUsername() + " - " + utilizador.getMoney() + "$");
        System.out.println("1.Comprar CFD");
        System.out.println("2.Ver portfolio");
        System.out.println("3.Ver transições antigas");
        System.out.println("4.Ver favoritos");
        System.out.println("5.Depositar dinheiro");
        System.out.println("6.Levantar dinheiro");
        System.out.println("7.Sair");

        int option = getSelectedOption();
        switch (option){
            case 1:
                mediator.changeView(ATIVOS_DISPONIVEIS);
                return ;
            case 2:
                mediator.changeView(MEUS_CFDS);
                return ;
            case 3:
                mediator.changeView(TRANSACOES_ANTIGAS);
                return ;
            case 4:
                mediator.changeView(FAVORITOS);
                return ;
            case 5:
                mediator.changeView(DEPOSITAR);
                return ;
            case 6:
                mediator.changeView(WITHDRAW);
                return ;
            case 7:
                this.utilizador = null;
                mediator.changeView(INICIAL);
                return ;
            default:
                System.out.println("ERROR: Não é uma opção válida");
                render();
        }
    }
}
