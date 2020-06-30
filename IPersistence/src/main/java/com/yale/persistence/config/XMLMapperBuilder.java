package com.yale.persistence.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration){
        this.configuration=configuration;
    }

    public void build(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        List<Element> selectNodes = rootElement.selectNodes("//select");
        for (Element selectNode : selectNodes) {
            String id = selectNode.attributeValue("id");
            String resultType = selectNode.attributeValue("resultType");
            String parameterType = selectNode.attributeValue("parameterType");
            String sql=selectNode.getTextTrim();
            configuration.getStatementMap().put(namespace+"."+id,new MappedStatement(sql,parameterType,resultType));
        }

        List<Element> insertNodes=rootElement.selectNodes("//insert");
        for (Element insertNode : insertNodes) {
            String id = insertNode.attributeValue("id");
            String parameterType = insertNode.attributeValue("parameterType");
            String sql=insertNode.getTextTrim();
            configuration.getStatementMap().put(namespace+"."+id,new MappedStatement(sql,parameterType,null));
        }

        List<Element> updateNodes=rootElement.selectNodes("//update");
        for (Element updateNode : updateNodes) {
            String id = updateNode.attributeValue("id");
            String parameterType = updateNode.attributeValue("parameterType");
            String sql=updateNode.getTextTrim();
            configuration.getStatementMap().put(namespace+"."+id,new MappedStatement(sql,parameterType,null));
        }

        List<Element> deleteNodes=rootElement.selectNodes("//delete");
        for (Element deleteNode : deleteNodes) {
            String id = deleteNode.attributeValue("id");
            String parameterType = deleteNode.attributeValue("parameterType");
            String sql=deleteNode.getTextTrim();
            configuration.getStatementMap().put(namespace+"."+id,new MappedStatement(sql,parameterType,null));
        }
    }
}
