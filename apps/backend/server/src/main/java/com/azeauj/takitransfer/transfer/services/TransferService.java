package com.azeauj.takitransfer.transfer.services;

import com.azeauj.takitransfer.config.TakiTransferProperties;
import com.azeauj.takitransfer.core.utils.ZipUtils;
import com.azeauj.takitransfer.storage.StorageProvider;
import com.azeauj.takitransfer.transfer.entity.Transfer;
import com.azeauj.takitransfer.transfer.repository.TransferRepository;
import jakarta.annotation.Nullable;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final StorageProvider storageProvider;
    private final TakiTransferProperties takiTransferProperties;
    private final PasswordEncoder passwordEncoder;

    public TransferService(TransferRepository transferRepository, StorageProvider storageProvider, TakiTransferProperties takiTransferProperties, PasswordEncoder passwordEncoder) {
        this.transferRepository = transferRepository;
        this.storageProvider = storageProvider;
        this.takiTransferProperties = takiTransferProperties;
        this.passwordEncoder = passwordEncoder;
    }

    public Transfer createTransferFromFiles(List<MultipartFile> files, @Nullable String password, Instant expireAt) {
        byte[] zippedFiles;
        try {
            zippedFiles = ZipUtils.zipFiles(files);
        } catch (IOException e) {
            return null;
        }

        UUID newTransferId = UUID.randomUUID();

        String key = storageProvider.storeData(zippedFiles);

        String encodedPassword = password != null ? passwordEncoder.encode(password) : null;

        Transfer newTransfer = new Transfer(newTransferId, key, encodedPassword, expireAt);
        return transferRepository.save(newTransfer);
    }

    public Pair<UUID, byte[]> getTransferFile(UUID transferId, @Nullable String password) {
        Optional<Transfer> transfer = transferRepository.findById(transferId);

        if (transfer.isEmpty()) {
            throw new RuntimeException("Transfer not found.");
        }

        if (Instant.now().isAfter(transfer.get().getExpireAt())) {
            storageProvider.deleteData(transfer.get().getKey());
            transferRepository.delete(transfer.get());
            throw new RuntimeException("Transfer not found.");
        }

        Transfer t = transfer.get();
        if (t.getPassword() != null) {
            if (password == null || !passwordEncoder.matches(password, t.getPassword())) {
                throw new RuntimeException("Transfer not found.");
            }
        }

        return Pair.of(t.getId(), storageProvider.getData(t.getKey()));
    }
}
