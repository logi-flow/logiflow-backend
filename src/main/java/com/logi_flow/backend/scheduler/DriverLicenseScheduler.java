package com.logi_flow.backend.scheduler;

import com.logi_flow.backend.service.DriverLicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverLicenseScheduler {

    private final DriverLicenseService driverLicenseService;

    @Scheduled(cron = "0 0 9 * * *")
    public void LicenseExpiredDate() {
        driverLicenseService.noticeExpiredDate();
    }
}
