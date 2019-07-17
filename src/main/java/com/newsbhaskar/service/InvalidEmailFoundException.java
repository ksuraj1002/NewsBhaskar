package com.newsbhaskar.service;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@ComponentScan
public class InvalidEmailFoundException extends Exception {
    public InvalidEmailFoundException(String ex){
        super(ex);
    }
}
