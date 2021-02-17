package com.rentpal.agreement.common;

/*
 * @author frank
 * @created 16 Dec,2020 - 1:41 AM
 */

import java.util.regex.Pattern;

public class Regex {
    public static final Pattern NOT_ALLOWED_CHARACTERS=Pattern.compile("^[^`~.><\":{}=+]+$");
    public static final Pattern POSTAL_CODE=Pattern.compile("^[0-9]{5}$");
    public static final Pattern FLOAT=Pattern.compile("^([0-9]{1,5}\\.[0-9]{1,2}|[0-9]{1,5})$");
    public static final Pattern INTEGER=Pattern.compile("^[0-9]{1,8}$");
    public static final Pattern ALPHA_NUM=Pattern.compile("^[a-zA-Z0-9]+$");
    public static final Pattern BOOLEAN=Pattern.compile("^(true|false)$");
    public static final Pattern ALPHAWITHSPACE=Pattern.compile("^[a-zA-Z]+(?:[\\s]{1}[a-zA-Z]+)*$");
    public static final Pattern EMAIL=Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    public static final Pattern DATE=Pattern.compile("^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) (0[1-9]|[12][0-9]|3[01]), (19|20)[0-9][0-9]$");
    public static final Pattern NATIONALITY=Pattern.compile("^(AFG|ALB|DZA|ASM|AND|AGO|AIA|ATA|ATG|ARG|ARM|ABW|AUS|AUT|AZE|BHS|BHR|BGD|BRB|BLR|BEL|BLZ|BEN|BMU|BTN|BOL|BES|BIH|BWA|BVT|BRA|IOT|BRN|BGR|BFA|BDI|CPV|KHM|CMR|CAN|CYM|CAF|TCD|CHL|CHN|CXR|CCK|COL|COM|COD|COG|COK|CRI|HRV|CUB|CUW|CYP|CZE|CIV|DNK|DJI|DMA|DOM|ECU|EGY|SLV|GNQ|ERI|EST|SWZ|ETH|FLK|FRO|FJI|FIN|FRA|GUF|PYF|ATF|GAB|GMB|GEO|DEU|GHA|GIB|GRC|GRL|GRD|GLP|GUM|GTM|GGY|GIN|GNB|GUY|HTI|HMD|VAT|HND|HKG|HUN|ISL|IND|IDN|IRN|IRQ|IRL|IMN|ISR|ITA|JAM|JPN|JEY|JOR|KAZ|KEN|KIR|PRK|KOR|KWT|KGZ|LAO|LVA|LBN|LSO|LBR|LBY|LIE|LTU|LUX|MAC|MDG|MWI|MYS|MDV|MLI|MLT|MHL|MTQ|MRT|MUS|MYT|MEX|FSM|MDA|MCO|MNG|MNE|MSR|MAR|MOZ|MMR|NAM|NRU|NPL|NLD|NCL|NZL|NIC|NER|NGA|NIU|NFK|MNP|NOR|OMN|PAK|PLW|PSE|PAN|PNG|PRY|PER|PHL|PCN|POL|PRT|PRI|QAT|MKD|ROU|RUS|RWA|REU|BLM|SHN|KNA|LCA|MAF|SPM|VCT|WSM|SMR|STP|SAU|SEN|SRB|SYC|SLE|SGP|SXM|SVK|SVN|SLB|SOM|ZAF|SGS|SSD|ESP|LKA|SDN|SUR|SJM|SWE|CHE|SYR|TWN|TJK|TZA|THA|TLS|TGO|TKL|TON|TTO|TUN|TUR|TKM|TCA|TUV|UGA|UKR|ARE|GBR|UMI|USA|URY|UZB|VUT|VEN|VNM|VGB|VIR|WLF|ESH|YEM|ZMB|ZWE|ALA)$");
}
