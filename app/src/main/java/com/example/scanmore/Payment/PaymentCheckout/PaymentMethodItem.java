package com.example.scanmore.Payment.PaymentCheckout;

public class PaymentMethodItem {

    private String name;
    private int photo;

    public PaymentMethodItem(String name, int photo){
        this.name = name;
        this.photo = photo;
    }

    public PaymentMethodItem(){

    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setPhoto(int photo){
        this.photo = photo;
    }

    public int getPhoto(){
        return photo;
    }



}
