package com.yale.persistence.sqlsession;

import com.yale.persistence.config.BoundSql;
import com.yale.persistence.config.Configuration;
import com.yale.persistence.config.MappedStatement;
import com.yale.persistence.util.GenericTokenParser;
import com.yale.persistence.util.ParameterMapping;
import com.yale.persistence.util.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {

    private Connection connection;

    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
        PreparedStatement preparedStatement = getPreparedStatement(configuration, mappedStatement, params);

        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType=mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);

        List<Object> objects=new ArrayList<Object>();

        while(resultSet.next()){
            //获取返回对象实例
            Object o = resultTypeClass.newInstance();
            //获取返回结果集字段信息 包括列名和值
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //列名
                String column=metaData.getColumnName(i);
                //值
                Object value = resultSet.getObject(column);

                //使用内省
                PropertyDescriptor propertyDescriptor=new PropertyDescriptor(column,resultTypeClass);
                //获取字段对应的set方法
                Method writeMethod = propertyDescriptor.getWriteMethod();
                //调用set方法赋值
                writeMethod.invoke(o,value);

            }
            //将单个对象添加到返回结果集中
            objects.add(o);
        }

        return (List<T>) objects;
    }

    private PreparedStatement getPreparedStatement(Configuration configuration,MappedStatement mappedStatement,Object...params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        connection=configuration.getDataSource().getConnection();
        String sql=mappedStatement.getSql();
        //获取转换和的sql  将‘#{’和‘}’用‘？’代替  转换成preparedStatement需要的sql格式  并将‘#{}’中声明的变量名称保存到parameterMapping对象中
        BoundSql boundSql=getBoundSql(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        String parameterType = mappedStatement.getParameterType();
        Class<?> paramClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            Field declaredField = paramClass.getDeclaredField(content);
            declaredField.setAccessible(true);
            //通过反射 从params[0]对象中 获取该字段对应的值  传入user:id=1,name='yale' 则id字段赋值为1
            Object o = declaredField.get(params[0]);
            System.out.println(o);
            //设置preparedStatement中sql对应的参数
            preparedStatement.setObject(i+1,o);
        }
        return preparedStatement;
    }



    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler=new ParameterMappingTokenHandler();
        GenericTokenParser parser=new GenericTokenParser("#{","}",parameterMappingTokenHandler);
        String sqlText = parser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(sqlText,parameterMappings);
    }

    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if(paramterType!=null){
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }
        return null;

    }

    @Override
    public int insert(Configuration configuration, MappedStatement mappedStatement, Object... object) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        PreparedStatement preparedStatement = getPreparedStatement(configuration,  mappedStatement, object);
        return preparedStatement.executeUpdate();
    }

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object[] args) throws SQLException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        PreparedStatement preparedStatement = getPreparedStatement(configuration,  mappedStatement, args);
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws SQLException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        PreparedStatement preparedStatement = getPreparedStatement(configuration,  mappedStatement, params);
        return preparedStatement.executeUpdate();
    }
}
