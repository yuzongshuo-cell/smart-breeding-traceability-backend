package com.briup.product_source.dao;

import com.briup.product_source.pojo.ManagerHurdles;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerHurdlesMapper {
    int countByFhId(String fhId);
    List<Integer> selectAllMax();

    List<ManagerHurdles> queryAllHurdles(
            @Param("hName") String hName,
            @Param("fhId") String fhId
    );

    ManagerHurdles selectByPrimaryKey(String hId);

    ManagerHurdles selectByNameAndFence(
            @Param("hName") String hName,
            @Param("fhId") String fhId
    );

    int insert(ManagerHurdles hurdles);

    int updateByPrimaryKey(ManagerHurdles hurdles);

    int deleteByPrimaryKey(String hId);



    void updateEnableById(@Param("hId") String hId,
                          @Param("hEnable") String hEnable);

}
