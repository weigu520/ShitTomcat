package http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ����:
 *
 * @author WeiGu
 * @create 2019-08-27 14:09
 */
public class MyServerMain {
    public static void main(String[] args) {
        /*
        1.�һ��tcp������
        2.���ܿͻ�������
        3.���ܿͻ��˷�������������,���������̨��
         */
        try {
            //1.�һ��tcp������
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("������������------------");

            //2.���ܿͻ�������
            Socket s = ss.accept();
            if(s!=null){
                System.out.println("���ӳɹ���---------");
            }

            //3.���ܿͻ��˷�������������,���������̨��
            /*
            URL:G:\myserver\a.txt
            1.http���������
            2.������,����ͷ,����,������
             */

            //A׼����
            InputStream is = s.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStream os = s.getOutputStream();

//            String message1 = br.readLine();
//            System.out.println(message1);

            String line=br.readLine();
//            System.out.println(line);
            //���տո���������  GET /a.txt HTTP/1.1
            String[] array = line.split(" ");

            //�ɵ�б��
            String fileName = array[1].substring(1);

            //�ж���Դ�Ƿ����
            //׼����
            File file = new File("G:\\myserver",fileName);
            boolean flag = file.exists();
            if(flag){
                //�����,�ж��Ƿ�������Դ
                if("/".equals(array[1])){
                    //����welcome.html
                    String reply = "HTTP/1.1 200 OK\n";             //��Ӧ��
                    reply += "Content-type:text/html\n\n";          //��Ӧͷ
                    BufferedReader reader = new BufferedReader(new FileReader(new File("G:\\myserver","welcom.html")));
                    String message=null;
                    while ((message=reader.readLine())!=null){
                        reply+=message;
                    }
                    os.write(reply.getBytes("GB2312"));
                }else{
//                System.out.println("����,��������в���");
                    //���ؿͻ���a.txt�ļ�
                    String reply = "HTTP/1.1 200 OK\n";             //��Ӧ��
                    reply += "Content-type:text/html\n\n";        //��Ӧͷ
                    FileReader fr = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fr);
//                InputStreamReader ISR = new InputStreamReader(new FileInputStream(file), "GBK");
//                BufferedReader reader = new BufferedReader(ISR);
                    String message = null;
                    while ((message = reader.readLine()) != null) {
                        reply += message;
                    }
                    os.write(reply.getBytes("GB2312"));
                    //http��Ӧ
                    //��Ӧ��
                    //��Ӧͷ(Ϊ��)
                    //����
                    //��Ϣ��(�ļ�)
                }
            }else{
                System.out.println("��û��,����㶼����");
                String reply = "HTTP/1.1 200 OK\n";             //��Ӧ��
                reply += "Content-type:text/html\n\n";          //��Ӧͷ
                BufferedReader reader = new BufferedReader(new FileReader(new File("G:\\myserver","error.html")));
                String message=null;
                while ((message=reader.readLine())!=null){
                    reply+=message;
                }
                os.write(reply.getBytes("GB2312"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}