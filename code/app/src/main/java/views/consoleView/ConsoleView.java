package views.consoleView;

import business.CFD;
import business.CFDVendido;
import business.EESTrading;
import business.Utilizador;
import views.IView;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ConsoleView implements IView {
    public static final String ATIVOS_DISPONIVEIS = "ativosDisponiveis";
    public static final String CFD_POSSUIDO = "cfdPossuido";
    public static final String COMPRA_CFD = "compraCFD";
    public static final String DEPOSITAR = "depositar";
    public static final String INICIAL = "inicial";
    public static final String LOGIN = "login";
    public static final String MEUS_CFDS = "meusCFDS";
    public static final String REGISTAR = "registar";
    public static final String TRANSACOES_ANTIGAS = "transacoesAntigas";
    public static final String UTILIZADOR = "utilizador";
    public static final String WITHDRAW = "withdraw";
    public static final String FAVORITOS = "favoritos";
    public static final String ATIVO_FINANCEIRO = "ativoFinanceiro";
    public static final String EXIT = null;


    protected EESTrading trading;
    protected Scanner scanner;
    protected Utilizador utilizador;
    protected ConsoleViewMediator mediator;
    private volatile boolean updated = false;

    public ConsoleView(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator){
        this.scanner = new Scanner(System.in);
        this.utilizador = utilizador;
        this.trading = trading;
        this.mediator = mediator;
    }

    protected void layout(String title){
        int width = 50;
        StringBuilder res = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < width; i++)
            sb.append('-');
        sb.append('\n');
        String s = sb.toString();
        res.append(s);
        int i = 0;
        sb = new StringBuilder();
        for(; i < ((width - (title.length()+2)) / 2); i++ )
            sb.append('-');
        sb.append(' ');
        sb.append(title);
        sb.append(' ');
        i+= title.length() + 2;
        for(; i < width; i++)
            sb.append('-');
        sb.append('\n');
        res.append(sb.toString());
        res.append(s);
        System.out.println(res.toString());
    }

    protected void printMessage(String message, char filler){
        int i = 0;
        int width = 50;
        StringBuilder sb = new StringBuilder();
        for(; i < ((width - (message.length()+2)) / 2); i++ )
            sb.append(filler);
        sb.append(' ');
        sb.append(message);
        sb.append(' ');
        i+= message.length() + 2;
        for(; i < width; i++)
            sb.append(filler);
        System.out.println(sb.toString());
    }

    protected void printMessage(String message){
        printMessage(message, '-');
    }

    protected int getSelectedOption(){
        try {
            return scanner.nextInt();
        } catch (Exception e){
            clearInput();
            System.out.println("ERROR: Insira apenas um número na consola");
            return getSelectedOption();
        }
    }

    protected double getDouble(){
        try{
            return scanner.nextDouble();
        } catch (Exception e){
            clearInput();
            System.out.println("ERROR: Insira um número válido na comsola");
            return getDouble();
        }
    }

    private void clearInput(){
        scanner.next();
    }

    protected void yesOrNoQuestion(String message, Consumer<Boolean> booleanConsumer){
        try{
            System.out.print(message + " [Y/N]: ");
            if(scanner.next().toUpperCase().equals("Y")){
                booleanConsumer.accept(true);
            }else{
                booleanConsumer.accept(false);
            }
        } catch (Exception e){
            yesOrNoQuestion(message, booleanConsumer);
        }
    }

    protected boolean yesOrNoQuestion(String message){
        try{
            System.out.print(message + " [Y/N]: ");
            if(scanner.next().toUpperCase().equals("Y")){
                return true;
            }else{
                return false;
            }
        } catch (Exception e){
            return yesOrNoQuestion(message);
        }
    }

    protected void printPage(int i, List<? extends Object> list){
        int length = 10;
        if( i > list.size() / length) i = list.size() / length;
        if( i < 0 ) i = 0;
        for(int j = 0; i*length+j < list.size() && j < length; j++ ){
            int listI = i*length+j ;
            Object object = list.get(listI);
            System.out.println((listI + 1) + ". " + object);
        }
        printMessage("Use \":page <numero>\" para mudar de página", '#');
    }

    protected int getOptionInPage(List<? extends Object> list){
        int ativoSelected = 0;
        boolean optionSelected = false;
        while (!optionSelected){
            String input = scanner.nextLine();
            if(input.matches("[0-9]+")){
                ativoSelected = Integer.parseInt(input);
                optionSelected = true;
            } else if(input.matches("[ ]*:[ ]*page[ ]+[0-9]+[ ]*")){
                Pattern pattern = Pattern.compile("[ ]*:[ ]*page[ ]+([0-9]+)[ ]*");
                Matcher matcher = pattern.matcher(input);
                if(matcher.find()) {
                    int pageNumber = Integer.parseInt(matcher.group(1));
                    printPage(0, list);
                }
            }
        }
        return ativoSelected;
    }

    public abstract void render();

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
