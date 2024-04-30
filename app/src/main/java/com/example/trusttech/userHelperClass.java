package com.example.trusttech;

public class userHelperClass {
    String name, username, email, phone, age, password, confirm_password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public userHelperClass(String name, String username, String email, String phone, String age, String password, String confirm_password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public userHelperClass() {
    }
}
