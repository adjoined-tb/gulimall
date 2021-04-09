package me.adjoined.gulimall.seckill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling //TaskSchedulingAutoConfiguration
@EnableAsync //TaskExecutionAutoConfiguration
@Configuration
public class ScheduledConfig {
}
