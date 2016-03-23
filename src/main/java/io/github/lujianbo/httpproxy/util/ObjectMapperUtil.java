package io.github.lujianbo.httpproxy.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by jianbo on 2016/3/23.
 */
public class ObjectMapperUtil {

    public ObjectMapper HarObjectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();

        //格式化时间到ISO-8601
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }
}
