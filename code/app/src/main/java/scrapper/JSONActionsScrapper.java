package scrapper;

import business.Acao;
import business.AtivoFinanceiro;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.*;

public class JSONActionsScrapper extends JSONAtivoFinanceiroScrapper{
	private double testValue = 0.10;
	private double TSLValue = 100;

	public JSONActionsScrapper(){
		super("https://financialmodelingprep.com/api/v3/stock/real-time-price");
	}

	@Override
	protected Set<AtivoFinanceiro> jsonToAtivosFinanceiros(JSONObject obj) {
		Set<AtivoFinanceiro> res = new HashSet<>();
		JSONArray ativos = obj.getJSONArray("stockList");
		ativos.forEach(jsonObj -> {
			JSONObject jsonAtivo = (JSONObject) jsonObj;
			AtivoFinanceiro ativoFinanceiro = new Acao();
			ativoFinanceiro.setCompany(jsonAtivo.getString("symbol"));
			ativoFinanceiro.setValue(Math.floor(jsonAtivo.getDouble("price") * 100) / 100);
			res.add(ativoFinanceiro);
		});
		Acao acao = new Acao("TEST", testValue);
		testValue += 0.01;
		res.add(acao);
		Acao tsl = new Acao("TSL", TSLValue);
		if(TSLValue - 1 > 1)
			TSLValue -= 1;
		res.add(tsl);
		return res;
	}

}