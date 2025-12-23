package com.briup.product_source.controller;

import com.briup.product_source.exception.ServiceException;
import com.briup.product_source.pojo.ManagerHurdles;
import com.briup.product_source.result.Result;
import com.briup.product_source.result.ResultCode;
import com.briup.product_source.service.FenceHouseService;
import com.briup.product_source.service.HurdlesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "栏圈管理模块")
@RestController
@RequestMapping("/hurdle")
public class HurdleController {
    @Autowired
    private HurdlesService hurdlesService;
    @Autowired
    private FenceHouseService houseService;

    @ApiOperation("分页查询栏圈信息")
    @GetMapping
    public Result queryByPage(
            Integer pageNum,
            Integer pageSize,
            String hName,
            String fhId
    ) {
        return Result.success(
                hurdlesService.findByPage(pageNum, pageSize, hName, fhId)
        );
    }

    @ApiOperation("新增或修改栏圈")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ManagerHurdles hurdles) {
        hurdlesService.saveOrUpdate(hurdles);
        return Result.success();
    }
    @ApiOperation("查询所有栏圈容量")
    @GetMapping("/queryAllMax")
    public Result queryAllMax() {
        return Result.success(hurdlesService.findAllMax());
    }
    @ApiOperation("修改栏圈启用状态")
    @PutMapping("/changeStatus")
    public Result changeStatus(String hId, String hEnable) {
        hurdlesService.changeStatus(hId, hEnable);
        return Result.success();
    }
    @ApiOperation("根据ID删除栏圈")
    @DeleteMapping("/deleteById/{hId}")
    public Result deleteById(@PathVariable String hId) {
        hurdlesService.deleteById(hId);
        return Result.success();
    }
    @ApiOperation("查询所有栏舍（下拉框用）")
    @GetMapping("/queryAll")
    public Result queryAllFenceHouse() {
        return Result.success(houseService.findAll());
    }
    @ApiOperation("批量修改栏圈启用状态")
    @PutMapping
    public Result batchChangeStatus(@RequestBody List<ManagerHurdles> list) {

        System.out.println("list = " + list);

        if (list == null || list.isEmpty()) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        for (ManagerHurdles h : list) {
            if (h.getHId() == null || h.getHEnable() == null) {
                throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
            }
        }

        hurdlesService.batchChangeStatus(list);
        return Result.success();
    }



}
