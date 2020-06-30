package com.yale.persistence.test.entity;


import com.yale.persistence.io.Resources;
import com.yale.persistence.sqlsession.SqlSession;
import com.yale.persistence.sqlsession.SqlSessionFactory;
import com.yale.persistence.sqlsession.SqlSessionFactoryBuilder;
import com.yale.persistence.test.dao.UserDao;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class IPersistenceTest {

    private SqlSession sqlSession;

    @Test
    public void test() throws PropertyVetoException, DocumentException {
        InputStream resource = Resources.getResource("sqlMapConfig.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resource);
        SqlSession sqlSession = sessionFactory.openSession();
        User user=new User(1L,"yale");
        UserDao mapper = (UserDao)sqlSession.getMapper(UserDao.class);
        List<User> users = mapper.selectList();

        System.out.println(mapper.selectOne(user));
        for (User user1 : users) {
            System.out.println(user1);
        }
        user.setId(3L);
        user.setName("doung lee");
        mapper.insert(user);

    }

    @Before
    public void before() throws IOException, PropertyVetoException, DocumentException {
        InputStream resource = Resources.getResource("sqlMapConfig.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resource);
        sqlSession = sessionFactory.openSession();
    }

    @Test
    public void test2() throws IOException, ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        System.out.println(sqlSession.insert("com.yale.persistence.test.dao.UserDao.insert", new User(7L, "lucky1")));
    }

    @Test
    public void testCache(){
        User user=new User();
        user.setName("yale");
        user.setId(1L);
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        User user1 = mapper.selectOne(user);
        System.out.println("1:"+user1);


        User user3=new User();
        user3.setName("yale2");
        user3.setId(1L);
        System.out.println("2:"+mapper.update(user3));

        List<User> users = mapper.selectList();
        for (User user2 : users) {
            System.out.println(user2);
        }

        System.out.println("3:"+mapper.deleteByUser(user3));

    }
}
