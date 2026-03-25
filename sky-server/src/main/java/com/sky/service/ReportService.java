package com.sky.service;


import com.sky.dto.GoodsSalesDTO;
import com.sky.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 销量排名10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end);

    /**
     * 导出运营数据报表
     * @param response
     */
    void exportBusinessData(HttpServletResponse response);
}
