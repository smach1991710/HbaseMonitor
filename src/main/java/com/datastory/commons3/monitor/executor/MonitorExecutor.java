package com.datastory.commons3.monitor.executor;

import java.util.Set;

/**
 * Created by songhao on 2017/11/2.
 * 具体的业务指标处理方法
 */
public interface MonitorExecutor {

    /**
     * 执行所有的url的方法
     * @param requestUrls
     */
    public void executor(Set<String> requestUrls);
}
