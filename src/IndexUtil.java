import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexUtil {

    /**
     * 跟据索引路径获取数据源内容并生成lucene Document对象的方法
     *
     * @param folderPath
     * @return
     * @throws IOException
     */
    public static List<Document> fileToDoc(String folderPath) throws IOException {

        List<Document> docList = new ArrayList<Document>();

        File folder = new File(folderPath);

        if(!folder.isDirectory()) {
            System.out.println("该路径不是文件夹！");
        }

        File[] fileArr = folder.listFiles();
        for (File file : fileArr) {
            //获得文件名、文件内容、文件体积和文件路径
            String fileName = file.getName();
            String fileContent = FileUtils.readFileToString(file,"GBK");
            Long fileSize = FileUtils.sizeOf(file);
            String filePath = file.getAbsolutePath();

            if(fileName.indexOf(".txt") != -1) {
                //将文件名、文件内容、文件体积和文件路径封装成Field对象（类比来说，Field相当于数据库中表中的列）
                Field fileName_field = new StringField("fileName", fileName, Field.Store.YES);
                Field content_field = new TextField("fileContent", fileContent, Field.Store.YES);
                Field size_field = new LongPoint("fileSize", fileSize);
                Field path_field = new StringField("filePath", filePath, Field.Store.YES);

                //生成lucene Document对象（相当于数据库中的表）
                Document doc = new Document();
                doc.add(fileName_field);
                doc.add(content_field);
                doc.add(size_field);
                doc.add(path_field);
                docList.add(doc);

            }
        }

        return docList;
    }

    /**
     * 打印获取到的索引查找内容的方法
     *
     * @param doc
     */
    public static void printDocumentOfFile(Document doc){

        System.out.println("------------------------------");

        System.out.println("文件名称 =" + doc.get("fileName"));

        System.out.println("文件大小 =" + doc.get("fileSize"));

        System.out.println("文件内容 =" + doc.get("fileContent"));

        System.out.println("文件路径 =" + doc.get("filePath"));

    }
}
