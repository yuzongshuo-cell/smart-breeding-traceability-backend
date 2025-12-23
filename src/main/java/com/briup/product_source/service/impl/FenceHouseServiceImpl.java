package com.briup.product_source.service.impl;

import com.briup.product_source.dao.ManagerFenceHouseMapper;
import com.briup.product_source.dao.ManagerHurdlesMapper;
import com.briup.product_source.dao.ext.ManagerFenceHouseExtMapper;
import com.briup.product_source.pojo.ManagerFenceHouse;
import com.briup.product_source.pojo.ext.ManagerFenceHouseExt;
import com.briup.product_source.service.FenceHouseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FenceHouseServiceImpl implements FenceHouseService {
    @Autowired
    private ManagerFenceHouseMapper houseMapper;
    @Autowired
    private ManagerHurdlesMapper hurdlesMapper;
    @Override
    public PageInfo<ManagerFenceHouse> findByPage(int pageNum,
                                                  int pageSize, String fhName) {
//1.PageHelper开启分页
        PageHelper.startPage(pageNum, pageSize);
//2.核心查询语句
        List<ManagerFenceHouse> list =
                houseMapper.queryAllHouses(fhName);
//3.将查询的信息封装在PageInfo对象中，返回
        PageInfo<ManagerFenceHouse> pageInfo = new PageInfo<>
                (list);
        return pageInfo;
    }

    @Autowired
    private ManagerFenceHouseExtMapper houseExtMapper;
    @Override
    public ManagerFenceHouseExt findById(String fhId) {
        return houseExtMapper.selectHouseAndHurdlesById(fhId);
    }
    @Override
    public void saveOrUpdate(ManagerFenceHouse house) {

        // 1. 基础校验：名称不能为空
        if (house == null || house.getFhName() == null || "".equals(house.getFhName().trim())) {
            throw new RuntimeException("栏舍名称不能为空");
        }

        String fhId = house.getFhId();

        // ================= 修改 =================
        if (fhId != null && !"".equals(fhId.trim())) {

            // 1.1 判断数据是否存在
            ManagerFenceHouse dbHouse = houseMapper.selectByPrimaryKey(fhId);
            if (dbHouse == null) {
                throw new RuntimeException("要修改的栏舍不存在");
            }

            // 1.2 名称是否被其他栏舍占用
            ManagerFenceHouse sameName = houseMapper.selectByFhName(house.getFhName());
            if (sameName != null && !sameName.getFhId().equals(fhId)) {
                throw new RuntimeException("栏舍名称已存在");
            }

            // 1.3 执行修改
            houseMapper.updateByPrimaryKey(house);

        } else {
            // ================= 新增 =================

            // 2.1 名称唯一性校验
            ManagerFenceHouse sameName = houseMapper.selectByFhName(house.getFhName());
            if (sameName != null) {
                throw new RuntimeException("栏舍名称已存在");
            }

            // 2.2 生成主键
            house.setFhId(UUID.randomUUID().toString().replace("-", ""));

            // 2.3 执行新增
            houseMapper.insert(house);
        }
    }
    @Override
    public void removeById(String fhId) {

        // 1. 判断栏舍是否存在
        ManagerFenceHouse house = houseMapper.selectByPrimaryKey(fhId);
        if (house == null) {
            throw new RuntimeException("栏舍不存在");
        }

        // 2. 判断是否有关联栏圈
        int count = hurdlesMapper.countByFhId(fhId);
        if (count > 0) {
            throw new RuntimeException("该栏舍下存在栏圈，不能删除");
        }

        // 3. 执行删除
        houseMapper.deleteByPrimaryKey(fhId);
    }
    @Override
    public void removeByIds(List<String> fhIds) {

        if (fhIds == null || fhIds.size() == 0) {
            throw new RuntimeException("参数不能为空");
        }

        boolean deleted = false;

        for (String fhId : fhIds) {

            ManagerFenceHouse house = houseMapper.selectByPrimaryKey(fhId);
            if (house == null) {
                continue; // 不存在，跳过
            }

            int count = hurdlesMapper.countByFhId(fhId);
            if (count > 0) {
                continue; // 有栏圈，跳过
            }

            houseMapper.deleteByPrimaryKey(fhId);
            deleted = true;
        }

        if (!deleted) {
            throw new RuntimeException("没有可删除的栏舍");
        }
    }
    @Override
    public List<ManagerFenceHouse> findAll() {
        return houseMapper.findAll();
    }

}
