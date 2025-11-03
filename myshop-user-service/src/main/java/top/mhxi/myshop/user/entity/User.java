package top.mhxi.myshop.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class User {
    private Long id;

    private String name;

    private String password;

    private String email;

    private String phone;

    private String role;

    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date createTime;

    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date updateTime;

}