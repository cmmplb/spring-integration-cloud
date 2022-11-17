package com.cmmplb.auth2.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author penglibo
 * @date 2022-07-12 09:25:08
 * @since jdk 1.8
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshInfoDTO {

    /**
     * 用户名
     */
    List<String> usernames;
}
