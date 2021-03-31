package me.adjoined.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.gulimall.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * ÉÌÆ·ÆÀ¼Û
 *
 * @author tianbian
 * @email adjoined.tb@gmail.com
 * @date 2021-03-30 21:05:37
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

