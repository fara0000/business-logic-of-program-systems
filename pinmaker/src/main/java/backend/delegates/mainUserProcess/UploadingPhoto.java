package backend.delegates.mainUserProcess;

import backend.utils.PhotoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
public class UploadingPhoto implements JavaDelegate {

    private final PhotoUtil photoUtil;

    private final static String PATH_TO_PHOTO_BUFFER = "photoBuffer/";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ByteArrayInputStream file = (ByteArrayInputStream) delegateExecution.getVariable("photo");

        String fileName = photoUtil.generateFileNameForCamunda();

        if (IOUtils.toByteArray(file) == null || IOUtils.toByteArray(file).length == 0) {
            delegateExecution.setVariable("photoError", "Photo cannot be empty");
            throw new BpmnError("uploadingPhotoError");
        }

        photoUtil.putPhotoAcrossCamunda(PATH_TO_PHOTO_BUFFER, fileName, file);

        delegateExecution.setVariable("fileName", fileName);
    }

}
