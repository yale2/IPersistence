package com.yale.persistence.sqlsession;

import com.yale.persistence.config.XMLConfigBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory  build(InputStream inputStream) throws PropertyVetoException, DocumentException {

        XMLConfigBuilder xmlConfigBuilder=new XMLConfigBuilder();
        return new DefaultSqlSessionFactory(xmlConfigBuilder.build(inputStream));
    }
}
