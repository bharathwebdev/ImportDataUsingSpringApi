package com.example.bharath.response;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HomeResponse extends Response {

    List<Map<String, Object>> Data;

   private int offset;
  private  int limit;

    public Integer getOffset() {
        return offset;
    }


    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Map<String, Object>> getData() {
        return Data;
    }

    public void setData(List<Map<String, Object>> data) {
        Data = data;
    }
}
