package com.azeauj.takitransfer.transfer.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transfer")
public class Transfer {
    @Id
    private UUID id;

    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Column(name = "password")
    private String password;

    @Column(name = "expire_at")
    private Instant expireAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Transfer() {
    }

    public Transfer(UUID id, String key, @Nullable String password, @Nullable Instant expireAt) {
        this.id = id;
        this.key = key;
        this.password = password;
        this.expireAt = expireAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getPassword() {
        return password;
    }

    public Instant getExpireAt() {
        return expireAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Transfer transfer)) return false;

        return transfer.getId().equals(getId());
    }

    public int hashCode() {
        return 29 * getId().hashCode();
    }

}