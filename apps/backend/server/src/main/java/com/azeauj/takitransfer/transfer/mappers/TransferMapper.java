package com.azeauj.takitransfer.transfer.mappers;

import com.azeauj.takitransfer.transfer.entity.Transfer;
import com.azeauj.takitransfer.transfer.models.FullTransferDto;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {
    public FullTransferDto toFullDto(Transfer transfer) {
        return new FullTransferDto(transfer.getId(), transfer.getKey(), transfer.getExpireAt(), transfer.getCreatedAt());
    }
}
