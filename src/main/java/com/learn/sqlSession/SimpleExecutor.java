package com.learn.sqlSession;

import com.learn.config.BoundSql;
import com.learn.pojo.Configuration;
import com.learn.pojo.MappedStatment;
import com.learn.utils.GenericTokenParser;
import com.learn.utils.ParameterMapping;
import com.learn.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {

    @Override
    public <E> List<E> selectList(Configuration configuration, MappedStatment mappedStatment, Object... params) {
        try {
            Connection conn = configuration.getDataSource().getConnection();
            BoundSql boundSql = getBoundSql(mappedStatment.getSql());
            PreparedStatement ps = conn.prepareStatement(boundSql.getSqlText());
            String parameterType = mappedStatment.getParameterType();
            Class<?> clazz = Class.forName(parameterType);

            // 设置参数
            List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
            for (int i = 0; i < parameterMappingList.size(); i++) {
                String name = parameterMappingList.get(i).getContent();
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                ps.setObject(i + 1, field.get(params[0]));
            }
            String resultType = mappedStatment.getResultType();
            Class<?> resultClazz = Class.forName(resultType);
            List<Object> list = new ArrayList<>();
            ResultSet resultSet = ps.executeQuery();
            // 解析查询结果集
            while (resultSet.next()) {
                Object instance = resultClazz.newInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                for (int i = 0; i < count; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    int columnType = metaData.getColumnType(i + 1);
                    String columnTypeName = metaData.getColumnTypeName(i + 1);
                    System.out.println("columnName " + columnName + " columnLabel " + columnLabel + " columnType " + columnType + " columnTypeName " + columnTypeName);
                    Object obj = resultSet.getObject(columnName);
                    if (isHaveField(resultClazz, columnName)) {
                        PropertyDescriptor pd = new PropertyDescriptor(columnName, resultClazz);
                        Method writeMethod = pd.getWriteMethod();
                        writeMethod.invoke(instance, obj);
                    }
                }
                list.add(instance);
            }
            return (List<E>) list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isHaveField(Class clazz, String name) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 完成对#{}的解析工作：1.将#{}使用？进行代替，2.解析出#{}里面的值进行存储
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        //#{}里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
        return boundSql;

    }
}
