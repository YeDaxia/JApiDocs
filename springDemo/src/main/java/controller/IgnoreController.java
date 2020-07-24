package controller;

import io.github.yedaxia.apidocs.Ignore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import result.ApiResult;

/**
 * 忽略接口
 * @author yeguozhong yedaxia.github.com
 */
@Ignore
@RestController
public class IgnoreController {

    @GetMapping("/ignore/hello")
    public ApiResult hello(){
        return null;
    }

}
