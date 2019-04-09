package com.example.scanmore.Database;

public class User {

    private int id;

    //added name
    private String name;
    private String email;
    private  String password;

    public User(String name, String email, String password) {

        //added nam
        this.name = name;
        this.email = email;
        this.password = password;

    }

    public User(String email, String password){

        this.email =email;
        this.password = password;
    }

    public User(){}

    public int getId() {

        return id;

    }



    public void setId(int id) {

        this.id = id;

    }

    public String getEmail() {

        return email;

    }

    public void setEmail(String email) {

        this.email = email;

    }

    public String getPassword() {

        return password;

    }



    public void setPassword(String password) {

        this.password = password;

    }

    public void setName(String name){
               this.name = name;
    }

    public String getName(){

        return name;
    }

    @Override

    public String toString() {

        return "User{" +

                "id=" + id + '\'' +

                ", name='" + name +  '\'' +

                ", email='" + email + '\'' +

                ", password='" + password + '\'' +

                '}';

    }

    public static final String TABLE_NAME = "users";

    public static final String COLUMN_ID = "id";

    public static final  String COLUMN_NAME = "name";

    public static final String COLUMN_EMAIL = "email";

    public static final String COLUMN_PASSWORD = "password";



    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_EMAIL + " TEXT NOT NULL," +
                    COLUMN_PASSWORD + " TEXT" +
                    ")";

}