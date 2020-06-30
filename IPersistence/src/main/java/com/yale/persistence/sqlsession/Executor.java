package com.yale.persistence.sqlsession;


import com.yale.persistence.config.Configuration;
import com.yale.persistence.config.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface Executor {

    <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object...params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException;

    int insert(Configuration configuration, MappedStatement mappedStatement, Object...object) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    int update(Configuration configuration, MappedStatement mappedStatement, Object...object)throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    int delete(Configuration configuration, MappedStatement mappedStatement, Object...object)throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;
}
