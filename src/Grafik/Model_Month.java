/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Grafik;

/**
 *
 * @author Rifal
 */
public class Model_Month {

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the monthtext
     */
    public String getMonthtext() {
        return monthtext;
    }

 
    public void setMonthtext(String monthtext) {
        this.monthtext = monthtext;
    }
     public Model_Month(int month, String monthtext) {
       this.month = month;
         this.monthtext = monthtext;
    }
    private int month;
    private String monthtext;
    
    public Model_Month(){
    }
    @Override
    public String toString(){
        return monthtext;
    }
    
}
