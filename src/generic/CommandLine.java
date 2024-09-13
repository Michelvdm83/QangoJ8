package generic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.UnaryOperator;

import static generic.AnsiColors.*;

public class CommandLine {

    private static final Scanner in = new Scanner(System.in);

    public static void clearScreen() {
        for (int i = 0; i < Toolkit.getDefaultToolkit().getScreenSize().height; i++) {
            System.out.println();
        }
        //System.out.println("\n".repeat(Toolkit.getDefaultToolkit().getScreenSize().heigth));
    }

    private static int askForInt() {
        do {
            if (in.hasNextInt()) {
                int returnInt = in.nextInt();
                in.nextLine();
                return returnInt;
            } else {
                in.nextLine();
                System.out.println("Please enter a number");
            }
        } while (true);
    }

    private static int askForInt(int min, int max) {
        do {
            int returnInt = askForInt();
            if (min <= returnInt && returnInt <= max) {
                return returnInt;
            } else {
                System.out.printf("Please enter a number between %d and %d (both inclusive)%n", min, max);
            }
        } while (true);
    }

    public static int askForInt(String prompt) {
        System.out.print(prompt);
        return askForInt();
    }

    public static int askForInt(String prompt, int min, int max) {
        System.out.print(prompt);
        return askForInt(min, max);
    }

    public static String askForString(String prompt) {
        System.out.print(prompt);
        return in.nextLine();
    }

    public static String askForStringFromMenu(String... options) {
        if (options.length < 1)
            throw (new IllegalArgumentException("method askForStringFromMenu needs at least 1 argument"));
        int choice = askForIntFromMenu(options);
        return options[choice - 1];
    }

    //given the number of options of a menu and the longest number of characters for an option,
    //gives the number of lines needed to print them on the screen
    private static int calculateNrOfLinesForMenu(int nrOfOptions, int longestOptionLength) {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int maxOptionsPerLine = screenWidth / longestOptionLength;

        int lines;
        int offsetForMultiColums = 5;
        if (nrOfOptions <= offsetForMultiColums) {
            lines = nrOfOptions;
        } else if (nrOfOptions / maxOptionsPerLine < offsetForMultiColums) {
            lines = offsetForMultiColums;
        } else {
            lines = nrOfOptions / maxOptionsPerLine;
            if (nrOfOptions % maxOptionsPerLine > 0) lines++;
        }
        return lines;
    }

    public static int askForIntFromMenu(String... options) {
        if (options.length < 1)
            throw (new IllegalArgumentException("method askForIntFromMenu needs at least 1 argument"));

        List<String> menu = new ArrayList<>();
        int longestOptionSize = 0;
        int menuIteratorSpace = String.valueOf(options.length+1).length();
        String menuItemFormat = "%" + menuIteratorSpace + "d. %s";
        for (int optionIterator = 0; optionIterator < options.length; optionIterator++) {
            menu.add(String.format(menuItemFormat, (optionIterator + 1), options[optionIterator]));
            //menu.add((optionIterator + 1) + ". " + options[optionIterator]);
            longestOptionSize = Math.max(longestOptionSize, menu.get(optionIterator).length());
        }
        longestOptionSize += 2;

        int size = menu.size();
        int totalLines = calculateNrOfLinesForMenu(size, longestOptionSize);
        int maxColumns = size / totalLines;
        if (size % totalLines > 0) maxColumns++;

        String optionFormat = "%-" + longestOptionSize + "s";
        for (int line = 0; line < totalLines; line++) {
            for (int column = 0; column < maxColumns; column++) {
                int menuItem = line + column * totalLines;
                if(menuItem >= menu.size()){
                    continue;
                }
                System.out.printf(optionFormat, menu.get(menuItem));
            }
            System.out.println();
        }

//        for (int line = 0; line < totalLines; line++) {
//            for (int column = 0, menuItem = line; menuItem < size; ++column, menuItem = line + column * totalLines) {
//                System.out.printf(optionFormat, menu.get(menuItem));
//            }
//            System.out.println();
//        }
        return askForInt(1, options.length);
    }

    public static int askForIntFromMenu(String question, String[] options) {
        System.out.println(question);
        return askForIntFromMenu(options);
    }

    public static <T extends Enum<T>> T askForEnumValueFromEnumMenu(T[] enumValues) {
        String[] optionsAsStrings = new String[enumValues.length];
        for (int i = 0; i < enumValues.length; i++) {
            optionsAsStrings[i] = enumValues[i].toString().toLowerCase().replace("_", " ");
        }
        int choice = askForIntFromMenu(optionsAsStrings);
        return enumValues[choice - 1];
    }

    public static <T extends Enum<T>> T askForEnumValueFromEnumMenu(String question, T[] enumValues) {
        System.out.println(question);
        return askForEnumValueFromEnumMenu(enumValues);
    }

    public static boolean askYesOrNo(String prompt) {
        System.out.println(prompt);
        return (askForIntFromMenu("yes", "no") == 1);
    }

    private static String getStringColored(String text, AnsiColors color) {
        return color.getColor() + text + RESET.getColor();
    }

    public static UnaryOperator<String> getNewRGBColorStringOperator(int r, int g, int b) {
        if ((r < 0 || g < 0 || b < 0) || (r > 255 || g > 255 || b > 255))
            throw new IllegalArgumentException("rgb values range from 0-255 inclusive!");
        String color = "\u001B[38;2;" + r + ";" + g + ";" + b + "m";
        return (s -> color + s + RESET.getColor());
    }

    public static UnaryOperator<String> getNewRGBColorBackgroundOperator(int r, int g, int b) {
        if ((r < 0 || g < 0 || b < 0) || (r > 255 || g > 255 || b > 255))
            throw new IllegalArgumentException("rgb values range from 0-255 inclusive!");
        String color = "\u001B[48;2;" + r + ";" + g + ";" + b + "m";
        return (s -> color + s + RESET.getColor());
    }

    public static UnaryOperator<String> red = s -> getStringColored(s, RED);
    public static UnaryOperator<String> green = s -> getStringColored(s, GREEN);
    public static UnaryOperator<String> cyan = s -> getStringColored(s, CYAN);
    public static UnaryOperator<String> yellow = s -> getStringColored(s, YELLOW);
    public static UnaryOperator<String> blue = s -> getStringColored(s, BLUE);
    public static UnaryOperator<String> purple = s -> getStringColored(s, PURPLE);
    public static UnaryOperator<String> white = s -> getStringColored(s, WHITE);
    public static UnaryOperator<String> black = s -> getStringColored(s, BLACK);
}