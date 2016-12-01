package com.abigdreamer.icequeen.portfolio;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Holding {
	
	private LocalDateTime DateTime;
	private Map<String, Double> SymbolHoldings;
	private double Comission;
	private double Cash;
	private double Total;
	private double Change;

	private double Returns;
	private double EquityCurve;

	public Holding() {
		this.SymbolHoldings = new LinkedHashMap<>();

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, Double> sh : SymbolHoldings.entrySet()) {
			sb.append(sh.getKey() + "=" + sh.getValue());
		}

		sb.append("Comission={this.Comission} Cash={this.Cash} Total={this.Total} Change={this.Change}");

		return sb.toString();
	}

	public LocalDateTime getDateTime() {
		return DateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		DateTime = dateTime;
	}

	public Map<String, Double> getSymbolHoldings() {
		return SymbolHoldings;
	}

	public void setSymbolHoldings(Map<String, Double> symbolHoldings) {
		SymbolHoldings = symbolHoldings;
	}

	public double getComission() {
		return Comission;
	}

	public void setComission(double comission) {
		Comission = comission;
	}

	public double getCash() {
		return Cash;
	}

	public void setCash(double cash) {
		Cash = cash;
	}

	public double getTotal() {
		return Total;
	}

	public void setTotal(double total) {
		Total = total;
	}

	public double getChange() {
		return Change;
	}

	public void setChange(double change) {
		Change = change;
	}

	public double getReturns() {
		return Returns;
	}

	public void setReturns(double returns) {
		Returns = returns;
	}

	public double getEquityCurve() {
		return EquityCurve;
	}

	public void setEquityCurve(double equityCurve) {
		EquityCurve = equityCurve;
	}
}
