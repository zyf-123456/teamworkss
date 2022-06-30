package com.projectm;

import com.dynamicDataSource.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 启动程序
 * 
 * @author gencya
 */
@ComponentScan(basePackages={"com.framework","com.projectm","com.dynamicDataSource","com.test"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@Import(DynamicDataSourceRegister.class)
public class APPLauncher
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(APPLauncher.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  任务协同项目管理系统启动成功   ლ(´ڡ`ლ)ﾞ  \n" );
    }
}