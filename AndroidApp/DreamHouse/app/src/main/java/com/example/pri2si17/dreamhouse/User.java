package com.example.pri2si17.dreamhouse;

public class User {
    private String name;
    private String email;
    private String unique_id;
    private String password;
    private String old_password;
    private String new_password;
    private String dateOfBirth;
    private String mobileNumber;
    private String fName;
    private String lName;
    private String mName;
    private String sex;
    private String user_hash;
    private String OTP;

    public void setOTP(String OTP){ this.OTP = OTP;}

    public String getOTP(){return OTP;}

    public String getUser_hash(){return user_hash;}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setUnique_id(String unique_id){this.unique_id = unique_id;}

    public String getUnique_id() {
        return unique_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser_hash(String hashvalue){this.user_hash = hashvalue;}

    public void setFirstName(String fname) {this.fName = fname;}

    public void setMiddleName(String mname) {this.mName = mname;}

    public void setLastName(String lname) {this.lName = lname;}

    public void setDateOfBirth(String dob) {this.dateOfBirth = dob;}

    public void setMobileNumber(String mob) {this.mobileNumber = mob;}

    public void setSex(String sex){this.sex = sex;}

    public String getFirstName(){return fName;}

    public String getMiddleName() {return mName;}

    public String getLastName(){return lName;}

    public String getDateOfBirth(){return dateOfBirth;}

    public String getMobileNumber(){return mobileNumber;}

    public String getSex(){return sex;}

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
