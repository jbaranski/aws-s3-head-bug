package com.jeffbaranski.bugreport;


import com.github.pjfanning.pekkohttpspi.PekkoHttpClient;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

public class Main {
    public static void main(String[] args) {
        String bucketName = args[0];
        String keyName = args[1];
        Region region = Region.of(args[2]);
        try (S3AsyncClient s3 = S3AsyncClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .httpClient(PekkoHttpClient.builder().build())
                .build()) {
            try {
                HeadObjectRequest objectRequest = HeadObjectRequest
                        .builder()
                        .key(keyName)
                        .bucket(bucketName)
                        .build();
                HeadObjectResponse headObjectResponse = s3.headObject(objectRequest).get();

                System.out.println("Content-Length=" + headObjectResponse.contentLength() + ";Content-Type=" + headObjectResponse.contentType());
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
