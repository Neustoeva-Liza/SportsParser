package service;

import org.jsoup.nodes.Element;

public class StatusResolver {

    public static String resolve(Element match) {
        Element statusEl = match.selectFirst(".status");
        if (statusEl == null) return "UPCOMING";

        String classAttr = statusEl.attr("class").toLowerCase();
        if (classAttr.contains("minute")
                || classAttr.contains("set1")
                || classAttr.contains("set2")
                || classAttr.contains("set3")
                || classAttr.contains("q1")
                || classAttr.contains("q2")
                || classAttr.contains("q3")
                || classAttr.contains("q4")) {
            return "LIVE";
        }
        if (classAttr.contains("ns")) {
            return "UPCOMING";
        }
        return "FINISHED";

    }
}