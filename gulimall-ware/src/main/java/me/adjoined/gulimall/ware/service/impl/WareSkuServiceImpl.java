package me.adjoined.gulimall.ware.service.impl;

import me.adjoined.common.exception.NoStockException;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.common.utils.Query;

import me.adjoined.gulimall.ware.dao.WareSkuDao;
import me.adjoined.gulimall.ware.entity.WareSkuEntity;
import me.adjoined.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void orderLockStock() {
        WareSkuEntity entity1 = new WareSkuEntity();
        entity1.setSkuId(111L);
        entity1.setStockLocked(10);

        WareSkuEntity entity2 = new WareSkuEntity();
        entity2.setSkuId(222L);
        entity2.setStockLocked(20);

        if(!this.save(entity1)) {
            throw new NoStockException(111L);
        }
        if (!this.save(entity2)) {
            throw new NoStockException(222L);
        }
//        throw new NoStockException(333L);
    }

}