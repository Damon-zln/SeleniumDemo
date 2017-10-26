package com.acxiom.seals.web.scrape.script.common.utils;

import com.acxiom.seals.web.scrape.entity.Job;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;

public class CapabilitiesCreator {

    public static DesiredCapabilities createChromeCapabilities(Job job){
        //设置谷歌浏览器驱动，我放在项目的路径下，这个驱动可以帮你打开本地的谷歌浏览器
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        String outputPath = job.getOutputPath();
        String proxyString = job.getProxy();
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", outputPath);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
        chromeOptions.addArguments("--test-type");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        if (proxyString != null) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyString).setFtpProxy(proxyString).setSslProxy(proxyString);
            capabilities.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
            capabilities.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
            capabilities.setCapability(CapabilityType.PROXY, proxy);
//            capabilities.setCapability("http.proxy", proxy);
        }
        return capabilities;
    }

    public static DesiredCapabilities createFirefoxCapabilities(Job job){
        // set proxy
        String proxyString = job.getProxy();
        Proxy firefoxProxy = new Proxy();
        firefoxProxy.setHttpProxy(proxyString)
                .setFtpProxy(proxyString)
                .setSslProxy(proxyString);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, firefoxProxy);

        //set default download output path
        FirefoxProfile profile = new FirefoxProfile();
        String path = job.getOutputPath();
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", path);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
        profile.setPreference("pdfjs.disabled", true);
        profile.setPreference("permissions.default.image", 2);
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);

        return capabilities;
    }
}
