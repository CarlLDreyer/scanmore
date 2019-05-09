package com.example.scanmore.Payment.PaymentCheckout;

public class PaymentMethodItem {

    private String id;
    private String name;
    private int photo;

    public PaymentMethodItem(String id, String name, int photo){
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
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
