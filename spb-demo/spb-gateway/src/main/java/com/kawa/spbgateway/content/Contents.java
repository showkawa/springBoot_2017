package com.kawa.spbgateway.content;

public class Contents {
    /**
     * enhance log print
     */
    public final static String DEFAULT_PATH = "com.kawa.spbgateway.filter";
    public final static String GATEWAY_FILTER_PATH = "com.kawa.spbgateway.service";

    /**
     * sort order
     */
    public final static int MASK_ORDER = 10;

    /**
     * refresh route
     */
    public final static String DEFAULT_FOLDER_KEY = "spring.config.location";

    public final static String SEARCH_FOLDER_KEY = "customized.gateway.env.location";

    public final static String[] DEFAULT_RESOURCE_FILE_EXTENSIONS = new String[]{"yml", "yaml", "properties"};

    public final static String RESOURCE_FILE_EXTENSION_KEY = "resource.file.extensions";

    public final static String[] EXCLUDES = new String[]{"application.properties", "logback.xml", "apikey.properties"};

    /**
     * Route define field
     */
    public final static String CONFIG_GATEWAY_FILTER_CLASS_NAME = "BrianConfig";

    public final static String REWRITE_GATEWAY_FILTER_CLASS_NAME = "RewritePath";

    public final static String REWRITE_GATEWAY_FILTER_REGEXP = "regexp";

    public final static String REWRITE_GATEWAY_FILTER_REPLACEMENT = "replacement";

    public final static String GATEWAY_CONFIG_CLASS_AUTH = "auths";

    public final static String GATEWAY_CONFIG_CLASS_API_KEYS = "apiKeys";

    public final static String PREDICATE_PATH = "Path";

    public final static String FALL_BACK_URI = "http://${error-format}";


    private static String qualify(String attr) {
        return Contents.class.getName() + "." + attr;
    }
}
