package com.ssp.mapper;

import com.ssp.entities.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Entity com.ssp.entities.Orders
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    @Update("update orders set status = #{status} where number = #{id}")
    void updateState(@Param("id") String tradeNo, @Param("status") int status);
}




