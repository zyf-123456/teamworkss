package reptile;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Slf4j
public class Tepo {

    public static void main(String[] args){

        for (int i = 0; i < 100; i++) {
            try {

                String url = "http://www.baidu.com/s?wd=" + 168204;
                final WebClient webClient = new WebClient(BrowserVersion.FIREFOX);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

                webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
                webClient.getOptions().setActiveXNative(false);//不启用ActiveX
                webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
                webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
                webClient.getOptions().setDownloadImages(false);//不下载图片
                webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

                HtmlPage page = null;
                try {
                    page = webClient.getPage(url);//尝试加载上面图片例子给出的网页
                } catch (Exception e) {
                    log.info("忽略这个错误");
                } finally {
                    webClient.close();
                }
                webClient.waitForBackgroundJavaScript(200000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
                String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
                Document parse = Jsoup.parse(pageXml);
                String str = parse.toString();
                if (str.contains("<span class=\"op-stockdynamic-cur-info")) {
                    String[] split = str.split("<span class=\"op-stockdynamic-cur-info");
                    String[] s = split[1].split(">");
                    String substring = split[1].substring(split[1].indexOf(">") + 2, 29);
                    String trim = substring.replace("(", "").replace(")", "").trim();
                    System.out.println(trim);
                    break;
                }
            } catch (Exception e) {
                log.error("未知的错误");
            } catch (Error e) {
                log.error("未知的错误");
            }

        }

    }


}
