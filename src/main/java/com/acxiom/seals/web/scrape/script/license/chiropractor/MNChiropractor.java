package com.acxiom.seals.web.scrape.script.license.chiropractor;

import com.acxiom.seals.web.scrape.framework.CommonWebScrape;
import com.acxiom.seals.web.scrape.framework.exception.GsfAuditException;
import com.acxiom.seals.web.scrape.framework.exception.GsfScrapingException;
import com.acxiom.seals.web.scrape.script.common.utils.CapabilitiesCreator;
import com.acxiom.seals.web.scrape.script.common.utils.GsfFileWriter;
import com.acxiom.seals.web.scrape.script.common.utils.GsfWebDriver;
import com.acxiom.seals.web.scrape.script.common.utils.SleepUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;

public class MNChiropractor extends CommonWebScrape {

    private static final Logger LOGGER = LogManager.getLogger(MNChiropractor.class);
    private static final String REQUEST_URL = "https://chi.hlb.state.mn.us/#/services/onlineLicenseSearch";
    private static final String FILE_NAME = "MNChiropractor.txt";
    private static final String SPLIT_CHAR = "|";
    private static final String SPLIT_LINE_CHAR = "\r\n";

    private DesiredCapabilities cap = null;
    private GsfFileWriter fileWriter = null;
    private String filePath = null;

    private static final String HEADER = "name|type|status|address|gender|issued|lastRenewed|expiration|phone"+SPLIT_LINE_CHAR;

    public void scrape() throws GsfScrapingException {
        filePath = this.job.getOutputPath() + File.separator +FILE_NAME;

        try {
            cap = CapabilitiesCreator.createChromeCapabilities(this.job);
            fileWriter = new GsfFileWriter(filePath, false);
            fileWriter.write(HEADER);
            for(char searchKey = 'a'; searchKey <= 'z'; searchKey++) {
                for(char searchKey2 = 'a'; searchKey2 <= 'z'; searchKey2++) {
                    String sendKey = String.valueOf(searchKey) + String.valueOf(searchKey2);
                    readChiropractor(sendKey);
                }
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            throw new GsfScrapingException(e.getMessage());
        }finally {
            try{
                if(fileWriter != null){
                    fileWriter.close();
                }
            }catch (IOException e){
                LOGGER.error("close file writer failed..."+e.getMessage());
            }
        }
    }

    private void readChiropractor(String sendKey){
        GsfWebDriver webDriver = null;
        try{
            webDriver = new GsfWebDriver(new ChromeDriver(cap));
            webDriver.get(REQUEST_URL);
            webDriver.manage().window().maximize();

            WebElement lastname = webDriver.findElement(By.id("lastName"));
            lastname.clear();
            lastname.sendKeys(sendKey);

            webDriver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/fieldset/div/div/div[2]/fieldset/div[2]/div/div/div/button")).click();
            SleepUtil.sleep(2);
            String pageSource = webDriver.getPageSource();
            if(!pageSource.contains("No results found")){
                Document doc = Jsoup.parse(pageSource);
                int n = 1;
                int num = doc.select("body > div:nth-child(2) > div.container.layout-content-area > div:nth-child(3) > div > fieldset > div > div > div.ng-scope > uib-accordion > div > div").size();
                if(num > 13){
                    webDriver.executeJavaScript("window.scrollTo(0,300);");
                }
                for(int i = 1; i <= num; i++){
                    webDriver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/fieldset/div/div/div[3]/uib-accordion/div/div["+i+"]/div[1]/h4/a")).click();
                    SleepUtil.sleep(2);
                    pageSource = webDriver.getPageSource();
                    doc = Jsoup.parse(pageSource);
                    Elements elements = doc.select("label.col-sm-3");
                    getDetailInfo(elements);
                    webDriver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/fieldset/div/div/div[3]/uib-accordion/div/div["+i+"]/div[1]/h4/a")).click();
                    if(i == 20*n){
                        webDriver.executeJavaScript("window.scrollTo(0,"+(800*n+300)+");");
                        n++;
                    }
                }
            }else{
                LOGGER.info("no results found...sendKey is "+sendKey);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(webDriver != null){
                webDriver.quit();
            }
        }
    }

    private void getDetailInfo(Elements elements){
        String name = "",type = "",status = "",address = "",gender = "",issued = "",lastRenewed = "",expiration = "",phone = "";
        for(Element element : elements){
            switch (element.text()){
                case "Name":
                    name = element.nextElementSibling().text();
                    break;
                case "Type":
                    type = element.nextElementSibling().text();
                    break;
                case "Status":
                    status = element.nextElementSibling().text();
                    break;
                case "Public Address":
                    address = element.nextElementSibling().text();
                    break;
                case "Gender":
                    gender = element.nextElementSibling().text();
                    break;
                case "Issued":
                    issued = element.nextElementSibling().text();
                    break;
                case "Last Renewed":
                    lastRenewed = element.nextElementSibling().text();
                    break;
                case "Expiration":
                    expiration = element.nextElementSibling().text();
                    break;
                case "Public Phone":
                    phone = element.nextElementSibling().text();
                    break;
                default:
                    break;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(SPLIT_CHAR)
                .append(type).append(SPLIT_CHAR)
                .append(status).append(SPLIT_CHAR)
                .append(address).append(SPLIT_CHAR)
                .append(gender).append(SPLIT_CHAR)
                .append(issued).append(SPLIT_CHAR)
                .append(lastRenewed).append(SPLIT_CHAR)
                .append(expiration).append(SPLIT_CHAR)
                .append(phone).append(SPLIT_LINE_CHAR);
        try {
            fileWriter.write(sb.toString());
            LOGGER.info(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void audit() throws GsfAuditException {

    }
}
