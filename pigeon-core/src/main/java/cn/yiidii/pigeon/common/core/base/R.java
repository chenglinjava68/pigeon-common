
package cn.yiidii.pigeon.common.core.base;

import cn.yiidii.pigeon.common.core.constant.CommonConstants;
import lombok.*;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author lengleng
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("通用响应模型")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @ApiModelProperty(value = "状态码")
    private int code;

    @Getter
    @Setter
    @ApiModelProperty(value = "信息")
    private String msg;

    @Getter
    @Setter
    @ApiModelProperty(value = "错误信息")
    private String errMsg;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回数据")
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, CommonConstants.SUCCESS, null, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, CommonConstants.SUCCESS, null, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, CommonConstants.SUCCESS, msg, null);
    }

    public static <T> R<T> failed() {
        return restResult(null, CommonConstants.FAIL, null, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, CommonConstants.FAIL, msg, null);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, CommonConstants.FAIL, null, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.FAIL, msg, null);
    }

    public static <T> R<T> failed(int code, String msg) {
        return restResult(null, code, msg, null);
    }

    public static <T> R<T> failed(int code, String msg, String errMsg) {
        return restResult(null, code, msg, errMsg);
    }

    private static <T> R<T> restResult(T data, int code, String msg, String errMsg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setErrMsg(errMsg);
        return apiResult;
    }

}
