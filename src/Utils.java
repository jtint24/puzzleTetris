import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static <T> List<T> randomSubList(List<T> list, int newSize) {
        list = new ArrayList<>(list);
        Collections.shuffle(list);
        List<T> subList = list.subList(0, newSize);
        return subList;
    }
}
