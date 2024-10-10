package seng202.team1.cucumber;

import io.cucumber.java.ParameterType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GlobalParamTypeDefs {
    @ParameterType("\\[([a-zA-Z0-9, ]*)\\]")
    public ArrayList<String> listOfTags(String tags) {
        return (ArrayList<String>) Arrays.stream(tags.split(", ?"))
                .map(String::toString)
                .collect(Collectors.toList());
    }

    @ParameterType("\\[([0-9, ]*)\\]")
    public ArrayList<Integer> listOfInts(String ints) {
        return (ArrayList<Integer>) Arrays.stream(ints.split(", ?"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
