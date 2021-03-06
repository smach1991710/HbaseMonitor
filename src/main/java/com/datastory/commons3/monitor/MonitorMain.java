package com.datastory.commons3.monitor;

import com.datastory.commons3.monitor.executor.MonitorExecutor;
import com.datastory.commons3.monitor.executor.MonitorExecutorFactory;
import com.datastory.commons3.monitor.utils.MonitorConf;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * Created by songhao on 2017/11/2.
 * 主启动程序
 */
public class MonitorMain {

    static Logger logger = Logger.getLogger(MonitorMain.class);

    volatile boolean isRunning = false;//标志程序是否运行
    private static final int interval_seconds = 120;//设置程序的时间间隔

    public static void main(String[] args) throws Exception {
        MonitorMain main = new MonitorMain();
        main.run();
    }

    public void run() throws Exception {
        logger.info("监控程序正在启动...");
        //启动程序，设置为true
        if (!isRunning) {
            isRunning = true;
        }
        int runIndex = 1;
        while (isRunning) {
            logger.info("第[" + runIndex + "]次获取监控指标信息");
            MonitorConf conf = new MonitorConf();
            Set<String> requestUrls = conf.getRequestUrls();
            if (requestUrls == null || requestUrls.size() == 0) {
                logger.info("本次未检查到有url配置信息,请检查配置是否正常!");
            } else {
                MonitorExecutor executor = new MonitorExecutorFactory().provide(conf);
                executor.executor(requestUrls);
            }
            try {
                Thread.sleep(interval_seconds * 1000);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void stop() {
        logger.info("监控程序正在停止...");
        if (isRunning) {
            isRunning = false;
        }
    }
}
