package com.yale.persistence.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yale.persistence.io.Resources;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder(){
        this.configuration=new Configuration();
    }

    public Configuration build(InputStream inputStream) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        ComboPooledDataSource comboPooledDataSource=new ComboPooledDataSource();
        Properties properties=new Properties();
        List<Element> dataSourceElements = rootElement.selectNodes("//property");
        for (Element element : dataSourceElements) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(comboPooledDataSource);

        List<Element> mapperLocation = rootElement.selectNodes("//mapper");
        for (Element element : mapperLocation) {
            String resource = element.attributeValue("resource");
            InputStream mapperInputStream = Resources.getResource(resource);
            XMLMapperBuilder xmlMapperBuilder=new XMLMapperBuilder(configuration);
            xmlMapperBuilder.build(mapperInputStream);
        }

        return configuration;
    }
}
