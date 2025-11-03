package top.mhxi.myshop.search.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.mhxi.myshop.common.to.ProductSkuTreeTO;

import java.util.List;

@Repository
public interface SkuRepository extends ElasticsearchRepository<ProductSkuTreeTO, Long> {
    // 根据名字模糊查询（自动生成）
    @Query("{\"match\": {\"name\": \"?0\"}}")
    Page<ProductSkuTreeTO> findByNameContaining(String name, Pageable pageable);
}
