package backend.delegates.mainUserProcess;

import backend.utils.PhotoUtil;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class UploadingPhoto implements JavaDelegate {

    private final PhotoUtil photoUtil;

    private final static String PATH_TO_PHOTO_BUFFER = "photoBuffer/";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ByteArrayInputStream file = (ByteArrayInputStream) delegateExecution.getVariable("photo");
        String fileName = (String) delegateExecution.getVariable("photoName");
        String contentType = (String) delegateExecution.getVariable("contentType");

        //TODO проверочки для фоточки

        String newfileName = photoUtil.generateFileNameForCamunda(fileName, contentType);

        photoUtil.putPhotoAcrossCamunda(PATH_TO_PHOTO_BUFFER, fileName, file);

        delegateExecution.setVariable("fileName", fileName);
    }

}
