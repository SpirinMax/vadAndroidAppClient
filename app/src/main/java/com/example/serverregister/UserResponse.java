package com.example.serverregister;

public class UserResponse {
    private int id;
    private String userfirstname;
    private String userlastname;
    private String userpatronymic;
    private String useremail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserfirstname() {
        return userfirstname;
    }

    public void setUserfirstname(String userfirstname) {
        this.userfirstname = userfirstname;
    }

    public String getUserlastname() {
        return userlastname;
    }

    public void setUserlastname(String userlastname) {
        this.userlastname = userlastname;
    }

    public String getUserpatronymic() {
        return userpatronymic;
    }

    public void setUserpatronymic(String userpatronymic) {
        this.userpatronymic = userpatronymic;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }
}
