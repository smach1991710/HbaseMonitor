package com.datastory.commons3.monitor.utils;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by songhao on 2017/11/2.
 * 监控配置信息
 */
public class MonitorConf {

    static Logger logger = Logger.getLogger(MonitorConf.class);

    //url的配置文件
    public static String URL_PATH = "/mon-config.xml";
    public static String URL_KEY = "url";

    //需要请求的url集合信息
    public static Set<String> requestUrls = new HashSet<String>();

    //json节点之间的分隔符
    public static final String JSON_NODE_SEGMENTATION = ".";

    /**
     * 获取请求的url集合
     */
    public Set<String> getRequestUrls(){
        InputStream is = null;
        SAXReader reader = null;
        try{
            is = MonitorConf.class.getResourceAsStream(URL_PATH);
            reader = new SAXReader();
            Document doc = reader.read(is);
            Element root = doc.getRootElement();
            for (Iterator<Element> i = root.elementIterator(URL_KEY); i.hasNext();) {
                String url = i.next().getData().toString();
                requestUrls.add(url);
            }
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
        return requestUrls;
    }
}
