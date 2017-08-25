package ex.app.service;

import ex.app.model.HealthCheckResponse;


import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAppService {
    public HealthCheckResponse healthCheck() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        return new HealthCheckResponse("App is up", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(runtimeBean.getStartTime())), runtimeBean.getUptime());
    }
}
