package me.adjoined.gulimall.ware.dao;

import me.adjoined.gulimall.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author tianbian
 * @email adjoined.tb@gmail.com
 * @date 2021-03-30 21:59:31
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
