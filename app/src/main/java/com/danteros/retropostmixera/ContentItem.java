package com.danteros.retropostmixera;


import java.util.List;

public class ContentItem  {
final int total;

    public List<Items> items;

    public ContentItem(int total) {
        this.total = total;
    }

    public void setItems( List<Items> items) {
         this.items = items;
    }


    public class Items {

        private double profit_month;
        private double total_profit;
        private double profit;
        private double profit_month_money;
        private long date;


        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public double getProfit_month() {
            return profit_month;
        }

        public void setProfit_month(double profit_month) {
            this.profit_month = profit_month;
        }

        public double getProfit_month_money() {
            return profit_month_money;
        }

        public void setProfit_month_money(double profit_month_money) {
            this.profit_month_money = profit_month_money;
        }

        public double getTotal_profit() {
            return total_profit;
        }

        public void setTotal_profit(double total_profit) {
            this.total_profit = total_profit;
        }

        @Override
        public String toString() {
            return "Items{" +
                    "date=" + date +
                    ", profit_month=" + profit_month +
                    ", total_profit=" + total_profit +
                    ", profit=" + profit +
                    ", profit_month_money=" + profit_month_money +
                    '}';
        }
    }
}
