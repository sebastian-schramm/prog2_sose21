package model.interfaces;

public interface AllgemeineKonstanten {

    String DEFAULT_RESOURCES_LOCATION = "src/resources/";
    String DEFAULT_APPLICATION_ICON = "file:src/resources/icons/test.jpg";
    int THREAD_AMOUNT = 12;
    int TRIANGLE_VERTICES = 4;
    String ASCII_STL_START_LINE = "solid";
    String ASCII_STL_END_LINE = "endsolid";
    String[] ASCII_TRIANGLE_PATTERN = {
            "facet normal",
            "outer loop",
            "vertex",
            "vertex",
            "vertex",
            "endloop",
            "endfacet"
    };

}
