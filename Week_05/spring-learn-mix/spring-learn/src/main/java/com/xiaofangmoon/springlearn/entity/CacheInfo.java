package com.xiaofangmoon.springlearn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaofang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheInfo {
    Long timestamp;
    Object data;

}
