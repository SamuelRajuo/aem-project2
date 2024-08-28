package com.adobe.aem.guides.project2.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="Demo Schedular Configuration",description="Demo Schedular Example")
public @interface DemoConfiguration {
        @AttributeDefinition(
            name="Service Name",
        type=AttributeType.STRING,
        description="Enter Service Name")
        public String getService_name() default "Practice";
        @AttributeDefinition(
            name="Can Run Concurrent?",
        type=AttributeType.BOOLEAN,
        description="Can Run Concurrent")
        public boolean concurrent() default false;
        @AttributeDefinition(
            name="Enabled Schedular",
        type=AttributeType.BOOLEAN,
        description="Enabled Schedular")
        public boolean enabled_scheduler() default true;
        @AttributeDefinition(
            name="Enter Expression",
        type=AttributeType.STRING,
        description="Enter Expression Name")
        public String expression() default "0 0/1 * 1/1 * ? *";
}
