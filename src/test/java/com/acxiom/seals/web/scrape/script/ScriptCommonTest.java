package com.acxiom.seals.web.scrape.script;

import com.acxiom.seals.web.scrape.framework.CommonWebScrape;
import com.acxiom.seals.web.scrape.framework.ForkableWebScrape;
import com.acxiom.seals.web.scrape.framework.WebScrapeState;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ScriptCommonTest {

    private ExecutorService executorService;

    private ForkJoinPool forkJoinPool;

    @Before
    public void before() {
        executorService = Executors.newFixedThreadPool(1);
        forkJoinPool = new ForkJoinPool();
    }

    @After
    public void after() {
        executorService.shutdown();
    }

    public void startScript(CommonWebScrape scrapeScript) throws InterruptedException {
        List<Callable<WebScrapeState>> tasks = new ArrayList<Callable<WebScrapeState>>();
        tasks.add(scrapeScript);
        executorService.invokeAll(tasks);
    }

    public void startScript(ForkableWebScrape scrapeScript) {
        forkJoinPool.invoke(scrapeScript);
    }
}
