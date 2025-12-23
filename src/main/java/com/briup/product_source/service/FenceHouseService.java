package com.briup.product_source.service;

import com.briup.product_source.pojo.ManagerFenceHouse;
import com.briup.product_source.pojo.ext.ManagerFenceHouseExt;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface FenceHouseService {
    /**
     * 多条件分页查询
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @param fhName 栏舍名称
     * @return 分页对象
     */
    PageInfo<ManagerFenceHouse> findByPage(int pageNum, int
            pageSize, String fhName);

    /**
     * 根据id查询栏舍信息及其对应的栏圈信息
     * @param fhId 栏舍id
     * @return ManagerFenceHouseExtend类型是一个自定义类型，用来实现1对
    多的映射
     */
    ManagerFenceHouseExt findById(String fhId);
    void saveOrUpdate(ManagerFenceHouse house);
    void removeById(String fhId);
    void removeByIds(List<String> fhIds);

    List<ManagerFenceHouse> findAll();

}
