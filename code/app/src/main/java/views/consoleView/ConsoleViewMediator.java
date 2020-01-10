package views.consoleView;

import business.*;
import views.IView;
import views.ViewMediator;

import java.util.List;
import java.util.Observable;

public class ConsoleViewMediator extends ViewMediator {
    public ConsoleViewMediator(String view, EESTrading trading){
        super(trading, view);
    }

    private volatile Utilizador utilizador = new Utilizador("", "", 0);
    private ConsoleView lastiView;

    private CFD lastCFD;
    private AtivoFinanceiro lastAtivo;


    @Override
    public void changeView(String viewId) {
        if(viewId == null) return;
        currentView = viewId;
        switch (viewId){
            case ConsoleView.INICIAL:
                lastiView = new ViewInicial(trading, utilizador, this);
                break;
            case ConsoleView.DEPOSITAR:
                lastiView = new ViewDepositar(trading, utilizador, this);
                break;
            case ConsoleView.ATIVOS_DISPONIVEIS:
                lastiView = new ViewAtivosDisponiveis(trading, utilizador, this, trading.getAtivos());
                break;
            case ConsoleView.MEUS_CFDS:
                lastiView = new ViewMeusCFDs(trading, utilizador, this ,trading.getPortfolio(utilizador));
                break;
            case ConsoleView.TRANSACOES_ANTIGAS:
                lastiView = new ViewTransacoesAntigas(trading, utilizador, this, trading.getTransacoesAntigas(utilizador));
                break;
            case ConsoleView.LOGIN:
                lastiView = new ViewLogin(trading, utilizador, this);
                break;
            case ConsoleView.REGISTAR:
                lastiView = new ViewRegistar(trading, utilizador, this);
                break;
            case ConsoleView.UTILIZADOR:
                lastiView = new ViewUtilizador(trading, utilizador, this);
                break;
            case ConsoleView.WITHDRAW:
                lastiView = new ViewWithdraw(trading, utilizador, this);
                break;
            case ConsoleView.FAVORITOS:
                lastiView = new ViewFavoritos(trading, utilizador, this);
                break;
            default: System.out.println("ERROR: No view with name " + viewId + "was found !!! Exiting..." );
        }
        lastiView.render();
    }

    public void changeView(String viewId, CFD cfd) {
        cfd = trading.getCFD(cfd.getId());
        this.lastCFD = cfd;
        switch (viewId){
            case ConsoleView.CFD_POSSUIDO:
                lastiView = new ViewCFDPossuido(trading, utilizador, this, cfd );
                break;
            default: System.out.println("ERROR: No view with name " + viewId + "was found !!! Exiting..." );
        }
        lastiView.render();
    }

    public void changeView(String viewId, AtivoFinanceiro ativoFinanceiro) {
        ativoFinanceiro = trading.getAtivo(ativoFinanceiro.getCompany());
        this.lastAtivo = ativoFinanceiro;
        switch (viewId){
            case ConsoleView.COMPRA_CFD:
                lastiView = new ViewCompraCFD(trading, utilizador,this,  ativoFinanceiro );
                break;
            case ConsoleView.ATIVO_FINANCEIRO:
                lastiView = new ViewAtivo(trading, utilizador, this, ativoFinanceiro);
                break;
            default: System.out.println("ERROR: No view with name " + viewId + "was found !!! Exiting..." );
        }
        lastiView.render();
    }


    @Override
    public synchronized void update(Observable o, Object arg) {
        if(arg instanceof List){
            List list = (List) arg;
            if(list.size() > 0 && list.get(0) instanceof AtivoFinanceiro){
                List<AtivoFinanceiro> ativos = (List<AtivoFinanceiro>) list;
                for(AtivoFinanceiro a: ativos)
                    if(utilizador.getFavoritos().contains(a)){
                        AtivoFinanceiroFavorito favorito = utilizador.getFavorito(a);
                        if(favorito.reachedThreshold(a.getValue())){
                            System.out.println(a + " passou o treshold " + favorito.getValueToNotify());
                        }
                    }
                switch (currentView){
                    case ConsoleView.ATIVOS_DISPONIVEIS:
                        lastiView.setUpdated(true);
                        break;
                    case ConsoleView.CFD_POSSUIDO:
                        for(AtivoFinanceiro f : ativos){
                            if(lastCFD.getAtivoFinanceiro().equals(f)){
                                lastiView.setUpdated(true);
                                break;
                            }
                        }
                        break;
                    case ConsoleView.COMPRA_CFD:
                        for(AtivoFinanceiro f : ativos){
                            if(lastAtivo.equals(f)){
                                lastiView.setUpdated(true);
                                break;
                            }
                        }
                        break;
                    case ConsoleView.MEUS_CFDS:
                        List<CFD> cfds = trading.getPortfolio(utilizador);
                        for(CFD cfd : cfds){
                            for(AtivoFinanceiro f : ativos){
                                if(cfd.getAtivoFinanceiro().equals(f)){
                                    lastiView.setUpdated(true);
                                    break;
                                }
                            }
                        }
                }
            }
        }
        if(utilizador != null && ! utilizador.getUsername().equals("")){
            if(arg instanceof CFDVendido){
                CFDVendido cfdVendido = (CFDVendido) arg;
                System.out.println(cfdVendido + "foi vendido (atingiu treshold)");
            }
            if(arg instanceof AtivoFinanceiro){
                AtivoFinanceiro ativoFinanceiro = (AtivoFinanceiro) arg;
                if(utilizador.hasFavorito(ativoFinanceiro))
                    System.out.println("O ativo financeiro " + ativoFinanceiro.getCompany() + " sofreu uma alteração significativa");
            }
        }

        Utilizador util = trading.login(utilizador.getUsername(), utilizador.getPassword());
        if(util != null){
            utilizador.deconstruct(util);
        }
    }
}
