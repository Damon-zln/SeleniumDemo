package com.acxiom.seals.web.scrape.script.common.utils;

import com.acxiom.seals.web.scrape.entity.GsfScrapingEntity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GsfFileWriter {
    private BufferedWriter bufferedWriter = null;

    public GsfFileWriter(String filePath) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(filePath));
    }

    public GsfFileWriter(String filePath,boolean flag) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(filePath,flag));
    }

    public void write(List<GsfScrapingEntity> entityList) throws IOException {
        for (GsfScrapingEntity entity : entityList) {
            bufferedWriter.write(entity.toString());
            bufferedWriter.newLine();
        }

        bufferedWriter.flush();
    }

    public void write(String str) throws IOException {
        bufferedWriter.write(str);
        bufferedWriter.flush();
    }

    public void close() throws IOException {
        bufferedWriter.close();
    }
}
