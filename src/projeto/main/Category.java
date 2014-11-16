package projeto.main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Category {
    GERAL,
    DESPORTO,
    COZINHA;

    private static final List<Category> values = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int size = values.size();
    private static final Random random = new Random();

    public static Category randomCategory() {
       return values.get(random.nextInt(size));
    }
}