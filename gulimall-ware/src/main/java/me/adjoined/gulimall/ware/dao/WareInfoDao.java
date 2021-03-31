package me.adjoined.gulimall.ware.dao;

import me.adjoined.gulimall.ware.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库信息
 * 
 * @author tianbian
 * @email adjoined.tb@gmail.com
 * @date 2021-03-30 21:59:31
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
