package com.hmall.api.client;

import com.hmall.api.client.fallback.PayClientFallbackFactory;
import com.hmall.api.dto.PayOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "pay-service", fallbackFactory = PayClientFallbackFactory.class)
public interface PayClient {
    /**
     * 根据交易订单id查询支付单
     * @param id 业务订单id
     * @return 支付单信息
     */
    @GetMapping("/pay-orders/biz/{id}")
    PayOrderDTO queryPayOrderByBizOrderNo(@PathVariable("id") Long id);

    /**
     * 修改支付单状态
     * @param orderId
     */
    @PutMapping("/pay-orders//status/{orderId}/{status}")
    void updatePayOrderStatusByOrderId(@PathVariable Long orderId, @PathVariable Integer status);

}