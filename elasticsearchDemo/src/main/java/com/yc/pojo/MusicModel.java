package com.yc.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author yucan
 * @date 2022/11/15 17:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicModel {
    //id
    private String mid;
    //歌曲名称
    private String name;
    //歌曲类型
    private String type;
    //歌手
    private String singer;
    //国家
    private String country;
    //时间
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String data;
}
