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
        List<String> sourceIds = new ArrayList<>();
        sourceIds.add("Quim");
        sourceIds.add("Ze");
        sourceIds.add("Manel");

        sameSourceTests(sourceIds);
        countingTrues(sourceIds);
        notPickingLastInsert(sourceIds);
        forgetting(sourceIds);
    }

    private void forgetting(List<String> sourceIds) {
        Category category;
        String sourceId;// Forgetting
        TrustModel beta = new BETA();
        sourceIds.forEach(beta::addSourceId);

        category = Category.randomCategory();
        sourceId = "Quim";
        beta.addRecord(true, sourceId, category);
        Assert.assertEquals(sourceId, beta.chooseSource(category));
        sourceId = "Manel";
        beta.addRecord(true, sourceId, category);
        Assert.assertEquals(sourceId, beta.chooseSource(category));
        sourceId = "Ze";
        beta.addRecord(true, sourceId, category);
        Assert.assertEquals(sourceId, beta.chooseSource(category));
    }

    private void notPickingLastInsert(List<String> sourceIds) {
        Category category;
        String sourceId;
        TrustModel beta = new BETA();
        sourceIds.forEach(beta::addSourceId);
        category = Category.randomCategory();

        sourceId = "Ze";
        beta.addRecord(true, sourceId, category);
        beta.addRecord(true, sourceId, category);
        beta.addRecord(false, sourceId, category);
        sourceId = "Manel";
        beta.addRecord(false, sourceId, category);
        beta.addRecord(false, sourceId, category);
        sourceId = "Quim";
        beta.addRecord(false, sourceId, category);
        beta.addRecord(true, sourceId, category);
        Assert.assertEquals("Ze", beta.chooseSource(category));
    }

    // Picks the best source for a given category
    private void countingTrues(List<String> sourceIds) {
        String sourceId;
        Category category;
        TrustModel beta = new BETA();
        sourceIds.forEach(beta::addSourceId);

        sourceId = "Quim";
        category = Category.randomCategory();
        beta.addRecord(true, sourceId, category);
        Assert.assertEquals(sourceId, beta.chooseSource(category));

        sourceId = "Manel";
        category = Category.randomCategory();
        beta.addRecord(true, sourceId, category);
        beta.addRecord(true, sourceId, category);
        beta.addRecord(false, sourceId, category);
        beta.addRecord(true, sourceId, category);
        Assert.assertEquals(sourceId, beta.chooseSource(category));

        sourceId = "Quim";
        beta.addRecord(true, sourceId, category);
        beta.addRecord(true, sourceId, category);
        beta.addRecord(true, sourceId, category);
        beta.addRecord(true, sourceId, category);
        Assert.assertEquals(sourceId, beta.chooseSource(category));
    }

    private void sameSourceTests(List<String> sourceIds) {
        String sourceId;
        Category category;
        TrustModel beta = new BETA();
        sourceIds.forEach(beta::addSourceId);

        // Does not always chose the same source
        // when scores are 0
        Assert.assertTrue(sourceIds.contains(
                beta.chooseSource(Category.randomCategory())));
        Assert.assertFalse(choosesAlwaysSameSource(beta, Category.randomCategory()));

        // Does not always chose the same source
        // when scores are 0 or negative
        sourceId = "Quim";
        category = Category.randomCategory();
        beta.addRecord(false, sourceId, category);

        Assert.assertFalse(choosesAlwaysSameSource(beta, category));
    }

    private boolean choosesAlwaysSameSource(TrustModel beta, Category category) {
        boolean alwaysSameSource = true;
        String firstSourceId = "";

        for (int i = 0; i < 60; i++)
            if (firstSourceId.equals(""))
                firstSourceId = beta.chooseSource(category);
            else if (!firstSourceId.equals(beta.chooseSource(category))) {
                alwaysSameSource = false;
                break;
            }

        return alwaysSameSource;
    }
}