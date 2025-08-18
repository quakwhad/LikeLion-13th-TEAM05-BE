package com.likelion.artipick.culture.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CultureScheduler {

    private final CultureService cultureService;

    @Scheduled(fixedRate = 14 * 24 * 60 * 60 * 1000L) // 2주마다
    public void syncCultureData() {
        cultureService.scheduledSync()
                .subscribe();
    }
}
