package ${package}.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import top.mhxi.myshop.common.utils.R;
import ${package}.entity.vo.${entity}VO;
import ${package}.entity.${entity};
import ${package}.service.${entity}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "${entity}管理")
@RestController
@RequestMapping("/order/${entityLower}")
public class ${entity}Controller {

    @Autowired
    private ${entity}Service ${entityLower}Service;


    @Operation(summary = "添加一个${entity}")
    @PostMapping
    public R insert(@RequestBody ${entity}VO ${entityLower}VO) {
        ${entity} ${entityLower} = new ${entity}();
        BeanUtils.copyProperties(${entityLower}VO,${entityLower});

        int i = ${entityLower}Service.insert(${entityLower});
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个${entity}")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = ${entityLower}Service.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个${entity}")
    @PutMapping
    public R update(@RequestBody ${entity}VO ${entityLower}VO) {
        ${entity} ${entityLower} = new ${entity}();
        BeanUtils.copyProperties(${entityLower}VO,${entityLower});

        int i = ${entityLower}Service.update(${entityLower});
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个${entity}")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        ${entity} ${entityLower} = ${entityLower}Service.selectById(id);

        if(${entityLower} != null) {
            ${entity}VO ${entityLower}VO = new ${entity}VO();
            BeanUtils.copyProperties(${entityLower}, ${entityLower}VO);
            return R.ok().data("record", ${entityLower}VO);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "查询所有${entity}")
    @GetMapping("/selectAll")
    public R selectAll() {
        List<${entity}> ${entityLower}s = ${entityLower}Service.selectAll();

        if(!${entityLower}s.isEmpty()) {
            List<${entity}VO> ${entityLower}VOS = new ArrayList<>();

            for (${entity} ${entityLower} : ${entityLower}s) {
                ${entity}VO ${entityLower}VO = new ${entity}VO();
                BeanUtils.copyProperties(${entityLower}, ${entityLower}VO);
                ${entityLower}VOS.add(${entityLower}VO);
            }

            return R.ok().data("record", ${entityLower}VOS);
        } else {
            return R.error();
        }
    }

}