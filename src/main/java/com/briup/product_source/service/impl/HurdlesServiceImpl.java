package com.briup.product_source.service.impl;

import com.briup.product_source.dao.ManagerHurdlesMapper;
import com.briup.product_source.exception.ServiceException;
import com.briup.product_source.pojo.ManagerHurdles;
import com.briup.product_source.result.ResultCode;
import com.briup.product_source.service.HurdlesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class HurdlesServiceImpl implements HurdlesService {
    @Autowired
    private ManagerHurdlesMapper hurdlesMapper;

    @Override
    public PageInfo<ManagerHurdles> findByPage(
            Integer pageNum,
            Integer pageSize,
            String hName,
            String fhId
    ) {
        PageHelper.startPage(pageNum, pageSize);
        List<ManagerHurdles> list =
                hurdlesMapper.queryAllHurdles(hName, fhId);
        return new PageInfo<>(list);
    }
    @Override
    public void saveOrUpdate(ManagerHurdles hurdles) {

        // 1. 基础校验
        if (hurdles == null
                || hurdles.getHName() == null
                || "".equals(hurdles.getHName().trim())
                || hurdles.getHFenceId() == null
                || "".equals(hurdles.getHFenceId().trim())) {
            throw new RuntimeException("参数不完整");
        }

        String hId = hurdles.getHId();

        // ================= 修改 =================
        if (hId != null && !"".equals(hId.trim())) {

            ManagerHurdles db = hurdlesMapper.selectByPrimaryKey(hId);
            if (db == null) {
                throw new RuntimeException("栏圈不存在");
            }

            // 同栏舍下名称唯一
            ManagerHurdles sameName =
                    hurdlesMapper.selectByNameAndFence(
                            hurdles.getHName(),
                            hurdles.getHFenceId()
                    );
            if (sameName != null && !sameName.getHId().equals(hId)) {
                throw new RuntimeException("该栏舍下栏圈名称已存在");
            }

            hurdlesMapper.updateByPrimaryKey(hurdles);

        } else {
            // ================= 新增 =================

            ManagerHurdles sameName =
                    hurdlesMapper.selectByNameAndFence(
                            hurdles.getHName(),
                            hurdles.getHFenceId()
                    );
            if (sameName != null) {
                throw new RuntimeException("该栏舍下栏圈名称已存在");
            }

            hurdles.setHId(UUID.randomUUID().toString().replace("-", ""));
            hurdlesMapper.insert(hurdles);
        }
    }
    @Override
    public List<Integer> findAllMax() {
        return hurdlesMapper.selectAllMax();
    }
    @Override
    public void changeStatus(String hId, String hEnable) {

        // 1. 参数校验
        if (hId == null || "".equals(hId.trim()) || hEnable == null) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        // 2. 判断数据是否存在
        ManagerHurdles hurdles = hurdlesMapper.selectByPrimaryKey(hId);
        if (hurdles == null) {
            throw new ServiceException(ResultCode.DATA_IS_EMPTY);
        }

        // 3. 修改状态
        hurdles.setHEnable(hEnable);
        hurdlesMapper.updateByPrimaryKey(hurdles);
    }
    @Override
    public void deleteById(String hId) {

        // 1. 参数校验
        if (!StringUtils.hasText(hId)) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        // 2. 数据是否存在
        ManagerHurdles dbHurdle = hurdlesMapper.selectByPrimaryKey(hId);
        if (dbHurdle == null) {
            throw new ServiceException(ResultCode.DATA_IS_EMPTY);
        }

        // 3. 执行删除
        hurdlesMapper.deleteByPrimaryKey(hId);
    }
    @Override
    public void batchChangeStatus(List<ManagerHurdles> list) {

        if (list == null || list.isEmpty()) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        for (ManagerHurdles h : list) {

            // ✅ 只校验 PPT 需要的两个字段
            if (!StringUtils.hasText(h.getHId())) {
                throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
            }
            if (!StringUtils.hasText(h.getHEnable())) {
                throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
            }

            hurdlesMapper.updateEnableById(h.getHId(), h.getHEnable());
        }
    }

}
