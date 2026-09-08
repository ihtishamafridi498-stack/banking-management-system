package com.afridi.bankmanagementsystem.scheduler;

import com.afridi.bankmanagementsystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InterestAccrualScheduler {

    private final AccountService accountService;

    @Scheduled(cron = "0 0 0 * * *")
    public void runScheduledInterestAccrual() {
        log.info("Scheduled interest accrual triggered.");
        accountService.applyInterestToAllEligibleAccounts();
    }
}