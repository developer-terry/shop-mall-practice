package com.practice.shopmall.thirdparty;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class ShopmallThirdpartyApplicationTests {

	@Autowired
	private Storage storage;

	@Test
	public void uploadObject() throws IOException {

//		String projectId = "shopmall-318214";
////		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
//
//		GoogleCredentials credentials = GoogleCredentials.
//				fromStream(new FileInputStream("/Users/terrylee/Project/google-credentials.json")).
//				createScoped(Lists.newArrayList("https://www.googleapis.com/auth/devstorage.read_write"));
//
//		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials).build().getService();

//		Bucket bucket = storage.get(BUCKET_NAME);

//		Storage storage2 = StorageOptions.getDefaultInstance().getService();

		System.out.println("Buckets:");
		Page<Bucket> buckets = storage.list();
		for (Bucket bucket : buckets.iterateAll()) {
			System.out.println(bucket.toString());
		}

//		String projectId = "shopmall-318214";
		String bucketName = "shopmall";
		String objectName = "image/龔俊.jpg";
		String filePath = "/Users/terrylee/Downloads/龔俊.jpg";
		// The ID of your GCP project
		// String projectId = "your-project-id";

		// The ID of your GCS bucket
		// String bucketName = "your-unique-bucket-name";

		// The ID of your GCS object
		// String objectName = "your-object-name";

		// The path to your file to upload
		// String filePath = "path/to/your/file"

//		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		BlobId blobId = BlobId.of(bucketName, objectName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

		System.out.println(
				"File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
	}

	@Test
	void contextLoads() {
	}

}
