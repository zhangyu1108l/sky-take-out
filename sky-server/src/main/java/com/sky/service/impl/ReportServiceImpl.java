package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        dateList.add(begin);
        while (begin.isBefore(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        for (LocalDate Date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(Date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(Date, LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        String join = StringUtils.join(dateList, ",");
        String turnoverListString = StringUtils.join(turnoverList, ",");
        return TurnoverReportVO.builder().dateList(join).turnoverList(turnoverListString).build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while (begin.isBefore(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("end", endTime);
            Integer totalUserCount = userMapper.countByMap(map);
            totalUserCount = totalUserCount == null ? 0 : totalUserCount;
            map.put("begin", beginTime);
            Integer newUserCount = userMapper.countByMap(map);
            newUserCount = newUserCount == null ? 0 : newUserCount;
            newUserList.add(newUserCount);
            totalUserList.add(totalUserCount);
        }
        String dateListString = StringUtils.join(dateList, ",");
        String totalUserListString = StringUtils.join(totalUserList, ",");
        String newUserListString = StringUtils.join(newUserList, ",");
        UserReportVO userReportVO = UserReportVO.builder().dateList(dateListString).totalUserList(totalUserListString).newUserList(newUserListString).build();
        return userReportVO;
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while (begin.isBefore(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // 总订单数
            Integer orderCount = getOrderCount(beginTime, endTime, null);
            orderCount = orderCount == null ? 0 : orderCount;
            // 有效订单数
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);
            validOrderCount = validOrderCount == null ? 0 : validOrderCount;
            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }
        //计算区间内的总订单数和有效订单数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer totalValidOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        //计算订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = totalValidOrderCount.doubleValue() / totalOrderCount;
        }
        OrderReportVO build = OrderReportVO.builder().dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
        return build;
    }

    /**
     * 销量排名
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names, ",");
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers, ",");
        return SalesTop10ReportVO.builder().nameList(nameList).numberList(numberList).build();
    }

    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        Map map = new HashMap<>();
        map.put("beginTime", begin);
        map.put("endTime", end);
        map.put("status", status);
        return orderMapper.countByMap(map);
    }
}
