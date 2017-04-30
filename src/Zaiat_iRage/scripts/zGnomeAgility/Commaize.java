package Zaiat_iRage.scripts.zGnomeAgility;

// Commaize a number.

public class Commaize
{

    /**
     * doubleToString
     */
    public static String doubleToString(double in) {
        Double d = in;
        int di = d.intValue();
        if (d == di) {
            // Optimization for easy floats.
            return String.format("%d", di);
        }

        String floatnum = String.format("%f", d);

        // Chop off trailing zeros.
        int i = floatnum.length() - 1;
        while (i >= 0 && floatnum.charAt(i) == '0') {
            i--;
        }
        floatnum = floatnum.substring(0, i+1);

        // Chop off the period if there is no mantissa.
        i = floatnum.length() - 1;
        if (i >= 0 && floatnum.charAt(i) == '.') {
            floatnum = floatnum.substring(0, i);
        }

        return floatnum;
    }

    /**
     * Insert thousands separator into a number and, optionally, specify
     * the number of decimal places.
     *
     * Here are some examples that demonstrate how it is used.
     *
     *   string x;
     *   x=commaize(1234)                        # 1,234
     *   x=commaize(-1234)                       # -1,234
     *   x=commaize(12345.678)                   # 12,345.678
     *   x=commaize(-12345.678)                  # -12,345.678
     *   x=commaize(12345.678, 2)                # 12,345.69
     *   x=commaize(12345.678, 1)                # 12,345.7
     *   x=commaize(1234, -1, '.')               # 1.234
     *   x=commaize("1234567,89", -1, '.', ',')  # EU: 1.234.567,89
     *
     * The arguments are:
     *
     * @param num  The number: int, float or string.
     * @param dpl  Decimal places (default: -1 -> do nothing).
     * @param sep  Thousands separator (default: ",").
     * @param dpt  Decimal point (default: ".").
     * @returns a formatted string.
     */
    public static <T> String commaize(T num, int dpl, char sep, char dpt) {
        String rep = num.toString();
        if (rep.indexOf('E') >= 0) {
            rep = doubleToString(Double.parseDouble(rep));
        }

        if (dpl > 0) {
            // Take advantage of built in rounding.
            String fmt = String.format("%%.%df", dpl);
            double value = Double.parseDouble(rep);
            rep = String.format(fmt, value);
        }

        // Get the parts of the number (characteristic and mantissa).
        //   123.45
        //   ^  ^^
        //   |  |+--- mantissa
        //   |  +---- decimal point
        //   +------- characteristic
        String characteristic = "";
        String mantissa = "";
        boolean past_dpt = true;
        for (int i=0; i<rep.length(); i++) {
            char ch = rep.charAt(i);
            if (ch == dpt) {
                past_dpt = false;
            }
            if (past_dpt) {
                characteristic += ch;
            }
            else {
                mantissa += ch;
            }
        }

        // Trim trailing zeros.
        if (dpl < 0) {
            int i = mantissa.length() - 1;
            while (i >= 0 && mantissa.charAt(i) == '0') {
                i--;
            }

            mantissa = mantissa.substring(0, i+1);
            i = mantissa.length() - 1;
            if (i >= 0 && mantissa.charAt(i) == dpt) {
                // Eliminate the decimal point separater if there are no
                // trailing values (e.g. "1.").
                mantissa = "";
            }
        }

        // Handle negative numbers.
        String neg = "";
        if (characteristic.charAt(0) == '-') {
            neg = "-";
            characteristic = characteristic.substring(1);
        }

        // Insert the command moving from left to right.
        String result = "";
        int offset = characteristic.length() % 3;  // offset to first comma
        for(int i=0; i<characteristic.length(); i++) {
            if (((i%3) == offset) && (i > 0)) {
                result += sep;
            }
            result += characteristic.charAt(i);
        }

        rep = neg + result + mantissa;
        return rep;
    }
    public static <T> String commaize(T num) {
        return commaize(num, -1, ',', '.');
    }
    public static <T> String commaize(T num, int dpl) {
        return commaize(num, dpl, ',', '.');
    }

    /**
     * main.
     */
    public static void main(String[] args) {
        int id = 0;
        String result;
        String str;
        String msg;

        // Test positive integers.
        int inums[] = new int[] {1, 12, 123, 1234, 12345, 123456, 1234567, 12345678, 123456789};
        for(int val : inums) {
            result = commaize(val);
            msg = String.format("%4d %20d %20s", ++id, val, result);
            System.out.println(msg);
        }

        // Test positive floating points.
        double dnums[] = new double[] {1.23, 12.34, 123.45, 1234.56, 12345.67, 123456.78, 1234567.89, 12345678.9, 1.0};
        for(double val : dnums) {
            str = doubleToString(val);
            result = commaize(val);
            msg = String.format("%4d %20s %20s", ++id, str, result);
            System.out.println(msg);
        }

        // Test negative integers.
        for(int val : inums) {
            int nval = -val;
            result = commaize(nval);
            msg = String.format("%4d %20d %20s", ++id, nval, result);
            System.out.println(msg);
        }

        // Test negative floating points.
        for(double val : dnums) {
            double nval = -val;
            result = commaize(nval);
            str = doubleToString(nval);
            msg = String.format("%4d %20s %20s", ++id, str, result);
            System.out.println(msg);
        }

        // String test.
        str = "123456789";
        result = commaize(str);
        msg = String.format("%4d %20s %20s", ++id, str, result);
        System.out.println(msg);

        // EU style
        str = "12345678,9";
        result = commaize(str, -1, '.', ',');
        msg = String.format("%4d %20s %20s", ++id, str, result);
        System.out.println(msg);

        // dollarize
        // Round to 2 significant digits.
        double ddata[] = new double[] {12345.123, 12345, 12345.1251};
        for(double val : ddata) {
            str = doubleToString(val);
            result = commaize(val, 2);
            msg = String.format("%4d %20s %20s", ++id, str, result);
            System.out.println(msg);
        }

        // 1 decimal place
        str = "1234567.89";
        result = commaize(str, 1);
        msg = String.format("%4d %20s %20s", ++id, str, result);
        System.out.println(msg);
    }
}