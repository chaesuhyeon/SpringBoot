package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {
    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
       // lock 획득
        while(!redisLockRepository.lock(id)) {
            Thread.sleep(100); // lock 획득에 실패하면 thread sleep을 사용해서 잠시 쉬었다가 다시 시도
        }

        // lock 획득에 성공했다면
        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id); // lock 해제
        }
    }
}
