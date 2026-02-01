package com.azeauj.takitransfer.transfer.models;

import jakarta.annotation.Nullable;

import java.time.Instant;
import java.util.UUID;

public record FullTransferDto(UUID id, String key, @Nullable Instant expireAt, Instant createdAt) {
}
