package views;

import business.EESTrading;
import business.Utilizador;

import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

public abstract class ViewMediator implements Observer {
    protected String currentView;
    protected EESTrading trading;

    public ViewMediator(EESTrading trading, String initialView){
        this.trading = trading;
        this.currentView = initialView;
    }

    public abstract void changeView(String s);

    public final void start(){
        trading.addObserver(this);
        changeView(currentView);
    }

    @Override
    public abstract void update(Observable o, Object arg);
}
