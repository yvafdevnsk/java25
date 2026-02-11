import org.apache.commons.lang3.RandomUtils;

public class ReferenceNonJDKClass {
    public static void main(String[] args) {
        IO.println(RandomUtils.secure().randomInt());
    }
}