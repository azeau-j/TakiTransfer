package com.azeauj.takitransfer.transfer.repository;

import com.azeauj.takitransfer.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
