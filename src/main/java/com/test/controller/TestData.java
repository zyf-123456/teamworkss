package com.test.controller;

import com.projectm.cycle.service.AISerice;
import com.projectm.cycle.service.BISerice;
import com.projectm.task.domain.Task;
import com.test.service.MasterService;
import com.test.service.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestData {

    @Autowired
    private MasterService masterService;

    @Autowired
    private OtherService otherService;

    @Autowired
    private AISerice aiSerice;

    @Autowired
    private BISerice biSerice;

    @RequestMapping("/toggle/data")
    public void testData() {
        // 主数据库查询
        Task masterData = masterService.getTaskOne();
        System.out.println(masterData);
        // 从数据库查询
        Task salveData = otherService.getData();
        System.out.println(salveData);
    }

    @RequestMapping("/test/cycle")
    public void testCycle() {
        //测试循环依赖
        aiSerice.printB();
        biSerice.printA();
        System.out.println(aiSerice);

    }

}
