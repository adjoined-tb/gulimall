package me.adjoined.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.gulimall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * εεεΊε­
 *
 * @author tianbian
 * @email adjoined.tb@gmail.com
 * @date 2021-03-30 21:59:31
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void orderLockStock();
}

