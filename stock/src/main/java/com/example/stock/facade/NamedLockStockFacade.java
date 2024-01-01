package com.example.stock.facade;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.NamedStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {
    private final LockRepository lockRepository;
    private final NamedStockService stockService;

    @Transactional
    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString()); // lock 획득
            stockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString()); // lock 해제
        }
    }
}
