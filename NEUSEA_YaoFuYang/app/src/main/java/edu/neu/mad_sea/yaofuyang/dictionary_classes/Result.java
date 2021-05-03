package edu.neu.mad_sea.yaofuyang.dictionary_classes;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private String result;
    private List<Result> results;

    public Result() {
        this.results = new ArrayList<>();
    }

    public Result(String result) {
        this.result = result;
        this.results = new ArrayList<>();
        this.results.add(this);
    }

    public String getResult() {
        return this.result;
    }

    public List<Result> getResults() {
        return results;
    }
}
