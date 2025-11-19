package top.mhxi.myshop.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.mhxi.myshop.common.to.ProductSkuTreeTO;

@Repository
public interface SkuRepository extends ElasticsearchRepository<ProductSkuTreeTO, Long> {
}
