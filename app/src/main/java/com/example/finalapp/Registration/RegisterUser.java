package com.example.finalapp.Registration;

public class RegisterUser {
    private String fullname;
    private String gender;
    private String age;
    private String birthday;
    private String address;
    private String email;
    private String profpic;

    public RegisterUser(){
        //No arg Contructor
    }

    public RegisterUser(String fullname, String gender, String age, String birthday, String address, String email, String profpic) {
        this.fullname = fullname;
        this.gender = gender;
        this.age = age;
        this.birthday = birthday;
        this.address = address;
        this.email = email;
        this.profpic = profpic;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfpic() {
        return profpic;
    }

    public void setProfpic(String profpic) {
        this.profpic = profpic;
    }
}
