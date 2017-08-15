package hdfs.test;

import hdfs.demo.HDFSUtil;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Test;

public class test {
	@Test
    public void test() throws IOException {
        Configuration conf = new Configuration();
        String newDir = "/home/demoDir";
        //01.���·���Ƿ���� ����
        if (HDFSUtil.exits(conf, newDir)) {
            System.out.println(newDir + " �Ѵ���!");
        } else {
            //02.����Ŀ¼����
            boolean result = HDFSUtil.createDirectory(conf, newDir);
            if (result) {
                System.out.println(newDir + " �����ɹ�!");
            } else {
                System.out.println(newDir + " ����ʧ��!");
            }
        }
        String fileContent = "Hi,hadoop. I love you";
        String newFileName = newDir + "/myfile.txt";

        //03.�����ļ�����
        HDFSUtil.createFile(conf, newFileName, fileContent);
        System.out.println(newFileName + " �����ɹ�");

        //04.��ȡ�ļ����� ����
        System.out.println(newFileName + " ������Ϊ:\n" + HDFSUtil.readFile(conf, newFileName));

        //05. ���Ի�ȡ����Ŀ¼��Ϣ
        FileStatus[] dirs = HDFSUtil.listStatus(conf, "/");
        System.out.println("--��Ŀ¼�µ�������Ŀ¼---");
        for (FileStatus s : dirs) {
            System.out.println(s);
        }

        //06. ���Ի�ȡ�����ļ�
        FileSystem fs = FileSystem.get(conf);
        RemoteIterator<LocatedFileStatus> files = HDFSUtil.listFiles(fs, "/", true);
        System.out.println("--��Ŀ¼�µ������ļ�---");
        while (files.hasNext()) {
            System.out.println(files.next());
        }
        fs.close();

        //ɾ���ļ�����
        boolean isDeleted = HDFSUtil.deleteFile(conf, newDir);
        System.out.println(newDir + " �ѱ�ɾ��");

    }
}
