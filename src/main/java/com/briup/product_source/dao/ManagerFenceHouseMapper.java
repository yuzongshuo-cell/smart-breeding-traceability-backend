package com.briup.product_source.dao;

import com.briup.product_source.pojo.ManagerFenceHouse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerFenceHouseMapper {

    // 1️⃣ 分页 + 条件查询（你已有）
    List<ManagerFenceHouse> queryAllHouses(String fhName);

    // 2️⃣ 根据主键查询（详情 / 修改前校验）
    ManagerFenceHouse selectByPrimaryKey(String fhId);

    // 3️⃣ 根据名称查询（唯一性校验）
    ManagerFenceHouse selectByFhName(String fhName);

    // 4️⃣ 新增栏舍
    int insert(ManagerFenceHouse house);

    // 5️⃣ 修改栏舍
    int updateByPrimaryKey(ManagerFenceHouse house);

    // 6️⃣ 根据主键删除
    int deleteByPrimaryKey(String fhId);


    List<ManagerFenceHouse> findAll();


}
