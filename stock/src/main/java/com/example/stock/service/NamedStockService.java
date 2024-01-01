package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NamedStockService {
    private final StockRepository stockRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW) // 부모의 트랜잭션과 분리
    public synchronized void decrease(Long id, Long quantity) {
        // Stock 조회
        Stock stock = stockRepository.findById(id).orElseThrow();

        // 재고를 감소 시킨 뒤
        stock.decrease(quantity);

        // 갱신된 값 저장
        stockRepository.saveAndFlush(stock);
    }
}
