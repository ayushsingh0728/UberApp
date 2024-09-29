package com.project.uber.uberApp.strategies;

import com.project.uber.uberApp.entities.enums.PaymentMethod;
import com.project.uber.uberApp.strategies.implementation.CashPaymentStrategy;
import com.project.uber.uberApp.strategies.implementation.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStrategy cashPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod){
        return switch (paymentMethod)
        {
            case WALLET -> walletPaymentStrategy;
            case CASH -> cashPaymentStrategy;
        };
    }
}
