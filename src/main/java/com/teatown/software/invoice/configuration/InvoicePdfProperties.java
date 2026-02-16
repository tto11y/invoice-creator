package com.teatown.software.invoice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for invoice PDF generation.
 */
@ConfigurationProperties(prefix = "invoice.pdf")
public class InvoicePdfProperties {

    private String dateFormat = "dd.MM.yyyy";
    private float marginMm = 40f;
    private int titleFontSize = 18;
    private int headingFontSize = 10;
    private int normalFontSize = 10;
    private int smallFontSize = 8;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public float getMarginMm() {
        return marginMm;
    }

    public void setMarginMm(float marginMm) {
        this.marginMm = marginMm;
    }

    public int getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(int titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public int getHeadingFontSize() {
        return headingFontSize;
    }

    public void setHeadingFontSize(int headingFontSize) {
        this.headingFontSize = headingFontSize;
    }

    public int getNormalFontSize() {
        return normalFontSize;
    }

    public void setNormalFontSize(int normalFontSize) {
        this.normalFontSize = normalFontSize;
    }

    public int getSmallFontSize() {
        return smallFontSize;
    }

    public void setSmallFontSize(int smallFontSize) {
        this.smallFontSize = smallFontSize;
    }
}
