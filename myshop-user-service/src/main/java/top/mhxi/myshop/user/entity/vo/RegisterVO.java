package top.mhxi.myshop.user.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegisterVO {
    @NotBlank  // 校验
    private String name;

    @NotBlank  // 校验
    private String email;

    @NotBlank  // 校验
    private String password;

    private String code;
}
