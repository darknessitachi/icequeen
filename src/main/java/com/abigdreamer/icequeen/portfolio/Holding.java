package com.abigdreamer.icequeen.portfolio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.abigdreamer.icequeen.utils.Util;
import com.google.common.base.Joiner;

public class Holding {
	
	private LocalDateTime dateTime;
	private Map<String, Double> symbolHoldings;
	private double comission;
	private double cash;
	private double total;
	private double change;

	private double returns;
	private double equityCurve;

	public Holding() {
		this.symbolHoldings = new LinkedHashMap<>();

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		List<String> holdInfo = new ArrayList<>();
		for (Map.Entry<String, Double> sh : symbolHoldings.entrySet()) {
			holdInfo.add(sh.getKey() + "=" + Util.getDecimal(sh.getValue(), 2));
		}
		
		sb.append(Joiner.on(",").join(holdInfo));

		sb.append("\tComission="+this.comission+",Cash="+Util.getDecimal(this.cash,2)+",Total="+Util.getDecimal(this.total,2)+",Change=" + Util.getDecimal(this.change,2));

		return sb.toString();
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Map<String, Double> getSymbolHoldings() {
		return symbolHoldings;
	}

	public void setSymbolHoldings(Map<String, Double> symbolHoldings) {
		this.symbolHoldings = symbolHoldings;
	}

	public double getComission() {
		return comission;
	}

	public void setComission(double comission) {
		this.comission = comission;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getReturns() {
		return returns;
	}

	public void setReturns(double returns) {
		this.returns = returns;
	}

	public double getEquityCurve() {
		return equityCurve;
	}

	public void setEquityCurve(double equityCurve) {
		this.equityCurve = equityCurve;
	}
}
