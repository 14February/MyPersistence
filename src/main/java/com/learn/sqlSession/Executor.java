package com.learn.sqlSession;

import com.learn.pojo.Configuration;
import com.learn.pojo.MappedStatment;

import java.util.List;

public interface Executor {

    public <E> List<E> selectList(Configuration configuration, MappedStatment mappedStatment, Object... params);
}
