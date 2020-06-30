//package com.yale.persistence.test.interceptor;
//
//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//
//import java.util.Properties;
//
//@Intercepts({@Signature(
//                type = Executor.class,
//                method = "query",
//                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
//        ), @Signature(
//                type = Executor.class,
//                method = "query",
//                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
//        )}
//)
//public class MyInterceptor implements Interceptor {
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        System.out.println("myInterceptor calls");
//        Object[] args = invocation.getArgs();
//        MappedStatement ms = (MappedStatement)args[0];
//        Object parameter = args[1];
//        RowBounds rowBounds = (RowBounds)args[2];
//        ResultHandler resultHandler = (ResultHandler)args[3];
//        Executor executor = (Executor)invocation.getTarget();
//        CacheKey cacheKey;
//        BoundSql boundSql;
//        if (args.length == 4) {
//            boundSql = ms.getBoundSql(parameter);
//            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
//        } else {
//            cacheKey = (CacheKey)args[4];
//            boundSql = (BoundSql)args[5];
//        }
//
//        return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//
//    }
//}
