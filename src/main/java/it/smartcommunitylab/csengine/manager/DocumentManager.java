package it.smartcommunitylab.csengine.manager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.DownloadObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;

@Component
public class DocumentManager {
	
	@Value("${minio.endpoint}")
	private String endpoint;
	
	@Value("${minio.accessKey}")
	private String accessKey;
	
	@Value("${minio.secreteKey}")
	private String secreteKey;
	
	@Value("${minio.bucketName}")
	private String bucketName;
	
	private MinioClient minioClient;
	
	
	@PostConstruct
	public void init() {
		minioClient = MinioClient.builder()
        .endpoint(endpoint)
        .credentials(accessKey, secreteKey)
        .build(); 
		try {
			boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
			if (!found) {
        // Make a new bucket called 'asiatrip'.
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      }
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void uploadFile(MultipartFile file, String fileKey) throws Exception {
		File tmpFile = createTmpFile(file);
		minioClient.uploadObject(
        UploadObjectArgs.builder()
            .bucket(bucketName)
            .contentType(file.getContentType())
            .object(fileKey)
            .filename(tmpFile.getAbsolutePath())
            .build());
		tmpFile.delete();
	}
	
	private File createTmpFile(MultipartFile file) throws Exception {
		Path tempFile = Files.createTempFile("cse_upload_", ".tmp");
		tempFile.toFile().deleteOnExit();
		Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
		return tempFile.toFile();
	}
	
	private String createTmpFile() throws Exception {
		Path tempFile = Files.createTempFile("cse_download_", ".tmp");
		tempFile.toFile().deleteOnExit();
		return tempFile.toFile().getAbsolutePath();
	}
	
	public void deleteFile(String fileKey) throws Exception {
		minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileKey).build());
	}
	
	public String downloadFile(String fileKey) throws Exception {
		String tmpFile = createTmpFile();
		minioClient.downloadObject(
			  DownloadObjectArgs.builder()
			  .bucket(bucketName)
			  .object(fileKey)
			  .filename(tmpFile)
			  .build());
		return tmpFile;
	}


}
