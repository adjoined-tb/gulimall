package me.adjoined.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import me.adjoined.gulimall.ware.entity.PurchaseEntity;
import me.adjoined.gulimall.ware.service.PurchaseService;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.common.utils.R;



/**
 * 采购信息
 *
 * @author tianbian
 * @email adjoined.tb@gmail.com
 * @date 2021-03-30 21:59:31
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @RequestMapping("/future")
    @ResponseBody
    public String future() throws ExecutionException, InterruptedException {
        CompletableFuture<String> infoFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("info");
            return "info";
        }, threadPoolExecutor);

        CompletableFuture<Void> saleFuture = infoFuture.thenAcceptAsync((v) -> {
            System.out.println("sale");
        }, threadPoolExecutor);

        CompletableFuture<Void> spuFuture = infoFuture.thenAcceptAsync(v -> {
            System.out.println("spu");
        }, threadPoolExecutor);

        CompletableFuture<Void> attrFuture = infoFuture.thenAcceptAsync(v -> {
            System.out.println("attr");
        }, threadPoolExecutor);

        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            System.out.println("images");
        }, threadPoolExecutor);

        CompletableFuture.allOf(saleFuture, spuFuture, attrFuture, imagesFuture).get();
        return "ok";
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
