package com.example.scanmore.Database;

public class CreditCard {

    public static final String TABLE_NAME = "creditcards";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CARD_NUMBER = "card_number";
    public static final String COLUMN_CARD_NAME = "card_name";
    public static final String COLUMN_CARD_VALIDITY = "card_validity";
    public static final String COLUMN_CARD_TYPE = "card_type";

    private int id;
    private String cardNumber;
    private String cardName;
    private String cardValidity;
    private int cardType;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CARD_NUMBER + " TEXT UNIQUE,"
                    + COLUMN_CARD_NAME + " TEXT,"
                    + COLUMN_CARD_VALIDITY + " TEXT,"
                    + COLUMN_CARD_TYPE + " INTEGER"
                    + ")";

    public CreditCard(){}

public CreditCard(int id, String cardNumber, String cardName, String cardValidity, int cardType){
    this.id = id;
    this.cardNumber = cardNumber;
    this.cardName = cardName;
    this.cardValidity = cardValidity;
    this.cardType = cardType;
}

public int getId() {
        return id;
    }

public String getCardNumber(){
    return cardNumber;
}

public String getCardName(){
    return cardName;
}

public String getCardValidity(){
    return cardValidity;
}

public int getCardType(){
    return cardType;
}

public void setId(int id) {
        this.id = id;
    }

public void setCardNumber(String cardNumber){
    this.cardNumber = cardNumber;
}

public void setCardName(String cardName){
    this.cardName = cardName;
}

public void setCardValidity(String cardValidity){
    this.cardValidity = cardValidity;
}

public void setCardType(int cardType){
    this.cardType = cardType;
}



}
