package com.acxiom.seals.web.scrape.script.license.embalmer;

import com.acxiom.seals.web.scrape.framework.CommonWebScrape;
import com.acxiom.seals.web.scrape.framework.exception.GsfAuditException;
import com.acxiom.seals.web.scrape.framework.exception.GsfScrapingException;
import com.acxiom.seals.web.scrape.script.common.utils.CapabilitiesCreator;
import com.acxiom.seals.web.scrape.script.common.utils.GsfFileWriter;
import com.acxiom.seals.web.scrape.script.common.utils.GsfWebDriver;
import com.acxiom.seals.web.scrape.script.common.utils.SleepUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;

public class ALEmbalmer extends CommonWebScrape {

    private static final Logger LOGGER = Logger.getLogger(ALEmbalmer.class);
    private static final String REQUEST_URL = "https://igovsolution.net/fsbonline/Lookups/Individual.aspx";
    private static final String FILE_NAME = "ALEmbalmer.txt";
    private static final String SPLIT_CHAR = "|";
    private static final String SPLIT_LINE_CHAR = "\r\n";

    private DesiredCapabilities cap = null;
    private GsfFileWriter fileWriter = null;
    private String filePath = null;

    private static final String HEADER = "name|licenseNumber|licenseType|status|issued|expiration|citizenship|discipline"+SPLIT_LINE_CHAR;

    public void scrape() throws GsfScrapingException {
        filePath = this.job.getOutputPath() + File.separator +FILE_NAME;

        try {
            cap = CapabilitiesCreator.createChromeCapabilities(this.job);
            fileWriter = new GsfFileWriter(filePath, false);
            fileWriter.write(HEADER);
            for(char searchKey = 'a'; searchKey <= 'z'; searchKey++) {
                String sendKey = String.valueOf(searchKey);
                readEmbalmer(sendKey);
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

    private void readEmbalmer(String sendKey){
        GsfWebDriver webDriver = null;
        try{
            webDriver = new GsfWebDriver(new ChromeDriver(cap));
            webDriver.get(REQUEST_URL);
            webDriver.manage().window().maximize();

            WebElement lastname = webDriver.findElement(By.id("ctl00_cntbdy_txt_lastname"));
            lastname.clear();
            lastname.sendKeys(sendKey);

            webDriver.findElement(By.id("ctl00_cntbdy_btn_search")).click();
            SleepUtil.shortSleep();
            String pageSource = webDriver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            Elements trs = doc.select("#grdlkp_tbl > tbody > tr");
            Select page = new Select(webDriver.findElement(By.xpath("//*[@id=\"grdlkp_tbl\"]/tfoot/tr/td/select[2]")));
            if(trs.size() > 0){
                if(page.getOptions().size() > 1){
                    for(int i = 0; i < page.getOptions().size(); i++){
                        page.selectByIndex(i);
                        SleepUtil.sleep(2);
                        pageSource = webDriver.getPageSource();
                        doc = Jsoup.parse(pageSource);
                        trs = doc.select("#grdlkp_tbl > tbody > tr");
                        getDetailInfo(trs);
                    }
                }else{
                    getDetailInfo(trs);
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

    private void getDetailInfo(Elements trs){
        String name = "",licenseNumber = "",licenseType = "",status = "",issued = "",expiration = "",citizenship = "",discipline = "";
        for(Element tr : trs){
            name = tr.select("td:nth-child(1)").text();
            licenseNumber = tr.select("td:nth-child(2)").text();
            licenseType = tr.select("td:nth-child(3)").text();
            status = tr.select("td:nth-child(4)").text();
            issued = tr.select("td:nth-child(5)").text();
            expiration = tr.select("td:nth-child(6)").text();
            citizenship = tr.select("td:nth-child(7)").text();
            discipline = tr.select("td:nth-child(8)").text();

            StringBuilder sb = new StringBuilder();
            sb.append(name).append(SPLIT_CHAR)
                    .append(licenseNumber).append(SPLIT_CHAR)
                    .append(licenseType).append(SPLIT_CHAR)
                    .append(status).append(SPLIT_CHAR)
                    .append(issued).append(SPLIT_CHAR)
                    .append(expiration).append(SPLIT_CHAR)
                    .append(citizenship).append(SPLIT_CHAR)
                    .append(discipline).append(SPLIT_LINE_CHAR);
            try {
                fileWriter.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void audit() throws GsfAuditException {

    }
}
