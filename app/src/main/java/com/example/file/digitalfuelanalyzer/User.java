package com.example.file.digitalfuelanalyzer;

public class User {
    public String name, email, phone,gen,dateofbirth;

    public User(){
    }

    public User(String name, String email, String phone,String gen,String dateofbirth) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gen = gen;
        this.dateofbirth = dateofbirth;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGen() {
        return gen;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

}