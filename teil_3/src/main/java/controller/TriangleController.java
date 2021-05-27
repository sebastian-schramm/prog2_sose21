package controller;

import model.Triangle;
import model.Vertex;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriangleController {
    private Triangle triangle = null;

    public void init(String dateiName) {

    }

    private TriangleController() {

    }

    public static TriangleController getInstance() {
        return TriangleControllerHolder.INSTANCE;
    }

    private static class TriangleControllerHolder {
        private static final TriangleController INSTANCE = new TriangleController();
    }

    //Interpretiere Daten
    public void constructTriangle(ArrayList<String> relevantData)
    {
        int count = 0;

        for (String s:relevantData)
        {
            if (count == 0)
            {
                int i = 0;
                String numberOnly = s.replaceAll("[^-?0-9]+", " ");
                List<String> test = Arrays.asList(numberOnly.trim().split(" "));
                Float.parseFloat(s);
                System.out.println(test);
                float x = Float.parseFloat(test.get(i));
                float y = Float.parseFloat(test.get(i+1));
                float z = Float.parseFloat(test.get(i+2));
                Vertex normal = new Vertex(x,y,z);
                count += 1;
                continue;
            }
            if (count == 1)
            {
                System.out.println("baum");
                String numberOnly = s.replaceAll("[^-?0-9.]+", " ");
                List<String> test = Arrays.asList(numberOnly.trim().split(" "));
                System.out.println(test);
            }

        }

        for (String s:relevantData)
        {
            System.out.println(s);
        }
        System.out.println("----------------------------");

    }


}
