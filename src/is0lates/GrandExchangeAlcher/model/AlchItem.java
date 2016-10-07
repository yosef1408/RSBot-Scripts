package is0lates.GrandExchangeAlcher.model;

public class AlchItem extends Object {
    public int index;
    public int id;
    public String name;
    public int price;
    public int alchPrice;
    public int profit;
    public int maxProfit;
    public int limit;
    public boolean members;
    public int buyPrice;
    public int calcProfit;
    public int calcMaxProfit;
    public int bought;
    public int alched;
    public int ordered;

    public void calcProfit(int natureRunePrice) {
        calcProfit = alchPrice - buyPrice - natureRunePrice;
    }

    public void calcMaxProfit(int natureRunePrice) {
        calcProfit(natureRunePrice);
        calcMaxProfit = calcProfit * limit;
    }
}
