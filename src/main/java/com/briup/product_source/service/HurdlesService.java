package com.briup.product_source.service;

import com.briup.product_source.pojo.ManagerHurdles;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface HurdlesService {
    PageInfo<ManagerHurdles> findByPage(
            Integer pageNum,
            Integer pageSize,
            String hName,
            String fhId
    );

    void saveOrUpdate(ManagerHurdles hurdles);
    List<Integer> findAllMax();

    void changeStatus(String hId, String hEnable);

    void deleteById(String fhId);

    void batchChangeStatus(List<ManagerHurdles> list);

}
