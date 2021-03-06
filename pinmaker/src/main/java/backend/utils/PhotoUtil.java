package backend.utils;

import backend.exceptions.ApplicationException;
import backend.exceptions.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
@Slf4j
public class PhotoUtil {

    private final static int MBYTE_20 = 20971520;

    public synchronized void putPhoto(String path, String name, MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream((path + name)));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(ErrorEnum.SERVICE_SAVING_PHOTO_EXCEPTION.createApplicationError());
        }
    }

    public synchronized byte[] getPhoto(String path, String name) {
        byte[] buff = new byte[MBYTE_20];
        try {
            File file = new File(path + name);
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
            int bytes = stream.read(buff);
            while (bytes != -1) {
                bytes = stream.read(buff);
            }
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buff;
    }

    public synchronized void savePhoto(String path, byte[] photo, String name) {
        try {
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream((path + name)));
            stream.write(photo);
            stream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(ErrorEnum.SERVICE_SAVING_PHOTO_EXCEPTION.createApplicationError());
        }
    }

    public void clearBuffer(String path) {
        File[] files = new File(path).listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                    log.info("junk file  " + file.getName() + " was delete");
                }
            }
        }

    }

    public void deletePhoto(String path, String name) {
        new File(path + name).delete();
    }

}
