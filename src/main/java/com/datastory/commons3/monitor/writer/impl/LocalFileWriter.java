package com.datastory.commons3.monitor.writer.impl;

import com.datastory.commons3.monitor.writer.Writer;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * Created by songhao on 2017/11/6.
 * 写本地文件的Writer
 */
public class LocalFileWriter implements Writer {

    static Logger logger = Logger.getLogger(LocalFileWriter.class);

    private String path;
    private String encoding;

    public LocalFileWriter(String path, String encoding) {
        this.path = path;
        this.encoding = encoding;
    }

    /**
     * 将数据写到本地存储系统中去
     *
     * @param targetMap
     */
    public void write(String host, Map<String, Object> targetMap) {
        File file = new File(this.path);
        file = file.getAbsoluteFile();
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(path), true), encoding));
            writer.write("机器[" + host + "]的hbase指标信息如下:");
            writer.newLine();
            for (String targetKey : targetMap.keySet()) {
                writer.write(targetKey + ":" + targetMap.get(targetKey));
                writer.newLine();
            }
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
