package scrapper;

import business.Acao;
import business.AtivoFinanceiro;
import business.EESTrading;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

public abstract class JSONAtivoFinanceiroScrapper implements AtivoFinanceiroScrapper{
    private Map<String, AtivoFinanceiro> ativosFinanceiros;
    private List<AtivoFinanceiro> changed;
    private boolean running;
    private String url;
    private EESTrading trading;

    // USED MORE FOR DEBUGGING PORPUSES
    private int numberOfStocks;
    private int numberOfStocksChanges;

    public JSONAtivoFinanceiroScrapper(String url){
        this.url = url;

        trading = EESTrading.getInstance();
        ativosFinanceiros = new HashMap<>();
        List<AtivoFinanceiro> temp = trading.getAtivos();
        for(AtivoFinanceiro a : temp)
            ativosFinanceiros.put(a.getCompany(), a);
        running = false;
        changed = new LinkedList<>();

        numberOfStocks = 0;
        numberOfStocksChanges = 0;
    }


    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }

    public final void start(){
        new Thread(() -> {
            try{
                setRunning(true);
                atualizaAtivos();
            } catch (Exception e){
                setRunning(false);
                e.printStackTrace();
            }
        }).start();
    }

    private void atualizaAtivos() throws InterruptedException {
        while(isRunning()) {
            numberOfStocks = numberOfStocksChanges = 0;
            String json = getJson();
            JSONObject obj = new JSONObject(json);
            addAtivos(obj);
            putAtualizacao();
            Thread.sleep(10000);
        }
    }

    private void putAtualizacao() {
        if(changed.size() > 0){
            trading.putAtivosFinanceiros(changed);
            changed = new LinkedList<>();
        }
    }

    private void addAtivos(JSONObject obj) {
        jsonToAtivosFinanceiros(obj).forEach(ativo -> {
            numberOfStocks++;
            addAtivoFinanceiro(ativo);
        });
    }

    protected abstract Set<AtivoFinanceiro> jsonToAtivosFinanceiros(JSONObject obj);

    public synchronized void stop(){
        if(!isRunning()) System.out.println("Scrapper wasn't running");
        setRunning(false);
    }

    private synchronized String getJson(){
        try{
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .get();
            Element info = doc.body();
            return info.text();
        } catch (Exception e){
            System.out.println("Não foi possível connectar o scrapper a +\"" + url + "\"");
            System.exit(0);
            return null;
        }
    }

    private synchronized void addAtivoFinanceiro(AtivoFinanceiro ativoFinanceiro){
        if(!ativosFinanceiros.containsKey(ativoFinanceiro.getCompany())){
            ativosFinanceiros.put(ativoFinanceiro.getCompany(), ativoFinanceiro);
            changed.add(ativoFinanceiro);
        } else {
            if(ativosFinanceiros.get(ativoFinanceiro.getCompany()).getValue() != ativoFinanceiro.getValue()){
                numberOfStocksChanges++;
                changed.add(ativoFinanceiro);
            }
            ativosFinanceiros.replace(ativoFinanceiro.getCompany(), ativoFinanceiro);
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String company : ativosFinanceiros.keySet()){
            sb.append(company);
            sb.append(": ");
            sb.append(ativosFinanceiros.get(company).getValue());
            sb.append("\n");
        }
        return sb.toString();
    }
}
