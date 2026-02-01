package com.azeauj.takitransfer.transfer.controllers;

import com.azeauj.takitransfer.transfer.entity.Transfer;
import com.azeauj.takitransfer.transfer.mappers.TransferMapper;
import com.azeauj.takitransfer.transfer.models.FullTransferDto;
import com.azeauj.takitransfer.transfer.models.TransferRequestDto;
import com.azeauj.takitransfer.transfer.services.TransferService;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    private final TransferService transferService;
    private final TransferMapper transferMapper;

    public TransferController(TransferService transferService, TransferMapper transferMapper) {
        this.transferService = transferService;
        this.transferMapper = transferMapper;
    }

    @PostMapping
    public FullTransferDto createTransfer(@RequestParam("files") List<MultipartFile> files, @RequestParam(value = "password", required = false) String password, @RequestParam(value = "expire_at", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant expireAt) {
        Transfer transfer = transferService.createTransferFromFiles(files, password, expireAt);

        return transferMapper.toFullDto(transfer);
    }

    @PostMapping("/{transferId}")
    public ResponseEntity<byte[]> getTransfer(@PathVariable UUID transferId, @RequestBody(required = false) TransferRequestDto transferRequestDto) {
        String password = transferRequestDto != null ? transferRequestDto.password() : null;
        Pair<UUID, byte[]> transferFileData = transferService.getTransferFile(transferId, password);

        return ResponseEntity.ok()
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + transferFileData.getFirst() + ".zip\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(transferFileData.getSecond());
    }
}