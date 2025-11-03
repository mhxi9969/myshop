package top.mhxi.myshop.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.web.bind.annotation.RequestHeader;
import top.mhxi.myshop.common.utils.R;

@FeignClient(name = "user")
public interface UserFeignClient {

    @PostMapping("/user/user/getUserBySession")
    R getUserBySession();
}
