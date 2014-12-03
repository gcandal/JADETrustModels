/*package tests;

import org.junit.Assert;
import org.junit.Test;
import projeto.main.BETA;
import projeto.main.Category;
import projeto.main.TrustModel;

import java.util.ArrayList;
import java.util.List;

public class TrustModelTest {


    @Test
    public void testAddRecord() throws Exception {
        TrustModel beta = new BETA();
        Integer round = 0;

        List<String> agentIds = new ArrayList<>();
        agentIds.add("Quim");
        agentIds.add("Ze");
        agentIds.add("Manel");
        for (String agentId : agentIds)
            beta.addSourceId(agentId);

        Assert.assertTrue(agentIds.contains(
                beta.chooseSource(Category.randomCategory(), round++)));
    }

    @Test
    public void testChooseSource() throws Exception {

    }
}*/