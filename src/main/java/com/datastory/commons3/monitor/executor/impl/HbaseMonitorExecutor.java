package com.datastory.commons3.monitor.executor.impl;

import com.datastory.commons3.monitor.executor.MonitorExecutor;
import com.datastory.commons3.monitor.utils.JsonUtils;
import com.datastory.commons3.monitor.utils.RequestUtils;
import com.datastory.commons3.monitor.writer.impl.LocalFileWriter;
import com.datastory.commons3.monitor.utils.StringUtils;
import com.datastory.commons3.monitor.writer.Writer;
import com.yeezhao.commons.util.AdvFile;
import com.yeezhao.commons.util.ClassUtil;
import com.yeezhao.commons.util.ILineParser;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by songhao on 2017/11/3.
 * 对hbase的指标进行监控
 */
public class HbaseMonitorExecutor implements MonitorExecutor {

    //配置key和指标的映射关系
    Map<String, String> keyMappings = new LinkedHashMap<String, String>();

    public HbaseMonitorExecutor() throws IOException {
        //jvm的指标信息
        InputStream in = ClassUtil.getResourceAsInputStream("hbase.jmx.paths");
        AdvFile.loadFileInLines(in, new ILineParser() {
            public void parseLine(String s) {
                String[] arr = s.trim().split("\t");
                keyMappings.put(arr[0], arr[1]);
            }
        });
    }

    /**
     * 具体的业务执行实现方法
     *
     * @param requestUrls
     */
    public void executor(Set<String> requestUrls) {
        Writer writer = new LocalFileWriter("target.txt", "utf-8");
        for (String requestUrl : requestUrls) {
            //根据url获取请求的信息
            String jsonRet = RequestUtils.getJsonByUrl(requestUrl);
            if (StringUtils.isNullStr(jsonRet)) {
                continue;
            }
            //将请求到的数据转换为json对象
            JSONObject object = JsonUtils.getJsonObject(jsonRet);
            //对json的数据进行解析，得到对应的vlaue
            Map<String, Object> targetMap = parseJsonObject(object, keyMappings);
            writer.write(requestUrl, targetMap);
        }
    }

    /**
     * 解析json对象，获取相应的值
     *
     * @param object
     * @param keyMappings
     * @return
     */
    private Map<String, Object> parseJsonObject(JSONObject object, Map<String, String> keyMappings) {
        Map<String, Object> targetMap = new LinkedHashMap<String, Object>();
        for (String key : keyMappings.keySet()) {
            //获取json标签的值
            Object value = JsonUtils.getValue(object, key);
            //将新填值之后的信息放入到集合中去
            targetMap.put(keyMappings.get(key), value);
        }
        return targetMap;
    }

    public static void main(String[] args) throws IOException {
        HbaseMonitorExecutor executor = new HbaseMonitorExecutor();
    }
}
