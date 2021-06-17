package model.interfaces;

public interface AllgemeineKonstanten {

    public static final String DEFAULT_RESOURCES_LOCATION = "src/main/java/resources/";
    public static final int THREAD_AMOUNT = 12;
    public static final int TRIANGLE_VERTICES = 4;
    public static final String ASCII_STL_START_LINE = "solid";
    public static final String ASCII_STL_END_LINE = "endsolid";
    public static final String[] ASCII_TRIANGLE_PATTERN = {
            "facet normal",
            "outer loop",
            "vertex",
            "vertex",
            "vertex",
            "endloop",
            "endfacet"
    };

}
