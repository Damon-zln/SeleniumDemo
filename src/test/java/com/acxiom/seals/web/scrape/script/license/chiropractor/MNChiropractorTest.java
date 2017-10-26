package com.acxiom.seals.web.scrape.script.license.chiropractor;

import com.acxiom.seals.web.scrape.entity.Job;
import com.acxiom.seals.web.scrape.script.ScriptCommonTest;
import org.junit.Test;

public class MNChiropractorTest extends ScriptCommonTest {

    @Test
    public void testScript() throws InterruptedException {
        MNChiropractor chiropractor = new MNChiropractor();
        Job job = new Job();
        job.setProxy("10.51.1.140:8080");
        job.setOutputPath("D:\\temp\\AL");
        chiropractor.setJob(job);
        startScript(chiropractor);
    }
}
