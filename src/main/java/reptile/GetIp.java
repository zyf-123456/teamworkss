package reptile;

import lombok.SneakyThrows;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetIp {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        Scanner sc= new Scanner(System.in);
        System.out.println("please input:");
        String mess= sc.next();
        try {
            String smess= URLEncoder.encode(mess, "UTF-8");
            String urll= "http://www.baidu.com/s?wd="+ smess;
            URL url= new URL(urll);
            InputStream input= url.openStream();
            FileOutputStream fout= new FileOutputStream(new File(".\\FirstPage.html"));  //爬取百度搜索并保存成文件
            int a= 0;
            while(a> -1) {
                a= input.read();
                fout.write(a);
            }
            fout.close();

            File file= new File(".\\FirstPage.html");

            String pattern= "href=\"(http://www.baidu.com/link\\?url=.+?)\".+$"; //正则匹配其中的地址
            String h3_pattern= "(h3 class)";   //通过爬取的网页源码可以发现每个链接之前都有一个h3 class标签
            Pattern pat= Pattern.compile(pattern);
            Pattern h3_pat= Pattern.compile(h3_pattern);

            InputStreamReader input1;
            input1 = new InputStreamReader(new FileInputStream(file), "UTF-8");
            Scanner scan= new Scanner(input1);


            String str= null;
            Matcher mach;
            Matcher machh3;
            int count= 0;
            FileWriter fw= new FileWriter(".\\Result.txt");	 //提取其中的地址并保存成文件
            boolean in= false;
            while(scan.hasNextLine()) {
                str= scan.nextLine();
                System.out.println("<<爬取的东西>>" + str);
                machh3= h3_pat.matcher(str);  //先看能不能匹配到h3 class标签，如果可以则进行匹配链接
                while(machh3.find()) {
                    if(machh3.groupCount()> 0) {
                        in= true;
                    }
                }
                if(in) {
                    mach= pat.matcher(str);  //匹配链接
                    while(mach.find()) {
                        count++;
                        in= false;
                        System.out.println(count+ " "+mach.group(1));
                        fw.write(mach.group(1)+"\r\n");
                    }
                }

            }
            fw.close();

            file= new File(".\\Result.txt");  //读取要解析的链接文件
            InputStreamReader input2= new InputStreamReader(new FileInputStream(file), "UTF-8");
            Scanner sca= new Scanner(input2);
            String st= null;
            count= 0;
            FileWriter fww= new FileWriter(".\\ResultPro.txt");  //将解析后的链接保存成文件
            while(sca.hasNext()) {
                count++;
                st= sca.nextLine();
                URL uurl= new URL(st);

                HttpURLConnection connect= (HttpURLConnection) uurl.openConnection();
                connect.setInstanceFollowRedirects(false); // 设置是否自动重定向
                fww.write(count+" "+st+"\n"+"  "+connect.getHeaderField("Location")+"\r\n");  //获取真实的地址
                System.out.println(count+ " "+connect.getHeaderField("Location"));
            }
            fww.close();
            System.out.println("has end");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }






}
