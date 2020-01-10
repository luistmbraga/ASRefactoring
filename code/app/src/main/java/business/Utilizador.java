package business;

import java.util.LinkedList;
import java.util.List;

public class Utilizador {

	private String username;
	private String password;
	private double money;
	private List<AtivoFinanceiroFavorito> favoritos;

	public Utilizador(String username, String password,double money){
		this.username = username;
		this.password = password;
		this.money = money;
		this.favoritos = new LinkedList<>();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public void addFavorito(AtivoFinanceiro ativoFinanceiro, double value){
		this.favoritos.add(new AtivoFinanceiroFavorito(ativoFinanceiro, value));
	}

	public AtivoFinanceiroFavorito getFavorito(AtivoFinanceiro ativoFinanceiro){
		for(AtivoFinanceiroFavorito favorito : favoritos){
			if(favorito.equals(ativoFinanceiro)){
				return favorito;
			}
		}
		return null;
	}

	public List<AtivoFinanceiroFavorito> getFavoritos(){
		return this.favoritos;
	}

	public boolean hasFavorito(AtivoFinanceiro ativoFinanceiro){
		return this.favoritos.contains(ativoFinanceiro);
	}

	public void removeFavorito(AtivoFinanceiro ativoFinanceiro){
		this.favoritos.remove(ativoFinanceiro);
	}

	public void deconstruct(Utilizador utilizador){
		this.username = utilizador.getUsername();
		this.password = utilizador.password;
		this.money = utilizador.getMoney();
		this.favoritos = utilizador.getFavoritos();
	}


}