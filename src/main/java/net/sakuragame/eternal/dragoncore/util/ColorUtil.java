package net.sakuragame.eternal.dragoncore.util;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorUtil {

    public static String parseColor(String s) {
        StringBuilder builder = new StringBuilder();
        List<String> colors = new ArrayList<>();
        int start = -1;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '<') {
                String subString = s.substring(i);
                if (subString.startsWith("<#")) {
                    int indexOf = subString.indexOf(">");
                    String c1 = subString.substring(1, indexOf);
                    colors = Arrays.asList(c1.split("-"));
                    start = i + indexOf + 1;
                } else if (subString.startsWith("<end>")) {

                    builder.append(parseColor(colors, s.substring(start, i)));
                    i += 4;
                    start = -1;
                }
            } else if (start == -1) {
                builder.append(s.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String parseColor(List<String> colors, String text) {
        String b = "";

        int length = text.length() / (colors.size() - 1);
        for (int j = 0; j < colors.size() - 1; j++) {

            Color colora = Color.decode("0x" + colors.get(j).substring(1));
            Color colorb = Color.decode("0x" + colors.get(j + 1).substring(1));
            int start = j * length;
            int end = (j + 1) * length;
            if (j == colors.size() - 2) {
                length = length + text.length() - end;
                end = text.length();

            }
            int i = 0;
            for (char c : text.substring(start, end).toCharArray()) {
               // System.out.println(i + "增加前" + b);
                String d = (getLocationColor(colora, colorb, length, i));
               // System.out.println(i+"增加了"+d+" "+c);
                b += d;
                b += (c);
               // System.out.println(i + "增加后" + b);
                i++;
            }

        }
        return b;
    }

    public static String getLocationColor(Color c1, Color c2, int distance, int location) {
        int r = c1.getRed() + Math.round(((float) (c2.getRed() - c1.getRed()) / (float) distance * (float) location));
        int g = c1.getGreen() + Math.round(((float) (c2.getGreen() - c1.getGreen()) / (float) distance * (float) location));
        int b = c1.getBlue() + Math.round(((float) (c2.getBlue() - c1.getBlue()) / (float) distance * (float) location));
        String hex = String.format("§#%02x%02x%02x", r, g, b);
        return hex;
    }
}
