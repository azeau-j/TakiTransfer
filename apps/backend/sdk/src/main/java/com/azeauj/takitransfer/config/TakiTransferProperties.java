package com.azeauj.takitransfer.config;

import io.github.wasabithumb.jtoml.document.TomlDocument;

public record TakiTransferProperties(String storageSystem, TomlDocument doc) {

}
