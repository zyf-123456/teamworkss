package com.test.service.impl;

import com.projectm.task.domain.Task;
import com.projectm.task.service.TaskService;
import com.test.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterServiceImpl implements MasterService {

    @Autowired
    private TaskService taskService;

    // 数据库不切换
    @Override
    public Task getTaskOne() {
        return taskService.list().get(0);
    }

}
