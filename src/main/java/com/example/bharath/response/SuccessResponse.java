package com.example.bharath.response;

public class SuccessResponse extends Response {
    private String status;
    private Data data;

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData(Data dataImportedSuccessfully) {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}


