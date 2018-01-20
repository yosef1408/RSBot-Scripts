package Aff1x.choppenheimer.Util;

public class TreeEnum {

    public enum TreeType {
        MAPLE("Maple Tree"),
        OAK("Oak"),
        REGULAR("Tree", "Dead Tree"),
        WILLOW("Willow Tree"),
        MAGIC("Magic Tree");

        public String[] name;

        TreeType(String... name) {
            this.name = name;
        }
    }
}
