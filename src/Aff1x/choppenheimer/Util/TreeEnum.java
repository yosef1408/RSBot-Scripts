package Aff1x.choppenheimer.Util;

public class TreeEnum {

    public enum TreeType {
        MAPLE("Maple Tree"),
        OAK("Oak Tree"),
        REGULAR("Tree", "Dead Tree"),
        MAGIC("Magic");

        public String[] name;

        TreeType(String... name) {
            this.name = name;
        }
    }
}
