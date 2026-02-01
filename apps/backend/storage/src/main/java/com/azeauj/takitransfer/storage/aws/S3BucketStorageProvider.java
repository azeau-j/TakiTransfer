package com.azeauj.takitransfer.storage.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.azeauj.takitransfer.config.TakiTransferProperties;
import com.azeauj.takitransfer.storage.StorageProvider;
import io.github.wasabithumb.jtoml.value.table.TomlTable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class S3BucketStorageProvider implements StorageProvider {
    private AmazonS3 s3;
    private String bucket;

    @Override
    public String storeData(byte[] data) {
        UUID newFileId = UUID.randomUUID();
        s3.putObject(bucket, newFileId.toString(), new ByteArrayInputStream(data), new ObjectMetadata());
        return newFileId.toString();
    }

    @Override
    public byte[] getData(String key) {
        S3Object object = s3.getObject(bucket, key);
        try {
            return object.getObjectContent().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteData(String key) {
        s3.deleteObject(bucket, key);
    }

    @Override
    public void setProperties(TakiTransferProperties transferProperties) {
        if (transferProperties.doc().get("storage_system_config") == null) {
            throw new RuntimeException("'storage_system_config' expected in config file.");
        }

        TomlTable systemStorageProperties = transferProperties.doc().get("storage_system_config").asTable();

        if (systemStorageProperties.get("bucket") == null) {
            throw new RuntimeException("'bucket' expected in 'storage_system_config' section.");
        }
        if (systemStorageProperties.get("access_key_id") == null) {
            throw new RuntimeException("'access_key_id' expected in 'storage_system_config' section.");
        }
        if (systemStorageProperties.get("secret_key") == null) {
            throw new RuntimeException("'secret_key' expected in 'storage_system_config' section.");
        }
        if (systemStorageProperties.get("region") == null) {
            throw new RuntimeException("'region' expected in 'storage_system_config' section.");
        }

        String bucket = systemStorageProperties.get("bucket").asPrimitive().asString();
        String accessKeyId = systemStorageProperties.get("access_key_id").asPrimitive().asString();
        String secretKey = systemStorageProperties.get("secret_key").asPrimitive().asString();
        String region = systemStorageProperties.get("region").asPrimitive().asString();

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretKey);

        s3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        this.bucket = bucket;
    }

    @Override
    public String getSystemName() {
        return "bucket-s3";
    }
}
