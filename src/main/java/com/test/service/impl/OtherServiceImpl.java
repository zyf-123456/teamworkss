package com.test.service.impl;

import com.dynamicDataSource.TargetDataSource;
import com.projectm.task.domain.Task;
import com.projectm.task.service.TaskService;
import com.test.service.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtherServiceImpl implements OtherService {
    @Autowired
    private TaskService taskService;

    @TargetDataSource(name = "db1")
    public Task getData() {
        return taskService.list().get(0);
    }

}
