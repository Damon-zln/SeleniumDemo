package com.acxiom.seals.web.scrape.script.license.embalmer;

import com.acxiom.seals.web.scrape.entity.Job;
import com.acxiom.seals.web.scrape.script.ScriptCommonTest;
import org.junit.Test;

public class ALEmbalmerTest extends ScriptCommonTest {

    @Test
    public void testScript() throws InterruptedException {
        ALEmbalmer embalmer = new ALEmbalmer();
        Job job = new Job();
        job.setProxy("10.51.1.140:8080");
        job.setOutputPath("D:\\temp\\AL");
        embalmer.setJob(job);
        startScript(embalmer);
    }
}
