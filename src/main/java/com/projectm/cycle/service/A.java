package com.projectm.cycle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class A implements AISerice{

	@Autowired
    private B b;

//    public A(B b) {
//        this.b = b;
//    }

	//打印B的消息
    @Override
    public void printB() {
        System.out.println(b);
    }

    @Async
    public void aMathod(){

    }

}
