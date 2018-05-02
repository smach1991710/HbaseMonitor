package com.datastory.commons3.monitor.writer;

import java.util.Map;

/**
 * Created by songhao on 2017/11/3.
 * 写存储系统的接口
 */
public interface Writer {

    public void write(String host, Map<String, Object> targetMap);
}
