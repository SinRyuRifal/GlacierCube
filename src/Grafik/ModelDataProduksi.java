package Grafik;

public class ModelDataProduksi {

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

 

    public ModelDataProduksi(String month, double amount, double cost) {
        this.month = month;
        this.amount = amount;
        this.cost = cost;
       
    }

    public ModelDataProduksi() {
    }

    private String month;
    private double amount;
    private double cost;
 
}
