package com.example.capstonedesign.retrofit;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RankingResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("ranking")
    private List<Ranking> ranking;

    public RankingResponse(List<Ranking> ranking) {
        this.ranking = ranking;
    }
    public List<Ranking> getRanking() {
        return ranking;
    }
    public void setRanking(List<Ranking> newRanking) {
        ranking = newRanking;
    }
    public String getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}
