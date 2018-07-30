package org.pengfei.hdfs;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class HdfsUtil {

    private static final Logger logger = Logger.getLogger("org.pengfei.hdfs.HdfsUtil");

    private FileSystem fs;

    public HdfsUtil(String hdfsUri) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS",hdfsUri);
        // Because of Maven
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        // Set HADOOP user
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        System.setProperty("hadoop.home.dir", "/");
        // get FileSystem
        fs = FileSystem.get(URI.create(hdfsUri), conf);
    }

    public void upload(String filePath, String fileName, String fileContent) throws IOException {
        Path workingDir=fs.getWorkingDirectory();
        //Create filePath if path does not exist
        Path newFolderPath = new Path(filePath);
        if(!fs.exists(newFolderPath)) {
            // Create new Directory
            fs.mkdirs(newFolderPath);
            logger.info("Path "+filePath+" created.");
        }
        //==== Write file
        logger.info("Begin Write file into hdfs");
        //Create a path
        Path hdfswritepath = new Path(newFolderPath + "/" + fileName);
        //Init output stream
        FSDataOutputStream outputStream=fs.create(hdfswritepath);
        //Cassical output stream usage
        outputStream.writeBytes(fileContent);
        outputStream.close();
        logger.info("End Write file into hdfs");

    }

    public void readFile(String filePath, String fileName) throws IOException {
        //==== Read file
        logger.info("Read file into hdfs");
        //Create a path
        Path hdfsreadpath = new Path(filePath + "/" + fileName);
        //Init input stream
        FSDataInputStream inputStream = fs.open(hdfsreadpath);
        //Classical input stream usage
        String out= IOUtils.toString(inputStream, "UTF-8");
        logger.info(out);
        inputStream.close();
    }

    public void listAllFiles(String filePath) throws IOException {
        Path path=new Path(filePath);
        RemoteIterator<LocatedFileStatus> fileList = fs.listFiles(path, true);
        while (fileList.hasNext()){
            LocatedFileStatus file = fileList.next();
            System.out.println(file.toString());
        }
    }

    public void closeFS() throws IOException {
        fs.close();
    }



    public static void main(String[] args) throws IOException {

        String fileName="hello.csv";
        String fileContent="hello;world;My name is pengfei";
        String hdfsuri="hdfs://localhost:9000";
        String path= "/test_data/api";


        HdfsUtil hdfs = new HdfsUtil(hdfsuri);
        //hdfs.readFile(path,fileName);
        //hdfs.upload(path,fileName,fileContent);
//        hdfs.readFile(path,fileName);
//        hdfs.closeFS();
        hdfs.listAllFiles("/test_data");
    }
}
