package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
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
}
