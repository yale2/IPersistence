package com.yale.persistence.sqlsession;

import com.yale.persistence.config.Configuration;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration){
        this.configuration=configuration;
    }

    public SqlSession openSession(){
        return new DefaultSqlSession(configuration);
    }
}
