package com.example.bharath.response;


public class HomeData  {
    private int id;
    private String  name;

    private Long rollno;

    public HomeData(int id, String name, Long rollno) {
        this.id = id;
        this.name = name;
        this.rollno = rollno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRollno() {
        return rollno;
    }

    public void setRollno(Long rollno) {
        this.rollno = rollno;
    }
}
