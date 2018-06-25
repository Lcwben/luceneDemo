import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


/**
 * lucene Demo
 *
 */
public class indexTest {
    //数据源文件位置
    private String indexSource = "E:\\luceneResource\\searchsource";
    //索引目标地址
    private String indexFolder = "E:\\luceneResource\\indexdata";

    /**
     * 测试创建索引方法
     */
    @Test
    public void testCreateIndex() {

        IndexWriter indexWriter = null;
        try {
            List<Document> docList = IndexUtil.fileToDoc(indexSource);
            Analyzer standardAnalyzer = new StandardAnalyzer();
            //制定索引存储目录
            Directory directory = FSDirectory.open(Paths.get(indexFolder));

            IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
            //定义索引操作对象indexWriter
            indexWriter = new IndexWriter(directory,config);

            //遍历数据源生成的Document对象
            for (Document doc : docList) {
                indexWriter.addDocument(doc);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 测试查询lucene索引的方法
     */
    @Test
    public void testSearchIndex() {

        try {
            Directory directory = FSDirectory.open(Paths.get(indexFolder));
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            //创索引查询对象，根据文件名称域搜索匹配文件名称的文档
            Query query = new TermQuery(new Term("fileName","发布说明.txt"));
            //执行搜索结果
            TopDocs topDocs = indexSearcher.search(query, 20);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            System.out.println("搜索文件名数目：" + topDocs.totalHits);

            //遍历所有搜索结果(Document对象)
            for (ScoreDoc scoreDoc: scoreDocs) {
                //获取docId
                int docId = scoreDoc.doc;
                //根据docId获取Document对象
                Document doc = indexSearcher.doc(docId);

                IndexUtil.printDocumentOfFile(doc);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
