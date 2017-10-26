package com.acxiom.seals.web.scrape.framework;

import com.acxiom.seals.web.scrape.entity.Job;
import com.acxiom.seals.web.scrape.framework.exception.GsfAuditException;
import com.acxiom.seals.web.scrape.framework.exception.GsfScrapingException;

import java.util.concurrent.RecursiveTask;

public abstract class ForkableWebScrape extends RecursiveTask<WebScrapeState> implements WebScrape {

    private static final long serialVersionUID = 3289067219027567558L;

    protected Job job;

    protected WebScrapeState compute(){
        try {
            scrape();
            audit();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return WebScrapeState.AUDIT_SUCCESS;
    }

    public abstract void scrape() throws GsfScrapingException;

    public abstract void audit() throws GsfAuditException;

    public void setJob(Job job) {
        this.job = job;
    }
}
