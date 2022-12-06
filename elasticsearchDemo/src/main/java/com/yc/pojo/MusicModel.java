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
    private String mid;
    private String name;
    private String type;
    private String singer;
    private String country;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String data;
}
