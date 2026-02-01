package com.azeauj.takitransfer.storage.filesystem;

import com.azeauj.takitransfer.config.TakiTransferProperties;
import com.azeauj.takitransfer.storage.StorageProvider;
import io.github.wasabithumb.jtoml.value.table.TomlTable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class FileStorageProvider implements StorageProvider {
    private String basePath;

    private boolean initBaseDirectory() {
        File baseDirectory = new File(basePath);

        if (baseDirectory.exists()) {
            return true;
        }

        return baseDirectory.mkdir();
    }

    @Override
    public String storeData(byte[] data) {
        UUID fileId = UUID.randomUUID();
        boolean baseDirectoryExist = initBaseDirectory();
        if (!baseDirectoryExist) {
            throw new RuntimeException("Base directory does not exist.");
        }

        File fileToStore = new File(basePath + fileId);

        try (OutputStream outputStream = new FileOutputStream(fileToStore)) {
            outputStream.write(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileId.toString();
    }

    @Override
    public byte[] getData(String key) {
        File fileToGet = new File(basePath + key);

        if (!fileToGet.exists()) {
            throw new RuntimeException("File does not exist");
        }

        try (InputStream inputStream = new FileInputStream(fileToGet)) {
            return inputStream.readAllBytes();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteData(String key) {
        File fileToDelete = new File(basePath + key);

        if (!fileToDelete.exists()) {
            throw new RuntimeException("File does not exist");
        }

        fileToDelete.delete();
    }

    @Override
    public void setProperties(TakiTransferProperties transferProperties) {
        if (transferProperties.doc().get("storage_system_config") == null) {
            throw new RuntimeException("'storage_system_config' expected in config file.");
        }

        TomlTable systemStorageProperties = transferProperties.doc().get("storage_system_config").asTable();

        if (systemStorageProperties.get("directory_path") == null) {
            throw new RuntimeException("'directory_path' expected in 'storage_system_config' section.");
        }

        basePath = systemStorageProperties.get("directory_path").asPrimitive().asString();
    }

    @Override
    public String getSystemName() {
        return "fs";
    }
}
