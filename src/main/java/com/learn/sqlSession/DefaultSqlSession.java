package com.learn.sqlSession;

import com.learn.pojo.Configuration;
import com.learn.pojo.MappedStatment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> E selectOne(String statementId, Object... params) {
        return (E) selectList(statementId, params).get(0);
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatment mappedStatment = configuration.getMappedStatmentMap().get(statementId);
        return simpleExecutor.selectList(configuration, mappedStatment, params);
    }


}
