package projeto.main;

import java.util.*;

public class BETA extends TrustModel {
    private class SourceTrust {
        public List<Integer> round = new ArrayList<>();
        public List<Double> s = new ArrayList<>(), r = new ArrayList<>();
    }

    private Map<Category, Map<String, SourceTrust>> categoryTrust = new HashMap<>(Category.values().length);
    private Random random = new Random();
    private Integer currentRound = 0;
    private final Double FORGETTING_FACTOR = 0.8;

    @Override
    public String chooseSource(Category category) {
        Double maxValue = 0.0, currentValue;
        String maxSourceId = "";

        for (Map.Entry<String, SourceTrust> e : categoryTrust.get(category).entrySet()) {
            // System.out.println(e.getKey());
            currentValue = calculateTrust(e.getValue());

            if (currentValue > maxValue) {
                maxSourceId = e.getKey();
                maxValue = currentValue;
            }
        }

        if (maxSourceId.equals("") || maxValue < 0)
            return sourceIds.get(random.nextInt(sourceIds.size()));

        return maxSourceId;
    }

    @Override
    public void addRecord(Boolean correctAnswer, String sourceId, Category category) {
        super.addRecord(correctAnswer, sourceId, category);

        SourceTrust source = categoryTrust.get(category).get(sourceId);

        if (correctAnswer)
            scaleVtoRS(1.0, source);
        else
            scaleVtoRS(-1.0, source);

        currentRound++;
    }

    @Override
    public void addSourceId(String sourceId) {
        if (sourceIds.contains(sourceId))
            return;

        super.addSourceId(sourceId);

        for (Category category : Category.values()) {
            initializeAgentCategory(sourceId, category);
        }
    }

    private void initializeAgentCategory(String sourceId, Category category) {
        Map<String, SourceTrust> sourcesInCategory;

        if (categoryTrust.containsKey(category))
            sourcesInCategory = categoryTrust.get(category);
        else {
            sourcesInCategory = new HashMap<>();
            categoryTrust.put(category, sourcesInCategory);
        }

        if (!sourcesInCategory.containsKey(sourceId))
            sourcesInCategory.put(sourceId, new SourceTrust());
    }


    private void scaleVtoRS(Double v, SourceTrust source) {
        source.r.add((1 - v) / 2.0);
        source.s.add((1 + v) / 2.0);
        source.round.add(currentRound);
    }

    /*
    private Double sum(List<Double> array) {
        return array.stream().reduce(0.0, Double::sum);
    }
     */

    private Double weightedSum(List<Double> values, List<Integer> round) {
        Double sum = 0.0;

        for (int i = 0; i < values.size(); i++)
            sum += values.get(i) * Math.pow(FORGETTING_FACTOR, currentRound - round.get(i));

        return sum;
    }

    private Double calculateTrust(SourceTrust source) {
        Double sumS, sumR;

        sumS = weightedSum(source.s, source.round);
        sumR = weightedSum(source.r, source.round);
        //System.out.println("Depois: " + (sumS - sumR) / (sumS + sumR + 2));

        /*
        sumS = sum(source.s);
        sumR = sum(source.r);
        System.out.println("Antes: " + (sumS - sumR) / (sumS + sumR + 2));
        */

        return (sumS - sumR) / (sumS + sumR + 2);
    }
}
