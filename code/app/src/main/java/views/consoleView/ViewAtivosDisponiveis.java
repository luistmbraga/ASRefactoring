package views.consoleView;

import business.AtivoFinanceiro;
import business.EESTrading;
import business.Utilizador;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ViewAtivosDisponiveis extends ConsoleView {
    private List<AtivoFinanceiro> ativos;

    public ViewAtivosDisponiveis(EESTrading trading, Utilizador utilizador, ConsoleViewMediator mediator, List<AtivoFinanceiro> ativoFinanceiros) {
        super(trading, utilizador, mediator);
        this.ativos = ativoFinanceiros;
    }

    @Override
    public void render() {
        layout("Ativos Financeiros");
        System.out.println("0.Retroceder");
        System.out.println("1.Ver tudo (crypto moedas e ações)");
        System.out.println("2.Ver ações");
        System.out.println("3.Ver crypto moedas");
        System.out.println("4.Filtrar");

        int option = getSelectedOption();
        switch (option){
            case 0:
                mediator.changeView(UTILIZADOR);
                return;
            case 1:
                break;
            case 2:
                this.ativos = this.ativos.stream().filter(ativoFinanceiro -> {
                    return ativoFinanceiro.getType().equals("Acao");
                }).collect(Collectors.toList());
                break;
            case 3:
                this.ativos = this.ativos.stream().filter(ativoFinanceiro -> {
                    return ativoFinanceiro.getType().equals("Moeda");
                }).collect(Collectors.toList());
                break;
            case 4:
                subMenuFiltrar();
                break;
            default:
                System.out.println("Opção nao encontrada");
                render();
                return;

        }
        subViewVerAtivos();
    }

    private String subMenuFiltrar(){
        boolean done = false;
        while(!done){
            System.out.println("0.Retroceder");
            System.out.println("1.Procurar por sigla");
            System.out.println("2.Procurar por preço minímo");
            System.out.println("3.Procurar por preço máximo");
            System.out.println("4.Ver");

            int filterOption = getSelectedOption();
            switch (filterOption){
                case 0: return ATIVOS_DISPONIVEIS;
                case 1:
                    System.out.print("Introduza o filtro: ");
                    String filtro = scanner.next().toUpperCase();
                    this.ativos = this.ativos.stream().filter(a -> a.getCompany().toUpperCase().startsWith(filtro))
                            .collect(Collectors.toList());
                    break;
                case 2:
                    System.out.print("Introduza o preço mínimo: ");
                    double precoMin = getDouble();
                    this.ativos = this.ativos.stream().filter(a -> a.getValue() >= precoMin)
                            .collect(Collectors.toList());
                    break;
                case 3:
                    System.out.println("Introduza o preço máximo: ");
                    double precoMax = getDouble();
                    this.ativos = this.ativos.stream().filter(a -> a.getValue() <= precoMax)
                            .collect(Collectors.toList());
                    break;
                case 4: done = true; break;
                default: System.out.println("Opção não encontrada"); break;
            }
        }
        return null;
    }

    private void subViewVerAtivos(){
        printAtivos(0);
        boolean optionSelected = false;
        int ativoSelected = 0;
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
                    printAtivos(pageNumber);
                }
            }
        }
        if(ativoSelected == 0){
            mediator.changeView(ATIVOS_DISPONIVEIS);
        }
        else if(ativoSelected > 0 && ativoSelected <= ativos.size()) {
            if(isUpdated()){
                boolean yes = yesOrNoQuestion("Alguns ativos financeiros foram atualizados, quer continuar?");
                if(!yes) subViewVerAtivos();
            }
            AtivoFinanceiro ativoFinanceiro = ativos.get(ativoSelected -1);
            mediator.changeView(ATIVO_FINANCEIRO, ativoFinanceiro);
        }
        else {
            System.out.println("ERROR: Escolha um ativo entre 1 - " + ativos.size());
            mediator.changeView(ATIVOS_DISPONIVEIS);
        }
    }


    private void printAtivos(int page){
        layout(utilizador.getUsername() + " $: " + utilizador.getMoney());
        System.out.println("0.Retroceder");
        printPage(page, this.ativos);
    }
}
