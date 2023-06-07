package com.example.bharath.response;

public class Data  {
    private int totalRecords;
    private String  message;
    public Data(String message,int totalRecords) {
        this.message = message;
        this.totalRecords = totalRecords;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
