package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询对应的套餐id
     * @param ids
     * @return
     */
    List<Long> getSetmealIdByDishIds(List<Long> ids);

    /**
     * 批量插入套餐菜品信息
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
