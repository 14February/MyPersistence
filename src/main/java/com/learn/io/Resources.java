package com.learn.io;

import java.io.InputStream;

public class Resources {

    public static InputStream getResourceAsStream(String xml) {
        return Resources.class.getClassLoader().getResourceAsStream(xml);
    }


}
