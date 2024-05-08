package com.tpsolution.animestore.payload;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.tpsolution.animestore.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataResponse {
    private Object data;
    private MsgSts msgSts;

    public static DataResponse ok(Object object){
        MsgSts msgSts = new MsgSts();
        msgSts.setCode("200");
        msgSts.setMessage(ErrorCode.SUCCESS.getMessage());
        DataResponse dataResponse = new DataResponse();
        dataResponse.setMsgSts(msgSts);
        dataResponse.setData(object);
        return dataResponse;
    }
}
