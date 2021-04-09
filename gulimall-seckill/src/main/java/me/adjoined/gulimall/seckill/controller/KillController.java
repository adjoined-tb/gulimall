package me.adjoined.gulimall.seckill.controller;

import me.adjoined.common.utils.PageUtils;
import me.adjoined.common.utils.R;
import me.adjoined.gulimall.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class KillController {
    @Autowired
    SeckillService seckillService;

    @ResponseBody
    @RequestMapping("/kill")
    public R list(){
        String orderSn = seckillService.kill();
        return R.ok().put("order number", orderSn);
    }
}
