package cn.yiidii.pigeon.common.core.base;

import cn.yiidii.pigeon.common.core.exception.code.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: YiiDii Wang
 * @create: 2021-02-16 19:32
 */
@Slf4j
@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public R error() {
        return R.failed(ExceptionCode.NOT_FOUND.getCode(), ExceptionCode.NOT_FOUND.getMsg(), "");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
