package com.example.bharath.interfaces;

import org.apache.poi.ss.usermodel.Sheet;

import java.sql.Connection;

public interface DbFunctions {
    Connection establishConnection();

    void insertRow(Connection conn, int id, Sheet sheet);

    void createTable(Connection conn);
}






