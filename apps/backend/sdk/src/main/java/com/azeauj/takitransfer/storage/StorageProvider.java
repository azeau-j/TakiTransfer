package com.azeauj.takitransfer.storage;

import com.azeauj.takitransfer.config.TakiTransferProperties;

public interface StorageProvider {
    String storeData(byte[] data);

    byte[] getData(String key);

    void deleteData(String key);

    void setProperties(TakiTransferProperties transferProperties);

    String getSystemName();
}
