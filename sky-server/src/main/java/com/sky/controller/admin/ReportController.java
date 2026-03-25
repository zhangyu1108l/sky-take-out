package com.sky.controller.admin;


import com.sky.dto.GoodsSalesDTO;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "数据统计接口")
public class ReportController {
    @Autowired
    private ReportService reportService;
    /**
     * 营业额统计
     * @param begin
     * @param end
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额统计，开始时间：{}", begin);
        log.info("营业额统计，结束时间：{}", end);
        TurnoverReportVO turnoverReportVO = reportService.getTurnoverStatistics(begin, end);
        return Result.success(turnoverReportVO);
    }
    /**
     * 用户统计
     * @param begin
     * @param end
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户统计，开始时间：{}", begin);
        log.info("用户统计，结束时间：{}", end);
        UserReportVO userReportVO = reportService.getUserStatistics(begin, end);
        return Result.success(userReportVO);
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> orderStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("订单统计，开始时间：{}", begin);
        log.info("订单统计，结束时间：{}", end);
        OrderReportVO orderReportVO = reportService.getOrderStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    /**
     * 销量排名10
     * @param begin
     * @param end
     */
    @GetMapping("/top10")
    @ApiOperation("Top10统计")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("Top10统计，开始时间：{}", begin);
        log.info("Top10统计，结束时间：{}", end);
        SalesTop10ReportVO salesTop10ReportVO = reportService.getTop10(begin, end);
        return Result.success(salesTop10ReportVO);
    }

    /**
     * 导出运营数据报表
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表")
    public void export(HttpServletResponse  response){
        reportService.exportBusinessData(response);
    }
}
