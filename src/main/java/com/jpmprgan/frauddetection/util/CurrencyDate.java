package com.jpmprgan.frauddetection.util;

import java.util.Map;

/**
 * Util class for storing current rates (EUR to other currencies) obtained from a third party API
 */
public class CurrencyDate {
	public String date;
	public Map<String, Double> rates;
	public CurrencyDate() {
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setRates(Map<String, Double> rates) {
		this.rates = rates;
	}
	public Map<String, Double> getRates() {
		return rates;
	}

	@Override
	public String toString() {
		return "CurrencyDate [date=" + date + ", rates=" + rates + "]";
	}

}
