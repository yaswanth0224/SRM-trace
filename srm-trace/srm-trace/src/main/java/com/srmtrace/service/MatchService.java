package com.srmtrace.service;

import com.srmtrace.model.Item;
import org.apache.commons.text.similarity.CosineSimilarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchService {
    // simple tokenizer: split by non-word
    private static Map<CharSequence, Integer> toVector(String s) {
        Map<CharSequence, Integer> m = new HashMap<>();
        if (s == null) return m;
        String[] parts = s.toLowerCase().split("\\W+");
        for (String p : parts) {
            if (p.isBlank()) continue;
            m.put(p, m.getOrDefault(p, 0) + 1);
        }
        return m;
    }

    public static double similarityPercent(Item a, Item b) {
        String sa = (a.title == null ? "" : a.title) + " " + (a.description == null ? "" : a.description);
        String sb = (b.title == null ? "" : b.title) + " " + (b.description == null ? "" : b.description);
        CosineSimilarity cs = new CosineSimilarity();
        Map<CharSequence, Integer> va = toVector(sa);
        Map<CharSequence, Integer> vb = toVector(sb);
        Double score = cs.cosineSimilarity(va, vb);
        if (score == null) score = 0.0;
        return Math.round(score * 10000.0) / 100.0; // two decimals as percent
    }

    // Returns ranked list of matches (for a given item vs all others)
    public static java.util.List<java.util.Map.Entry<Item, Double>> rankMatches(Item origin, List<Item> all) {
        java.util.List<java.util.Map.Entry<Item, Double>> out = new java.util.ArrayList<>();
        for (Item it : all) {
            if (it.id == origin.id) continue;
            if (origin.type.equals(it.type)) continue; // match lost->found or found->lost only
            double p = similarityPercent(origin, it);
            out.add(new java.util.AbstractMap.SimpleEntry<>(it,p));
        }
        out.sort((a,b)->Double.compare(b.getValue(), a.getValue()));
        return out;
    }
}
