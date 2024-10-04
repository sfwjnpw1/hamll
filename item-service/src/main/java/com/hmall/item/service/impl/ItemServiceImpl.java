package com.hmall.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.exception.BizIllegalException;
import com.hmall.common.utils.BeanUtils;
import com.hmall.item.domain.dto.ItemDTO;
import com.hmall.item.domain.dto.OrderDetailDTO;
import com.hmall.item.domain.po.Item;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.service.IItemService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author 虎哥
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

    @Override
    public void deductStock(List<OrderDetailDTO> items) {
        String sqlStatement = "com.hmall.item.mapper.ItemMapper.updateStock";
        boolean r = false;
        try {
            r = executeBatch(items, (sqlSession, entity) -> sqlSession.update(sqlStatement, entity));
        } catch (Exception e) {
            log.error("更新库存异常", e);
            return;
        }
        if (!r) {
            throw new BizIllegalException("库存不足！");
        }
    }

    @Override
    public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
        // 导nacos的包，模拟业务延迟
        // ThreadUtils.sleep(500);
        return BeanUtils.copyList(listByIds(ids), ItemDTO.class);
    }
    @Override
    public void restoreStock(List<OrderDetailDTO> orderDetailDTOS) {
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOS) {
            Item item = lambdaQuery().eq(Item::getId, orderDetailDTO.getItemId()).one();
            lambdaUpdate()
                    .set(Item::getStock,item.getStock()+orderDetailDTO.getNum())
                    .eq(Item::getId, orderDetailDTO.getItemId())
                    .update();
        }
    }
}
