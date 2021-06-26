package pers.liam.util;

import com.alibaba.fastjson.JSON;
import pers.liam.enums.ResponseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 自定义返回数据结构封装
 * @author liam
 * @date 2021/4/13 15:04
 */
@Data
@NoArgsConstructor
public class Res implements Serializable {
    private static final long serialVersionUID = -2927382804783195063L;
    /**
     * 自定义状态码
     */
    private int status;
    /**
     * 返回数据
     */
    private Object data;
    /**
     * 返回提示消息
     */
    private String msg;

    /**
     * 请求成功
     * @return 请求数据
     */
    public static String success() {
        ResponseStatus successResponse = ResponseStatus.SUCCESS;
        return resultSet(successResponse);
    }

    /**
     * 请求成功
     * @param data 请求数据
     * @return 请求数据
     */
    public static String success(Object data) {
        ResponseStatus successResponse = ResponseStatus.SUCCESS;
        return resultSet(successResponse, data);
    }

    /**
     * 自定义请求失败
     * @param responseStatus 状态码
     * @return 失败消息
     */
    public static String failed(ResponseStatus responseStatus) {
        return resultSet(responseStatus);
    }

    /**
     * 默认请求失败
     * @return 失败消息
     */
    public static String failed() {
        ResponseStatus failedResponse = ResponseStatus.FAILED;
        return resultSet(failedResponse);
    }

    /**
     * 数据封装（仅有状态码，无数据）
     * @param responseStatus 状态码
     * @return 封装后的json数据
     */
    private static String resultSet(ResponseStatus responseStatus) {
        Res res = new Res();
        res.setStatus(responseStatus.getStatus());
        res.setData("");
        res.setMsg(responseStatus.getMsg());
        return JSON.toJSONString(res);
    }

    /**
     * 成功数据封装
     * @param responseStatus 状态码
     * @return 封装后的json数据
     */
    private static String resultSet(ResponseStatus responseStatus, Object data) {
        Res res = new Res();
        res.setStatus(responseStatus.getStatus());
        res.setData(data);
        res.setMsg(responseStatus.getMsg());
        return JSON.toJSONString(res);
    }
}
