package com.acxiom.seals.web.scrape.framework;

import com.acxiom.seals.web.scrape.entity.Job;
import com.acxiom.seals.web.scrape.framework.exception.GsfAuditException;
import com.acxiom.seals.web.scrape.framework.exception.GsfScrapingException;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

public abstract class CommonWebScrape implements WebScrape, Callable<WebScrapeState> {

    private static final Logger LOG = Logger.getLogger(CommonWebScrape.class);

    protected Job job;

    public WebScrapeState call() throws Exception {
        try {
            scrape();
            audit();
        } catch (Exception e) {
            LOG.error("Failed to execute web scrape script, ", e);
            //TODO; set error message to job.
        }
        return WebScrapeState.AUDIT_SUCCESS;
    }

    public abstract void scrape() throws GsfScrapingException;

    public abstract void audit() throws GsfAuditException;

    public void setJob(Job job) {
        this.job = job;
    }
}
