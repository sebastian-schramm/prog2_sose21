package model.interfaces;
/**
 * AllgemeineKonstanten Interface of the STL-Viewer
 *
 * @author Sebastian Schramm, Joel Pitzler, Christoph Senft
 * @version 1.0
 */
public interface AllgemeineKonstanten {

    String DEFAULT_APPLICATION_ICON = "file:src/resources/icons/img2.png";
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

    double ROUND_KOMMASTELLE = 1000.0;

}
