package com.yjntc.excelexporter.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
    * 房间
    */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RoomV2 implements Serializable {

    /**
    * 房间id
    */
    private String roomId;

    /**
    * 楼层id
    */
    private String roomFloorId;

    /**
    * 房间名
    */
    private String roomName;

    /**
    * 面积（平方米）
    */
    private Float roomSize;

    /**
    * 管辖社区
    */
    private String communityId;

    /**
    * 小区id
    */
    private String livingQuarterId;

    /**
    * 网格id
    */
    private String gridId;

    /**
    * 房屋状态：1自用  2出租  3空闲
    */
    private Integer roomStatus;

    /**
    * 规划用途：1住宅  2商业 3厂房
    */
    private Integer roomPlandUsage;

    /**
     * 楼层id
     *  查询用
     */
    private List<String> roomFloorIds;


    private static final long serialVersionUID = 1L;
}
