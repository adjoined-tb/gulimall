package me.adjoined.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.gulimall.product.entity.CommentReplayEntity;

import java.util.Map;

/**
 * ÉÌÆ·ÆÀ¼Û»Ø¸´¹ØÏµ
 *
 * @author tianbian
 * @email adjoined.tb@gmail.com
 * @date 2021-03-30 21:05:37
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
