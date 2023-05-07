

package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
            File file = new File(args[0]);
            try (Scanner scanner = new Scanner(file)) {

                String fileText = scanner.nextLine();

                System.out.println("The text is: \n" + fileText + "\n");
                System.out.println("Words: " + words(fileText));
                System.out.println("Sentences: " + sentences(fileText));
                System.out.println("Characters: " + characters(fileText));
                System.out.println("Syllables: " + countSyllablesInText(fileText));
                System.out.println("Polysyllables: " + polysyllables(fileText));
                System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
                Scanner scanner1 = new Scanner(System.in);
                String whichScore = scanner1.next();
                switch (whichScore) {
                    case "ARI" -> {
                        System.out.println("The score is: " + ARI(fileText));
                        System.out.println("This text should be understood by " + table(ARI(fileText)) + " year-olds.");
                    }
                    case "FK" -> {
                        System.out.println("The score is: " + FleschKnicaid(fileText));
                        System.out.println("This text should be understood by " + table(FleschKnicaid(fileText)) + " year-olds.");
                    }
                    case "SMOG" -> {
                        System.out.println("The score is: " + SMOG(fileText));
                        System.out.println("This text should be understood by " + table(SMOG(fileText)) + " year-olds.");
                    }
                    case "CL" -> {
                        System.out.println("The score is: " + ColemanLiau(fileText));
                        System.out.println("This text should be understood by " + table(ColemanLiau(fileText)) + " year-olds.");
                    }
                    case "all" -> {
                        System.out.println("Automated Readability Index: " + ARI(fileText) + " (about " + table(ARI(fileText)) + "-year-olds.)");
                        System.out.println("Flesch-Kincaid readability tests: " + FleschKnicaid(fileText) + " (about " + table(FleschKnicaid(fileText)) + "-year-olds.)");
                        System.out.println("Simple Measure of Gobbledygook: " + SMOG(fileText) + " (about " + table(SMOG(fileText)) + "-year-olds.)");
                        System.out.println("Coleman-Liau index: " + ColemanLiau(fileText) + " (about " + table(ColemanLiau(fileText)) + "-year-olds.)");
                        double average = (double) (table(ARI(fileText)) + table(FleschKnicaid(fileText)) + table(SMOG(fileText)) + table(ColemanLiau(fileText))) / 4;
                        System.out.println("\nThis text should be understood in average by " + average + "-year-olds.");
                    }
                }


            }  catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
    }
    public static double roundTo2DecimalPlace(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static int table(double score) {
        int scoreInt = (int) Math.ceil(score);
        return switch (scoreInt) {
            case 1 -> 6;
            case 2 -> 7;
            case 3 -> 8;
            case 4 -> 9;
            case 5 -> 10;
            case 6 -> 11;
            case 7 -> 12;
            case 8 -> 13;
            case 9 -> 14;
            case 10 -> 15;
            case 11 -> 16;
            case 12 -> 17;
            case 13 -> 18;
            case 14 -> 22;
            default -> 0;
        };
    }

    public static double ARI(String file) {
        double charWord = (double) characters(file) / words(file);
        double wordSent = (double) words(file) / sentences(file);
        double score = 4.71 * charWord + 0.5 * wordSent - 21.43;
        score = roundTo2DecimalPlace(score);
        return score;
    }

    public static double FleschKnicaid(String file) {
        double wordsSent = (double) words(file) / sentences(file);
        double syllablesWords = (double) countSyllablesInText(file) / words(file);
        double score = 0.39 * wordsSent + 11.8 * syllablesWords - 15.59;
        score = roundTo2DecimalPlace(score);
        return score;
    }

    public static double SMOG(String file) {
        double score = 1.043 * Math.sqrt(polysyllables(file) * ((double) 30 / sentences(file))) + 3.1291;
        score = roundTo2DecimalPlace(score);
        return score;
    }

    public static double ColemanLiau(String file) {
        double L = (double) characters(file) * 100 / words(file);
        double S = (double) sentences(file) * 100 / words(file);
        double score =  0.0588 * L - 0.296 * S - 15.8;
        score = roundTo2DecimalPlace(score);
        return score;
    }

    public static int polysyllables(String text) {
        text = text.replaceAll("\\p{Punct}", "");
        String[] words = text.split("\\s+");
        int polysyllables = 0;
        for (String word:
             words) {
            if (countSyllables(word) > 2) {
                polysyllables++;
            }
        }
        return polysyllables;
    }

    public static int countSyllables(String word) {
        String vowels = "aeiouy";
        int count = 0;
        boolean lastWasVowel = false;
        for (int i = 0; i < word.length(); i++) {
            char c = Character.toLowerCase(word.charAt(i));
            if (vowels.indexOf(c) != -1) {
                if (!lastWasVowel) {
                    count++;
                    lastWasVowel = true;
                }
            } else {
                lastWasVowel = false;
            }
        }
        if (word.endsWith("e")) {
            count--;
        }
        if (count == 0) {
            count = 1;
        }
        return count;
    }

    public static int countSyllablesInText(String text) {
        text = text.replaceAll("\\p{Punct}", "");
        String[] words = text.split("\\s+");
        int syllableCount = 0;
        for (String word : words) {
            syllableCount += countSyllables(word);
        }
        return syllableCount;
    }

    public static int sentences(String file) {
        String[] sentence = file.split("[.?!]");
        return sentence.length;
    }

    public static int words(String file) {
        String[] word = file.split("\\s+");
        return word.length;
    }

    public static int characters(String file) {
        char[] chars = file.toCharArray();
        return chars.length - words(file) + 1;
    }
}
