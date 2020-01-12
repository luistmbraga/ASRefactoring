package views.consoleView;

import business.CFD;
import business.EESTrading;
import business.Utilizador;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewMeusCFDs extends ConsoleView {
    private List<CFD> cfds;

    public ViewMeusCFDs(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator, List<CFD> cfds) {
        super(trading, utilizador, mediator);
        this.cfds = cfds;
    }

    @Override
    public void render() {
        layout("Meus CFDs");
        System.out.println("0.Retroceder");
        printPage(0, cfds);

        if (apresentaContratosAtualizados()) return;
        int option = getOption();
        nextMenu(option);
    }

    private boolean apresentaContratosAtualizados() {
        if(isUpdated()){
            boolean yes = yesOrNoQuestion("Alguns dos seus contratos foram atualizados\nQuer dar refresh?");
            if(yes){
                mediator.changeView(MEUS_CFDS);
                return true;
            }
        }
        return false;
    }

    private boolean isaCFDOption(int option) {
        return option > 0 && option <= cfds.size();
    }

    private int getOption() {
        int option = 0;
        boolean optionSelected = false;
        while (!optionSelected){
            String input = scanner.nextLine();
            if(input.matches("[0-9]+")){
                option = Integer.parseInt(input);
                optionSelected = true;
            }
            else verifyPage(input);
        }
        return option;
    }

    private void verifyPage(String input) {
        if(input.matches("[ ]*:[ ]*page[ ]+[0-9]+[ ]*")){
            Pattern pattern = Pattern.compile("[ ]*:[ ]*page[ ]+([0-9]+)[ ]*");
            Matcher matcher = pattern.matcher(input);
            if(matcher.find()) {
                int pageNumber = Integer.parseInt(matcher.group(1));
                System.out.println("0.Retroceder");
                printPage(pageNumber, cfds);
            }
        }
    }

    private void nextMenu(int option) {
        if(isaCFDOption(option)){
            mediator.changeView(CFD_POSSUIDO, cfds.get(option - 1));
        }else if(option == 0){
            mediator.changeView(UTILIZADOR);
        }
        else {
            System.out.println("Não existe esse CFD");
            mediator.changeView(MEUS_CFDS);
        }
    }

}
