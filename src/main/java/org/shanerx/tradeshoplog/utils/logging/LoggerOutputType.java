package org.shanerx.tradeshoplog.utils.logging;

public enum LoggerOutputType {



    TSV("tsv", "\t");

    private final String fileExtension, delimiter;

    LoggerOutputType(String fileExtension, String delimiter) {
        this.fileExtension = fileExtension;
        this.delimiter = delimiter;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
