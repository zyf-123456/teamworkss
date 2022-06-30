package com.dynamicDataSource;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 动态数据源注册<br/>
 * 启动动态数据源请在启动类中（如SpringBootSampleApplication）
 * 添加 @Import(DynamicDataSourceRegister.class)
 *
 * @author 单红宇(365384722)
 * @myblog http://blog.csdn.net/catoop/
 * @create 2016年1月24日
 */
public class DynamicDataSourceRegister
        implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    // 如配置文件中未指定数据源类型，使用该默认值
    private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";
    //主数据源(默认数据源)
    private DataSource defaultDataSource;
    //自定义数据源
    private Map<String, DataSource> customDataSources = new HashMap<>();
    //公共数据源配置
    private Map<String, Object> commonConfig;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        // 将主数据源添加到更多数据源中
        targetDataSources.put("dataSource", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);

        logger.info("Dynamic DataSource Registry");
    }

    /**
     * 创建DataSource
     *
     * @return
     * @author SHANHY
     * @create 2016年1月24日
     */
    @SuppressWarnings("unchecked")
    public DataSource buildDataSource(Map<String, Object> dsMap) {
        try {
            Object type = dsMap.get("type");
            if (type == null)
                type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource

            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);

            String driverClassName = dsMap.get("driver-class-name").toString();
            Map masterMap = (Map) dsMap.get("master");
            String url = masterMap.get("url").toString();
            String username = masterMap.get("username").toString();
            String password = masterMap.get("password").toString();
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载多数据源配置
     */
    @Override
    public void setEnvironment(Environment env) {
        Binder binder = Binder.get(env);
        //1.加载默认数据源
        initDefaultDataSource(env, binder);
        //2.加载自定义数据源
        initCustomDataSources(env, binder);
    }

    /**
     * 初始化主数据源
     *
     * @author SHANHY
     * @create 2016年1月24日
     */
    private void initDefaultDataSource(Environment env, Binder binder) {
        //这里获取主配置的信息,spring.datasource.master为公共配置前缀
        commonConfig = binder.bind("spring.datasource", Map.class).get();
        //生成DataSource,用于注册
        defaultDataSource = buildDataSource(commonConfig);
        //给DataSource赋值其它公共配置
        bindDataSourceConfig(defaultDataSource, commonConfig);
    }

    /**
     * 为DataSource绑定更多数据
     *
     */
    private void bindDataSourceConfig(DataSource dataSource, Map config) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(config);
        Binder binder = new Binder(source);
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(dataSource));
    }

    /**
     * 初始化更多数据源
     */
    private void initCustomDataSources(Environment env, Binder binder) {
        //获取配置文件中其它数据源配置spring.datasource.other. 为多数据源的公共前缀
        Map otherConfig = binder.bind("spring.datasource.other", Map.class).get();
        //获取配置中多数据源的名称集合
        String dsPrefixs = otherConfig.get("names").toString();
        //循环每个数据源获取配置，生成DataSource
        for (String dsPrefix : dsPrefixs.split(",")) {// 多个数据源
            //获取每个数据源配置信息
            Map<String, Object> dsMap = binder.bind("spring.datasource.other." + dsPrefix, Map.class).get();
            //创建一个Map,用于个数据源与公共数据源配置合并
            Map<String, Object> mergeMap = new HashMap<>();
            //默认将公共配置先放入Map
            mergeMap.putAll(commonConfig);
            //将各个数据源配置和公共配置合并，如果有相同的覆盖公共的
            mergeMap.putAll(dsMap);
            //生成每个数据源的DataSource对象
            DataSource ds = buildDataSource(mergeMap);
            //将多数据源存入全局变量中，用于注册
            customDataSources.put(dsPrefix, ds);
            bindDataSourceConfig(ds, mergeMap);
        }
    }
}