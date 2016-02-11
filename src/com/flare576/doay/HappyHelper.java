package com.flare576.doay;

import com.flare576.doay.model.ScoreCategory;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Happy Helper is a tool that the elite of Dice on a Yacht(tm) players can use to help up their game. The tool will
 * help you figure out your score for a given category, determine the BEST category for you, and even show you how the
 * dice would score in ALL categories, instantly!
 */
public class HappyHelper {
    private static Scanner gather = new Scanner(System.in);
    //This could be loaded in from a properties file, or some other source, to create more dynamic ads
    private static final String PRODUCT_PLUG = " Consider purchasing our Exclusive Gold Plated Polys(tm) from the ship store!";
    private static final String TRY_AGAIN_PART = " Try again, or (Q)uit. ";

    private static final String SCOREMODE_USER_ERROR = "Something wasn't quite right with your ScoreCategory Selection.";
    private static final String APPMODE_USER_ERROR = "Something wasn't quite right with your mode selection.";
    static final String INVALID_DICE_USER_ERROR = "Dice on a Yacht(tm) uses 8-sided Dice." + PRODUCT_PLUG;
    static final String INVALID_DICE_QTY_USER_ERROR = "Five (5) Dice are used in Dice on a Yacht(tm)." + PRODUCT_PLUG;


    static final String START_USER_PROMPT = "Please enter a mode: (C)alculate Points, (S)uggest ScoreCategory, (Q)uit.";
    static final String MODE_INTRO_USER_PROMPT = "What Category do you wish to calculate?";
    static final String ALL_CATEGORIES_USER_PROMPT = "Would you like to see the other totals? (Y)es, (N)o";
    static final String ROLL_USER_PROMPT = "Please enter your roll. For simplicity, you can omit spaces, comas, etc (e.g., 12312 or 88888)";

    static final String SHOULD_USE_USER_MSG = "You should use ";
    static final String SCORE_USER_MSG = "Your score will be: ";
    static final String GREETING_USER_MSG = "Welcome to the Dice on a Yacht(tm) Happy Helper(tm). ";
    public static final String PARTING_USER_MSG = "Thank you for using the Dice on a Yacht(tm) Happy Helper(tm)." + PRODUCT_PLUG;

    static final String INVALID_SCOREMODE = "Not a real scoreCategory";

    private static final String SCOREMODE_PROMPT;
    //Create this once
    static {
        String prompt = MODE_INTRO_USER_PROMPT + " (";
        String delim = "";
        ScoreCategory[] scoreCategories = ScoreCategory.values();
        for(int i = 0; i < scoreCategories.length; i++){
            prompt += delim + "(" + i + ")" + scoreCategories[i];
            delim = ", ";
        }
        prompt+=")";
        SCOREMODE_PROMPT = prompt;
    }

    /**
     * No actual args to the program; it's interactive for a fast-paced game!
     *
     * @param args None
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        runTool();
    }

    /**
     * Meet and bones of the tool's data gathering, separated off from main for eventual testing.
     * @throws Exception
     */
    static void runTool() throws Exception{
        while(true){
            String appMode = getInput(GREETING_USER_MSG + START_USER_PROMPT).toLowerCase();
            if(appMode.startsWith("c")){
                Integer[] dice = getRoll();
                ScoreCategory scoreCategory = getCategory();
                System.out.println( SCORE_USER_MSG + getScore(scoreCategory, dice));
            }
            else if(appMode.startsWith("s")){
                Integer[] dice = getRoll();
                ScoreCategory scoreCategory = getSuggestion(dice);
                Integer points = getScore(scoreCategory, dice);
                System.out.println(SHOULD_USE_USER_MSG + getSuggestion(dice) + ". " + SCORE_USER_MSG + points + ".");
                String full = getInput(ALL_CATEGORIES_USER_PROMPT).toLowerCase();
                if(full.startsWith("y")){
                    System.out.println(getAllScores(dice));
                }
            } else{
                System.out.println(APPMODE_USER_ERROR + TRY_AGAIN_PART);
            }
        }
    }

    /**
     * Centralize the prompt for the user's roll. As long as they enter 5 numbers, it doesn't matter what the input
     * looks like: "11111", "1...2..3..4.5", "1 and 2 and 3 and 4 and 5", you get the idea.
     *
     * @return The roll the user enters.
     */
    static Integer[] getRoll(){
        Integer[] dice = null;
        while(null == dice) {
            String diceString = getInput(ROLL_USER_PROMPT);
            try {
                char[] diceA = diceString.replaceAll("\\D", "").toCharArray();
                dice = new Integer[diceA.length];
                for (int i = 0; i < dice.length; i++) {
                    dice[i] = diceA[i] - '0';
                }
                checkDice(dice);
            } catch (Exception e) {
                dice = null;
                System.out.println(e.getMessage() + TRY_AGAIN_PART);
            }
        }
        return dice;
    }

