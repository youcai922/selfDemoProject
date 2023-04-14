package com.yc.esDemo;

import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author yucan
 * @date 2022/12/6 11:15
 */
@SpringBootTest
public class IndexTests {

    @Resource
    private RestHighLevelClient client;

    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
    public void createIndex() throws IOException {
        //创建索引Request
        CreateIndexRequest request = new CreateIndexRequest("music");
        //设置索引分片和备份
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));
        //设置mapping映射
        request.mapping(
                "{\n" +
                        "  \"properties\":\n" +
                        "  {\n" +
                        "    \"mid\": {\n" +
                        "      \"type\": \"long\"\n" +
                        "    },\n" +
                        "    \"name\":{\n" +
                        "      \"type\": \"text\"\n" +
                        "    },\n" +
                        "    \"type\":{\n" +
                        "      \"type\": \"keyword\"\n" +
                        "    },\n" +
                        "    \"singer\":{\n" +
                        "      \"type\": \"text\",\n" +
                        "      \"fields\":{\n" +
                        "        \"keyword\":{\n" +
                        "          \"type\": \"keyword\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"country\":{\n" +
                        "      \"type\": \"keyword\"\n" +
                        "    },\n" +
                        "    \"date\": {\n" +
                        "      \"type\": \"date\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);
        //设置别名
        request.alias(new Alias("music_alias"));
        //发送请求
        //同步
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        //指示是否所有节点都已确认请求
        boolean acknowledged = response.isAcknowledged();
        //指示是否在超时之前为索引中的每个分片启动了必需的分片副本数
        boolean shardsAcknowledged = response.isShardsAcknowledged();
        System.out.println("acknowledged:" + acknowledged);
        System.out.println("shardsAcknowledged:" + shardsAcknowledged);
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    @Test
    public void deleteIndex() throws IOException {
        //创建删除索引（指向索引：music）
        DeleteIndexRequest request = new DeleteIndexRequest("music");
        //发送请求
        //同步
        AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(deleteIndexResponse.isAcknowledged());
    }
}
