package views.consoleView;

import business.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewTransacoesAntigas extends ConsoleView {
    private List<CFDVendido> cfds;
    public ViewTransacoesAntigas(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator, List<CFDVendido> cfds) {
        super(trading, utilizador, mediator);
        this.cfds = cfds;
        this.cfds.sort((cfd1, cfd2) -> {
            LocalDateTime d1 = cfd1.getDataVenda();
            LocalDateTime d2 = cfd2.getDataVenda();
            return d1.isBefore(d2) ? 1 : d1.isEqual(d2) ?  0 : -1;
        });
    }

    @Override
    public void render() {
        layout("Transações antigas");
        double total = 0;
        double profit = 0;
        for(CFDVendido cfd : cfds){
            total += cfd.getSoldValue();
            profit += cfd.getProfit();
        }
        NumberFormat formatter = new DecimalFormat("#0.00");

        printPage(0, this.cfds);
        printMessage("Pressione o ENTER para sair", '#');
        System.out.println();
        printMessage("Total vendido : " + formatter.format(total));
        printMessage("Lucro total: " + formatter.format(profit));

        boolean optionSelected = false;
        while (!optionSelected){
            String input = scanner.nextLine();
                if(input.matches("[ ]*:[ ]*page[ ]+[0-9]+[ ]*")){
                    Pattern pattern = Pattern.compile("[ ]*:[ ]*page[ ]+([0-9]+)[ ]*");
                    Matcher matcher = pattern.matcher(input);
                    if(matcher.find()) {
                        int pageNumber = Integer.parseInt(matcher.group(1));
                        printPage(pageNumber, this.cfds);
                    }
                 }
                else {
                    optionSelected = true;
                }
        }
        mediator.changeView(UTILIZADOR);
    }
}
