package com.learn.sqlSession;

import com.learn.config.XMLConfigBuilder;
import com.learn.io.Resources;
import com.learn.pojo.Configuration;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream in) {
//        InputStream in = Resources.getResourceAsStream(path);
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.build(in);

        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }

}
