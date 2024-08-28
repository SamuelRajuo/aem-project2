package com.adobe.aem.guides.project2.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;

@Component(service=Runnable.class,immediate=true)
@Designate(ocd=DemoConfiguration.class)
public class SchedularExample implements Runnable{
    private static final Logger log=LoggerFactory.getLogger(SchedularExample.class);

    public static Logger getLog() {
        return log;
    }
    @Reference
    Scheduler scheduler;
    @Activate
    public void activate(DemoConfiguration config){
        addScheduler(config);
    }
    public void addScheduler(DemoConfiguration config){
        log.info("Scheduler is created...");
        if(config.enabled_scheduler()){
        ScheduleOptions scheduleOptions=scheduler.EXPR(config.expression());
        scheduleOptions.name(config.getService_name());
        scheduleOptions.canRunConcurrently(config.concurrent());
        scheduler.schedule(this, scheduleOptions);
        }
    }
    @Deactivate
    public void deactivate(DemoConfiguration config){
        unscheduler(config);
    }
    public void unscheduler(DemoConfiguration config){
        log.info("Job is unschedule");
        scheduler.unschedule(config.getService_name());
    }
    @Override
    public void run() {
        log.info("practice scheduler is executed");
        
    }
    
}
