package com.cmmplb.gateway.server.filter.openapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

@JsonSerialize
@NoArgsConstructor
@Data
public class OpenApiResponse {

    @JsonProperty("RETURN_DATA")
    private JSONObject returnData;

    @JsonProperty(value = "RETURN_CODE")
    private String returnCode = "S000A000";

    @JsonProperty(value = "RETURN_DESC")
    private String returnDesc = "SUCCESS";

    @JsonProperty(value = "RETURN_STAMP")
    private String returnStamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");

    public OpenApiResponse(String json) {
        returnData = JSON.parseObject(json);
    }
}
