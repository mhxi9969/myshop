package ${package}.service.impl;


import lombok.extern.slf4j.Slf4j;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import ${package}.mapper.${entity}Mapper;
import ${package}.entity.${entity};
import ${package}.service.${entity}Service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
@Service
public class ${entity}ServiceImpl implements ${entity}Service {

    @Autowired
    private ${entity}Mapper ${entityLower}Mapper;


    public int insert(${entity} ${entityLower}) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        ${entityLower}.setId(idWorker.nextId());
        return ${entityLower}Mapper.insert(${entityLower});
    }


    public int deleteById(Long id) {
        return ${entityLower}Mapper.deleteByPrimaryKey(id);
    }


    public int update(${entity} ${entityLower}) {
        return ${entityLower}Mapper.updateByPrimaryKey(${entityLower});
    }


    public ${entity} selectById(Long id) {
        return ${entityLower}Mapper.selectByPrimaryKey(id);
    }


    public List<${entity}> selectAll() {
        return ${entityLower}Mapper.selectAll();
    }

}