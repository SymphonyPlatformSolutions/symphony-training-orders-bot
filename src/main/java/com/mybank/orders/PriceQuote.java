package com.mybank.orders;

public class PriceQuote {
  private int price;
  private String ticker;

  public PriceQuote(int price, String ticker) {
    this.price = price;
    this.ticker = ticker;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }
}
