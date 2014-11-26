package tests;

import org.junit.Assert;
import org.junit.Test;
import projeto.main.BETA;
import projeto.main.Category;
import projeto.main.TrustModel;

import java.util.ArrayList;
import java.util.List;

public class TrustModelTest {
    @Test
    public void testBETA() throws Exception {
        TrustModel beta = new BETA();
        Integer round = 0;
        String sourceId;
        Category category;

        List<String> sourceIds = new ArrayList<>();
        sourceIds.add("Quim");
        sourceIds.add("Ze");
        sourceIds.add("Manel");
        sourceIds.forEach(beta::addSourceId);

        // Does not always chose the same source
        // when scores are 0
        Assert.assertTrue(sourceIds.contains(
                beta.chooseSource(Category.randomCategory(), round)));
        Assert.assertFalse(choosesAlwaysSameSource(beta, round, Category.randomCategory()));

        round++;

        // Does not always chose the same source
        // when scores are 0 or negative
        sourceId = "Quim";
        category = Category.randomCategory();
        beta.addRecord(round, false, sourceId, category);

        Assert.assertFalse(choosesAlwaysSameSource(beta, round, category));


        // Picks the best source for a given category
        sourceId = "Quim";
        category = Category.randomCategory();
        beta.addRecord(round, true, sourceId, category);
        System.out.println("----------------------");
        Assert.assertEquals(sourceId, beta.chooseSource(category, round));

        sourceId = "Manel";
        category = Category.randomCategory();
        beta.addRecord(round, true, sourceId, category);
        beta.addRecord(round, true, sourceId, category);
        beta.addRecord(round, false, sourceId, category);
        beta.addRecord(round, true, sourceId, category);
        Assert.assertEquals(sourceId, beta.chooseSource(category, round));

        sourceId = "Quim";
        beta.addRecord(round, true, sourceId, category);
        beta.addRecord(round, true, sourceId, category);
        beta.addRecord(round, true, sourceId, category);
        beta.addRecord(round, true, sourceId, category);
        Assert.assertEquals(sourceId, beta.chooseSource(category, round));

    }

    private boolean choosesAlwaysSameSource(TrustModel beta, Integer round, Category category) {
        boolean alwaysSameSource = true;
        String firstSourceId = "";

        for (int i = 0; i < 60; i++)
            if (firstSourceId.equals(""))
                firstSourceId = beta.chooseSource(category, round);
            else if (!firstSourceId.equals(beta.chooseSource(category, round))) {
                alwaysSameSource = false;
                break;
            }

        return alwaysSameSource;
    }
}