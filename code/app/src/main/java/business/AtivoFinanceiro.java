package business;

public abstract class AtivoFinanceiro {

	private String company;
	//private String companyFullName;
	private double value;
	private String type;

	public AtivoFinanceiro(String company, double value,String type) {
		this.company = company;
		this.value = value;
		this.type=type;
	}

	public AtivoFinanceiro(){
		this.company=null;
		this.value=0;
	}

	public AtivoFinanceiro(AtivoFinanceiro a){
		this(a.getCompany(), a.getValue(), a.getType());
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public double getUpdateValue(double value){
		return Math.floor(((getValue() - value) / getValue()) * 100) / 100;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof AtivoFinanceiro){
			return this.company.equals(((AtivoFinanceiro)obj).getCompany());
		}
		return false;
	}

	@Override
	public String toString(){
		return getCompany() + "(" + getValue() + "$" + ")";
	}
}