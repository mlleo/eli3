package com.it_intensive.eli3;

import java.util.HashMap;

public class Mart {
    private static Mart instance = null;
    private static HashMap<String, String> korToEng;
    private static HashMap<String, String> engToKor;
    private static String[][] sItemPair = {{"우유", "Milk"}, {"빵", "Bread"}, {"물", "Water"},
                                        {"과자", "snack"}, {"쌀", "Rice"}, {"딸기", "Strawberry"},
                                        {"오이", "Cucumber"}, {"치즈", "Cheese"}, {"오렌지", "Orange"},
                                        {"포도", "Grape"}, {"와인", "Wine"}};

    private Mart(){
        korToEng = new HashMap<>();
        engToKor = new HashMap<>();
        for(String[] pair : sItemPair){
            korToEng.put(pair[0], pair[1]);
            engToKor.put(pair[1], pair[0]);
        }
    }

    public static Mart getInstance(){
        if(instance == null)
            instance = new Mart();
        return instance;
    }

    public boolean isKorKey(String key){
        return korToEng.containsKey(key);
    }

    public String transKorToEng(String kor){
        return korToEng.get(kor);
    }

    public String transEngToKor(String eng){
        return engToKor.get(eng);
    }
}
