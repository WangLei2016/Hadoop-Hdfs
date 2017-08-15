package org.apache.hadoop.examples;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.junit.Before;
import org.junit.Test;

public class FileDemo {
    private  Configuration conf = new Configuration();//���ﴴ��conf������һ��Ĭ�ϲ�����boolean loadDefaults��Ĭ��Ϊtrue
    private String rootPath=new String("hdfs://192.168.96.128:9000/");
    private FileSystem coreSys=null;
    /**
     * ÿ��ִ��֮ǰ��ʼ����������ʼ��FileSystem���Ķ���
     */
    @Before
    public void iniFileSystemObject(){
        try {
            coreSys=FileSystem.get(URI.create(rootPath), conf);
        } catch (IOException e) {
            System.out.println("��ʼ��HDFS�����ļ�����ʧ�ܣ�"+e.getLocalizedMessage());
        }
    }
    /**
     * ��HDFS�ϴ����ļ�Ŀ¼
     */
    public void createDirOnHDFS(){
           Path demoDir=new Path(rootPath+"home/demoDir");
           boolean isSuccess=true;
           try {
               isSuccess=coreSys.mkdirs(demoDir);
           } catch (IOException e) {
               isSuccess=false;
           }
           System.out.println(isSuccess?"Ŀ¼�����ɹ���":"Ŀ¼����ʧ��!");
           
    }
    /**
     * ��HDFS�ϴ����ļ�
     * @throws Exception 
     */
   
    public void createFile() throws Exception{
                Path hdfsPath = new Path(rootPath+"home/demoDir/" + "createDemoFile1");
                System.out.println(coreSys.getHomeDirectory());
                String content = " hadoop i love ,this is first time that I create file on hdfs";
                FSDataOutputStream fsout = coreSys.create(hdfsPath);
                BufferedOutputStream bout = new BufferedOutputStream(fsout);
                bout.write(content.getBytes(), 0, content.getBytes().length);
                bout.close();
                fsout.close();
                System.out.println("�ļ�������ϣ�");
    }
    /**
     * �ӱ����ϴ������ļ���������HDFS����
     * @throws Exception
     */
    @Test
    public void uploadFile() throws Exception{
         Configuration conf = new Configuration();
         Path remotePath=new Path(rootPath+"/demoDir/");
         coreSys.copyFromLocalFile(new Path("C:\\Users\\Administrator\\Desktop\\QQͼƬ20170410172123.png"), remotePath);
         System.out.println("Upload to:"+conf.get("fs.default.name"));
         FileStatus [] files=coreSys.listStatus(remotePath);
         for(FileStatus file:files){
             System.out.println(file.getPath().toString());
         }
    }
    /**
     * �������ļ���
     */
   
    public void renameFile(){
        Path oldFileName=new Path(rootPath+"user/hdfsupload/createDemoFile");
        Path newFileName=new Path(rootPath+"user/hdfsupload/renameDemoFile");
        boolean isSuccess=true;
        try {
            isSuccess=coreSys.rename(oldFileName, newFileName);
        } catch (IOException e) {
             isSuccess=false;
        }
        System.out.println(isSuccess?"�������ɹ���":"������ʧ�ܣ�");
    }
    /**
     * ɾ���ļ�
     */
   
    public void deleteFile(){
        Path deleteFile=new Path(rootPath+"user/hdfsupload/job.jar");
        boolean isSuccess=true;
        try {
            isSuccess=coreSys.delete(deleteFile, false);
        } catch (IOException e) {
            isSuccess=false;
        }
        System.out.println(isSuccess?"ɾ���ɹ�!":"ɾ��ʧ�ܣ�");
    }
    /**
     * ����ĳ���ļ��Ƿ����
     */
 
    public void findFileIsExit(){
          Path checkFile=new Path(rootPath+"user/hdfsupload/job.jar");
          boolean isExit=true;
          try {
              isExit=coreSys.exists(checkFile);
            } catch (IOException e) {
                isExit=false;
           }
        System.out.println(isExit?"�ļ�����!":"�ļ������ڣ�");
    }
    /**
     * �鿴ĳ���ļ�������޸�ʱ��
     * @throws IOException 
     */
   
    public void watchFileLastModifyTime() throws IOException{
         Path targetFile=new Path(rootPath+"user/hdfsupload/renameDemoFile");
         FileStatus fileStatus=coreSys.getFileStatus(targetFile);
         Long lastTime=fileStatus.getModificationTime();
         Date date=new Date(lastTime);
         SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         System.err.println("�ļ�������޸�ʱ��Ϊ:"+format.format(date));
    }
    /**
     * ��ȡĳ��·������������ļ�
     * @throws IOException 
     */
   
    public void getUnderDirAllFile() throws IOException{
        Path targetDir=new Path(rootPath+"user/hdfsupload/");
        FileStatus []fileStatus=coreSys.listStatus(targetDir);
        for(FileStatus file:fileStatus){
            System.out.println(file.getPath()+"--"+file.getGroup()+"--"+file.getBlockSize()+"--"+file.getLen()+"--"+file.getModificationTime()+"--"+file.getOwner());
        }
    }
    /**
     * �鿴ĳ���ļ���HDFS��Ⱥ��λ��
     * @throws IOException 
     */
  
    public void findLocationOnHadoop() throws IOException{
        Path targetFile=new Path(rootPath+"user/hdfsupload/AA.txt");
        FileStatus fileStaus=coreSys.getFileStatus(targetFile);
        BlockLocation []bloLocations=coreSys.getFileBlockLocations(fileStaus, 0, fileStaus.getLen());
        for(int i=0;i<bloLocations.length;i++){
            System.out.println("block_"+i+"_location:"+bloLocations[i].getHosts()[0]);
        }
        
    }
    /**
     * ��ȡ��Ⱥ�Ͻ�����Ϣ
     * @throws IOException 
     */
  
    public void getNodeMsgHdfs() throws IOException{
         DistributedFileSystem distributedFileSystem=(DistributedFileSystem) coreSys;
         DatanodeInfo []dataInfos=distributedFileSystem.getDataNodeStats();
         for(int j=0;j<dataInfos.length;j++){
             System.out.println("DataNode_"+j+"_Name:"+dataInfos[j].getHostName()+"--->"+dataInfos[j].getDatanodeReport()+"-->"+
            dataInfos[j].getDfsUsedPercent()+"-->"+dataInfos[j].getLevel());
         }
    }
    
}