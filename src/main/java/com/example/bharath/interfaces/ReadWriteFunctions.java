package com.example.bharath.interfaces;

import java.util.concurrent.atomic.AtomicInteger;

public interface ReadWriteFunctions {
     void writeDataWithoutThread() throws Exception;
     void writeDataUsingThreadPool() throws Exception;
}

