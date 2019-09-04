package com.aws2.demo;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class S3Demo {
    private static final Region region = Region.US_EAST_2;
    private static  S3Client client = S3Client.builder().region(region).build();
    public static void main(String[] args) throws IOException {
        List<Bucket> bucketList = S3Demo.listBucket();
        ResponseInputStream<GetObjectResponse> responseInputStream = client.getObject(GetObjectRequest
                .builder()
                .bucket(bucketList.get(0)
                        .name())
                .key("test").build());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseInputStream));
        String s = "";
        while ((s = bufferedReader.readLine()) != null){
            System.out.println(s);
        }
    }


    private static void createBucket(){

        String bucket = "bucket" + System.currentTimeMillis();
        CreateBucketRequest createBucketRequest = CreateBucketRequest
                .builder()
                .bucket(bucket)
                .createBucketConfiguration(CreateBucketConfiguration.builder().locationConstraint(region.id()).build())
                .build();
        client.createBucket(createBucketRequest);
    }

    private static List<Bucket> listBucket(){
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = client.listBuckets(listBucketsRequest);
        return listBucketsResponse.buckets();
    }

    private static void putObject(Bucket bucket){
        client.putObject(PutObjectRequest
                .builder()
                .bucket(bucket.name())
                .key("test")
                .build(), RequestBody.fromString("aaa"));
    }
}
