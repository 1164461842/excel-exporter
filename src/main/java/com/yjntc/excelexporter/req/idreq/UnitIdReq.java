package com.yjntc.excelexporter.req.idreq;

import com.yjntc.excelexporter.common.constant.Regexp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-07 10:42
 */
public class UnitIdReq {
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @NotNull (message = "单元id不能为空")
    @Pattern (regexp = Regexp.UUID_REGEX,message = "单元id错误")
    private String unitId;


    /**
     * 房间状态
     */
    private Integer roomStatus;

    public Integer getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }
}
