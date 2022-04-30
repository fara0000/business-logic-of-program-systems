package backend.scheduler;

import backend.utils.PhotoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class CleaningJunkFile {

    private final static String PATH_TO_PHOTO_BUFFER = "photoBuffer";

    private final PhotoUtil photoUtil;

    /**
     * Очищаем буффер для файлов
     */

    //@Scheduled(cron = "@daily")
    @Scheduled(cron = "10 * * * * *")
    public void removeJunkFile() {
        log.info("Scheduler started his work");
        photoUtil.clearBuffer(PATH_TO_PHOTO_BUFFER);
        log.info("Scheduler finished his work");
    }

}
