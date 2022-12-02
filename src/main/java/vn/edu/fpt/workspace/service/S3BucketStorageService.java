package vn.edu.fpt.workspace.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 26/10/2022 - 02:01
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface S3BucketStorageService {

    String uploadFile(MultipartFile file);

    void downloadFile(String fileKey, HttpServletResponse response);

    File downloadFile(String fileKey);
    String sharingUsingPresignedURL(String fileKey);
}
