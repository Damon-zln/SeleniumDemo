package com.acxiom.seals.web.scrape.framework;

import com.acxiom.seals.web.scrape.entity.Job;
import com.acxiom.seals.web.scrape.framework.exception.GsfAuditException;
import com.acxiom.seals.web.scrape.framework.exception.GsfScrapingException;

public interface WebScrape {

    public void scrape() throws GsfScrapingException;

    public void audit() throws GsfAuditException;

    public void setJob(Job job);
}
