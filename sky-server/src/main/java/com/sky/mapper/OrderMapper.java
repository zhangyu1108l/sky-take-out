package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 新增订单
     */
    void insert(Orders orders);
    /**
     * 分页条件查询并按下单时间排序
     *
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 修改订单信息
     */
    void update(Orders orders);


    /**
     * 根据条件统计营业额
     */
    Double sumByMap(Map map);

    @Select("select * from orders where status = #{pendingPayment} and create_time &lt; #{localDateTime}")
    List<Orders> getByStatusAndCreateTimeLT(Integer pendingPayment, LocalDateTime localDateTime);

    /**
     * 根据条件统计订单数量
     */
    Integer countByMap(Map map);
}