    /**
     * Centralize the prompt for the user's Category. This can be the parenthetical number offered, or if the user is a
     * masochist, the full name (case doesn't matter)
     *
     * @return the ScoreCategory represented by the user's input.
     */
    static ScoreCategory getCategory(){
        ScoreCategory scoreCategory = null;
        while(scoreCategory == null) {
            String modeString = getInput(SCOREMODE_PROMPT).toUpperCase();
            try {
                Integer selection = Integer.parseInt(modeString);
                scoreCategory = ScoreCategory.values()[selection];
            } catch (Exception e) {
                // They either spelled out their selection, or it was invalid
                try {
                    scoreCategory = ScoreCategory.valueOf(modeString);
                } catch(IllegalArgumentException e2){
                    //handled with the If below.
                }
            }
            if (null == scoreCategory) {
                System.out.println(SCOREMODE_USER_ERROR + TRY_AGAIN_PART);
            }
        }
        return scoreCategory;
    }

    /**
     * Get the user's score, based on a provided category.
     *
     * @param scoreCategory The category to calculate the score for
     * @param dice The dice values
     * @return The calculated score
     * @throws Exception If category or dice were invalid.
     */
    static Integer getScore(ScoreCategory scoreCategory, Integer[] dice) throws Exception {
        checkDice(dice);
        if ( null == scoreCategory) {
            throw new Exception(INVALID_SCOREMODE);
        } else {
            switch (scoreCategory) {
                case ALLDIFFERENT:
                    return checkAllDifferent(dice);
                case ALLSAME:
                    return checkAllSame(dice);
                case CHANCE:
                    return addAll(null, dice);
                case FOUROFAKIND:
                    return checkFourOfAKind(dice);
                case FULLHOUSE:
                    return checkFullHouse(dice);
                case LARGESTRAIGHT:
                    return checkLargeStraight(dice);
                case SMALLSTRAIGHT:
                    return checkSmallStraight(dice);
                case THREEOFAKIND:
                    return checkThreeOfAKind(dice);
                default:
                    //All other modes are NUMERIC and incorporated into a catch-all
                    return addAll(scoreCategory.getValue(), dice);
            }
        }
    }

