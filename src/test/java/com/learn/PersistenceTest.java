package com.learn;

import com.learn.io.Resources;
import com.learn.pojo.User;
import com.learn.sqlSession.SqlSession;
import com.learn.sqlSession.SqlSessionFactory;
import com.learn.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;

public class PersistenceTest {

    @Test
    public void test() {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setUsername("test1");
        user = sqlSession.selectOne("com.learn.pojo.User.getUserByName", user);
    }

}
