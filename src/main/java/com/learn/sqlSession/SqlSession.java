package com.learn.sqlSession;

import java.util.List;

public interface SqlSession {

    public <E> E selectOne(String id, Object... params);

    public <E> List<E> selectList(String id, Object... params);



}
