package top.mhxi.myshop.search.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import top.mhxi.myshop.common.to.ProductSkuTreeTO;
import top.mhxi.myshop.search.entity.SearchConditon;
import top.mhxi.myshop.search.repository.SkuRepository;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class SearchService {

    @Autowired
    SkuRepository repository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public void addProduct(List<ProductSkuTreeTO> skuTreeTOS) {
        repository.saveAll(skuTreeTOS);
    }

    public void removeProduct(List<ProductSkuTreeTO> skuTreeTOS) {
        for (ProductSkuTreeTO skuTreeTO : skuTreeTOS) {
            repository.deleteById(skuTreeTO.getId());
        }
    }


    public Page<ProductSkuTreeTO> searchProduct(SearchConditon condition, int page, int size) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        // 商品名精确短语匹配
        if (condition.getName() != null && !condition.getName().isEmpty()) {
            boolQuery.must(QueryBuilders.matchPhraseQuery("name", condition.getName()));
        }

        // 属性过滤
        if (condition.getAttrs() != null && !condition.getAttrs().isEmpty()) {
            for (String attr : condition.getAttrs()) {
                String[] arr = attr.split(":"); // 格式: attrName:attrValue
                if (arr.length == 2) {
                    boolQuery.must(QueryBuilders.nestedQuery(
                            "attrTOs",
                            QueryBuilders.boolQuery()
                                    .must(QueryBuilders.termQuery("attrTOs.name", arr[0]))
                                    .must(QueryBuilders.termQuery("attrTOs.skuValueName", arr[1])),
                            ScoreMode.None
                    ));
                }
            }
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(page, size));

        // 价格排序
        if (condition.getSortByPrice() != null) {
            if ("asc".equalsIgnoreCase(condition.getSortByPrice())) {
                queryBuilder.withSort(Sort.by(Sort.Order.asc("price")));
            } else if ("desc".equalsIgnoreCase(condition.getSortByPrice())) {
                queryBuilder.withSort(Sort.by(Sort.Order.desc("price")));
            }
        }

        NativeSearchQuery query = queryBuilder.build();

        List<ProductSkuTreeTO> content = elasticsearchRestTemplate.search(query, ProductSkuTreeTO.class)
                .stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        long total = elasticsearchRestTemplate.count(query, ProductSkuTreeTO.class);

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }


    public List<ProductSkuTreeTO> searchBySpuID(Long spuId) {

        // 构建 ES 查询：spuId 精确匹配
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("spuId", spuId));

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();

        // 执行查询
        List<ProductSkuTreeTO> skus = elasticsearchRestTemplate.search(query, ProductSkuTreeTO.class)
                .stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        return skus;
    }
}
