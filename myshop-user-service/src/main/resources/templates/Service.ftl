package ${package}.service;


import ${package}.entity.${entity};

import java.util.List;

public interface ${entity}Service {
    int insert(${entity} ${entityLower});
    int deleteById(Long id);
    int update(${entity} ${entityLower});
    ${entity} selectById(Long id);
    List<${entity}> selectAll();
}