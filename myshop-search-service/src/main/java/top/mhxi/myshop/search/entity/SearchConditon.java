package top.mhxi.myshop.search.entity;

import lombok.Data;

import java.util.List;

@Data
public class SearchConditon {
    String name;

    List<String> attrs;

    String sortByPrice;

}
