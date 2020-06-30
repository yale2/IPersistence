package com.yale.persistence.io;


import java.io.InputStream;

public class Resources {

    public static InputStream getResource(String path){
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
