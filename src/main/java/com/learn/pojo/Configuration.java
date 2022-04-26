package com.learn.pojo;

import javax.sql.DataSource;
import java.util.Map;

public class Configuration {

    private DataSource dataSource;

    private Map<String, MappedStatment> mappedStatmentMap;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatment> getMappedStatmentMap() {
        return mappedStatmentMap;
    }

    public void setMappedStatmentMap(Map<String, MappedStatment> mappedStatmentMap) {
        this.mappedStatmentMap = mappedStatmentMap;
    }
}
