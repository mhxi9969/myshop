import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.mhxi.myshop.search.SearchServiceApplication;

import java.io.IOException;


@SpringBootTest(classes = SearchServiceApplication.class)
public class SearchTest {

    @Autowired
    private RestHighLevelClient client;


    @Test
    public void testIndex() throws IOException {

    }
}
