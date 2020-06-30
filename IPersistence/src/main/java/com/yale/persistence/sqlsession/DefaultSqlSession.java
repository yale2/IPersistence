package com.yale.persistence.sqlsession;

import com.yale.persistence.config.Configuration;
import com.yale.persistence.config.MappedStatement;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> List<T> selectList(String statementId, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getStatementMap().get(statementId);
        return simpleExecutor.query(configuration, mappedStatement, params);
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() != 1) {
            throw new RuntimeException("no result or to many result selected in selectOne method");
        }
        return (T) objects.get(0);
    }

    @Override
    public int insert(String statementId,Object...object) throws ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        SimpleExecutor simpleExecutor=new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getStatementMap().get(statementId);
        return simpleExecutor.insert(configuration,mappedStatement,object);
    }

    private int update(String statementId, Object[] args) throws ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        SimpleExecutor simpleExecutor=new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getStatementMap().get(statementId);
        return simpleExecutor.update(configuration,mappedStatement,args);
    }

    @Override
    public int delete(String statementId,Object... params) throws ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        SimpleExecutor simpleExecutor=new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getStatementMap().get(statementId);
        return simpleExecutor.delete(configuration,mappedStatement,params);
    }

    /**
     * 传入mapper对象
     * 1.通过解析好的configuration对象，能够获取当前mapper对应的mapper.xml  每条sql（mapper中的select标签）都绑定到了一个mappedStatement对象中
     * 2.通过反射,调用的不是mapper本身的方法，而是通过保存好的方法及sql的对应关系，调用对用的sql
     * @param classType
     * @param <T>
     * @return
     */
    @Override
    public <T> T getMapper(Class<?> classType) {
        Object o = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{classType}, (proxy, method, args) -> {
            String methodName = method.getName();
            String className = method.getDeclaringClass().getName();
            String statementId = className + "." + methodName;
            Type genericReturnType = method.getGenericReturnType();

            if (genericReturnType instanceof ParameterizedType) {
                List<Object> objects = DefaultSqlSession.this.selectList(statementId, args);
                return objects;
            }
            if(methodName.startsWith("insert")){
                return DefaultSqlSession.this.insert(statementId,args);
            }else if(methodName.startsWith("update")){
                return DefaultSqlSession.this.update(statementId,args);
            }else if(methodName.startsWith("delete")){
                return DefaultSqlSession.this.delete(statementId,args);
            }else {
                return DefaultSqlSession.this.selectOne(statementId, args);
            }

        });
        return (T) o;
    }




}
