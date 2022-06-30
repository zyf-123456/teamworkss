package com.projectm.cycle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class B implements BISerice {

    @Autowired
    private A a;


    @Override
    public void printA() {

    }

}