    /**
     * Cycle through all Categories and report the value the dice would have in each.
     *
     * @param dice The dice values
     * @return String of the format "CATEGORY: VALUE" with each Category on a separate line.
     * @throws Exception If the dice are invalid.
     */
    static String getAllScores(Integer[] dice) throws Exception {
        StringBuilder output = new StringBuilder();
        String delim = "";
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            output
                    .append(delim)
                    .append(scoreCategory)
                    .append(": ")
                    .append(getScore(scoreCategory, dice));
            delim = "\n";
        }
        return output.toString();
    }

    /**
     * This function would make a lot more sense if the game used ScoreCategory-elimination system, where once you assigned
     * points to a category you couldn't use it again. Otherwise, THREEOFAKIND, FOUROFAKIND, and CHANCE are the same,
     * LARGESTRAIGHT will never be better than ALLDIFFERENT, and you'd NEVER choose a NUMBER-based category. Just sayin'.
     *
     * @param dice The dice values
     * @return ScoreCategory of the best option the player has.
     * @throws Exception If the dice are invalid.
     */
    static ScoreCategory getSuggestion(Integer[] dice) throws Exception {
        //ALLSAME = ALLDIFFERENT = LARGESTRAIGHT = 40
        //SMALLSTRAIGHT = 30
        //FULLHOUSE = 25
        //Highest Chance is 88876 = 37
        checkDice(dice);
        int maxDups = maxInstances(dice);
        //It is impossible to score more than 40pts on a throw
        if(5 == maxDups){
            return ScoreCategory.ALLSAME;
        }
        if(1 == maxDups){
            return ScoreCategory.ALLDIFFERENT;
        }
        //Longest straight
        int longest = longestStraight(dice);
        if(5 == longest){
            return ScoreCategory.LARGESTRAIGHT;
        }
        //Calculate Chance, which is better than any of the NUMBER, THREE- or FOUR-OFAKIND
        int chance = addAll(null,dice);
        if(chance > 30){
            return ScoreCategory.CHANCE;
        }
        if(4 == longest){
            return ScoreCategory.SMALLSTRAIGHT;
        }
        if(chance > 25){
            return ScoreCategory.CHANCE;
        }
        //Small-straight is the next highest non-chance @ 30, but it can be beat by chance
        if(3 == maxDups && checkFullHouse(dice) != 0){
            return ScoreCategory.FULLHOUSE;
        }
        return ScoreCategory.CHANCE;
    }

    /**
     * Centralized dice validation.
     * @param dice The dice values
     * @throws Exception If there aren't exactly 5 dice, and if any are outisde of a 1-8 range.
     */
    private static void checkDice(Integer[] dice) throws Exception {
        if(null == dice){
            throw new Exception(INVALID_DICE_QTY_USER_ERROR);
        }
        if(5 != dice.length){
            throw new Exception(INVALID_DICE_QTY_USER_ERROR);
        }
        for(Integer value : dice){
            if( value == null || value < 1 || value > 8){
                throw new Exception(INVALID_DICE_USER_ERROR);
            }
        }
    }

    /**
     * If maxInstances is 1, all of the dice are different as there is no more than 1 instance of any number.
     *
     * @param dice The dice values
     * @return 40 if all dice values are different, 0 otherwise
     */
    private static Integer checkAllDifferent(Integer[] dice){
        if(1 == maxInstances(dice)){
            return 40;
        } else{
            return 0;
        }
    }

    /**
     * This COULD call maxInstances and check for 5, but given the likelihood, a fast-fail will save time.
     *
     * @param dice The dice values
     * @return 40 if all dice values are different, 0 otherwise
     */
    private static Integer checkAllSame(Integer[] dice){
        int needs = dice[0];
        for(Integer die : dice){
            if(needs != die){
                return 0;
            }
        }
        return 40;
    }

    /**
     * If maxInstances is 4 or higher, we achieved 4-of-a-kind
     *
     * @param dice The dice values
     * @return total of all dice values if 4 match, 0 otherwise
     */
    private static Integer checkFourOfAKind(Integer[] dice){
        if(maxInstances(dice) >= 4){
            return addAll(null,dice);
        } else{
            return 0;
        }
    }

    /**
     * If maxInstances is 3 or higher, we achieved 3-of-a-kind
     *
     * @param dice The dice values
     * @return total of all dice values if 3 match, 0 otherwise
     */
    private static Integer checkThreeOfAKind(Integer[] dice){
        if(maxInstances(dice) >= 3){
            return addAll(null,dice);
        } else{
            return 0;
        }
    }


    /**
     * Track the instances found for each value, and how many of each count is found. If, at the end, the there is 1
     * value with 2 dice, and one value with 3 dice, that's  full house.
     *
     * @param dice The dice values
     * @return 25 if there are 2 of one dice value, and 3 of another, 0 otherwise
     */
    private static Integer checkFullHouse(Integer[] dice){
        int[] found = new int[9];
        int[] common = new int[6];
        for(Integer die : dice){
            common[found[die]]--;
            common[++found[die]]++;
        }
        if(common[2] == 1 && common[3] == 1){
            return 25;
        } else{
            return 0;
        }
    }

    /**
     * If longestStraight is 5, then all 5 dice make a Large Straight
     *
     * @param dice The dice values
     * @return 40 if all dice are in consecutive order, 0 otherwise
     */
    private static Integer checkLargeStraight(Integer[] dice){
        if(longestStraight(dice) == 5){
            return 40;
        } else{
            return 0;
        }
    }

    /**
     * If longestStraight at least 4, then the dice make a Small Straight
     *
     * @param dice The dice values
     * @return 30 if at least 4 dice are in consecutive order, 0 otherwise
     */

    private static Integer checkSmallStraight(Integer[] dice){
        if(longestStraight(dice) >= 4){
            return 30;
        } else{
            return 0;
        }
    }

    /**
     * Sort the die to allow an O(n) operation here, then track the "active die" and its value, and look ahead to see
     * if the next dice in line have expected value. Track how far you can go with this check, reset the "active die"
     * when check fails, and restart. Highest consecutive distance is LongestStraight.
     *
     * @param dice The dice values
     l* @return Length of Longest chain of consecutive values.
     */
    private static Integer longestStraight(Integer[] dice){
        Arrays.sort(dice);
        int longestStraight = 0;
        int startDice = 0;
        while(startDice < 5) {
            int straightCount = 0;
            for (int i = dice[startDice]; startDice < 5 && i <= 8; i++) {
                if(dice[startDice] == i){
                    straightCount++;
                } else{
                    break;
                }
                startDice++;
            }
            longestStraight = Math.max(longestStraight,straightCount);
        }
        return longestStraight;
    }

    /**
     * Add up the value of all the dice matching "counts." If counts is null, add all values.
     *
     * @param counts if Null, add all dice values, otherwise add only matching.
     * @param dice The dice values
     * @return Sum as described in @param counts.
     */
    private static Integer addAll(Integer counts, Integer[] dice){
        int total = 0;
        for(Integer value : dice){
            if( null == counts || value.equals(counts)){
                total += value;
            }
        }
        return total;
    }

    /**
     * Iterate through the dice array, tracking the instances of each value. Track the max separately as you go.
     * @param dice The dice values
     * @return count of the occurrences of the highest occurring dice value.
     */
    private static Integer maxInstances(Integer[] dice){
        int[] found = new int[9];
        int max = 0;
        for(Integer die : dice){
            max = Math.max(max,(++found[die]));
        }
        return max;
    }

    /**
     * Display promt, wait for input from user
     * @param prompt What to display before waiting for input
     * @return Whatever the user enters.
     */
    private static String getInput(String prompt){
        System.out.println(prompt);
        String input = gather.next();
        checkExitTool(input);
        return input;
    }

    /**
     * If at any time the user enters a word staring with "q", we're going to assume it's to quit. Send them off nicely.
     * @param input the user's last input
     */
    private static void checkExitTool(String input){
        if(input.toLowerCase().startsWith("q")) {
            System.out.println(HappyHelper.PARTING_USER_MSG);
            System.exit(0);
        }
    }
}
