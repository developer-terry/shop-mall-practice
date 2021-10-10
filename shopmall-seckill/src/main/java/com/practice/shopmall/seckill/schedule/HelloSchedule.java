package com.practice.shopmall.seckill.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定時任務
 *  開啟定時任務 @EnableScheduling
 *  @Scheduled
 *      自動配置類別 TaskExecutionAutoConfiguration
 *
 * 異步任務
 *  1. 開啟異步任務功能 @EnableAsync
 *  2. 給異步執行的方法上標記 @Async
 *      自動配置類別 TaskExecutionAutoConfiguration 屬性綁定在 TaskExecutionProperties
 * 解決 使用異步+定時任務來達成不阻塞
 */
@Slf4j
@EnableScheduling
@EnableAsync
@Component
public class HelloSchedule {

    /**
     * 1. Spring 中 6 位組成 不允許第 7 位
     * 2. 週一到週日分別是 1 - 7
     * 3. 定時任務不應該是阻塞的 定時任務默認會阻塞
     *  讓業務以異步的方式運行 自己提交到線程池
     *  支持定時任務線程池 設置 TaskSchedulingProperties
     *      spring.task.scheduling.pool.size 有時不起作用
     */
    @Scheduled(cron = "* * * ? * 2")
    @Async
    public void hello() throws InterruptedException {

        log.info("hello...");
        Thread.sleep(3000);
    }
}
