package me.adjoined.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.gulimall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author tianbian
 * @email adjoined.tb@gmail.com
 * @date 2021-03-30 21:59:31
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

