package com.project.uber.uberApp.services.implementation;

import com.project.uber.uberApp.dto.WalletTransactionDto;
import com.project.uber.uberApp.entities.WalletTransaction;
import com.project.uber.uberApp.repositories.WalletTransactionRepository;
import com.project.uber.uberApp.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImplementation implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);
    }
}
