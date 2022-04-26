package com.learn.config;

import com.learn.io.Resources;
import com.learn.pojo.Configuration;
import com.learn.pojo.MappedStatment;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLConfigBuilder {

    public Configuration build(InputStream in) {
        Configuration configuration = new Configuration();
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(in);
            Element root = document.getRootElement();
            List<Element> propertyList = root.selectNodes("//property");
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            for (Element element : propertyList) {
                String name = element.attributeValue("name");
                String value = element.attributeValue("value");
                // 怎么处理？
                if (name.equals("driverClass")) {
                    dataSource.setDriverClass(value);
                } else if (name.equals("jdbcUrl")) {
                    dataSource.setJdbcUrl(value);
                } else if (name.equals("username")) {
                    dataSource.setUser(value);
                } else if (name.equals("password")) {
                    dataSource.setPassword(value);
                }
            }
            configuration.setDataSource(dataSource);
            Node mappers = root.selectSingleNode("//mappers");
            List<Element> mapperList = mappers.selectNodes("//mapper");
            Map<String, MappedStatment> mappedStatmentMap = new HashMap<>();
            for (Element element : mapperList) {
                String resource = element.attributeValue("resource");
                Document doc = reader.read(Resources.getResourceAsStream(resource));
                Element rootElement = doc.getRootElement();
                String namespace = rootElement.attributeValue("namespace");
                List<Element> selects = rootElement.selectNodes("select");
                for (Element select : selects) {
                    MappedStatment mappedStatment = new MappedStatment();
                    String id = select.attributeValue("id");
                    String resultType = select.attributeValue("resultType");
                    String parameterType = select.attributeValue("parameterType");
                    String sql = select.getTextTrim();
                    // id可以不要
                    mappedStatment.setId(namespace + "." + id);
                    mappedStatment.setSql(sql);
                    mappedStatment.setResultType(resultType);
                    mappedStatment.setParameterType(parameterType);
                    mappedStatmentMap.put(namespace + "." + id, mappedStatment);
                }
            }
            configuration.setMappedStatmentMap(mappedStatmentMap);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return configuration;
    }

}
