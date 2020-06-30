package com.yale.persistence.config;

public class MappedStatement {

    public MappedStatement(String sql,String parameterType,String resultType){
        this.sql=sql;
        this.parameterType=parameterType;
        this.resultType=resultType;
    }

    private String resultType;

    private String sql;

    private String parameterType;

    private String id;

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
