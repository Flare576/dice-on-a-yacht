package com.flare576.doay;
import com.flare576.doay.model.ScoreCategory;

/**
 * What this tests:
 * Happy/Negative for getSuggestion, getScore, getAllScores, and getCategory
 *
 * What this does not test:
 * Implementation (the private calls behind the semi-interface), and program interaction.
 *
 * What's next:
 * If this were a real application, we'd move to a more testable UI and get actual interaction tests. We'd also start
 * using an actual test framework since we'd no longer be tied to a main[] and static execution style.
 * Created by Flare576 on 1/22/2016.
 */
public class HappyHelperTest extends HappyHelper {
    private static int passed = 0;
    private static int failed = 0;

    private static final DiceAndPoints[] safe = {
            new DiceAndPoints( new Integer[] {1,1,1,1,1}, ScoreCategory.ALLSAME, 40, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 40, 5, 0, 0, 0, 5),
            new DiceAndPoints( new Integer[] {1,2,3,4,1}, ScoreCategory.SMALLSTRAIGHT, 30, 11, 2, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 0),
            new DiceAndPoints( new Integer[] {1,2,3,4,5}, ScoreCategory.ALLDIFFERENT, 40, 15, 1, 2, 3, 4, 5, 0, 0, 0, 40, 0, 0, 0, 40, 30, 0),
            new DiceAndPoints( new Integer[] {1,2,3,5,6}, ScoreCategory.ALLDIFFERENT, 40, 17, 1, 2, 3, 0, 5, 6, 0, 0, 40, 0, 0, 0, 0, 0, 0),
            new DiceAndPoints( new Integer[] {1,1,1,2,2}, ScoreCategory.FULLHOUSE, 25, 7, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 0, 0, 7),
            new DiceAndPoints( new Integer[] {8,8,8,7,7}, ScoreCategory.CHANCE, 38, 38, 0, 0, 0, 0, 0, 0, 14, 24, 0, 0, 0, 25, 0, 0, 38),
            new DiceAndPoints( new Integer[] {1,1,2,2,3}, ScoreCategory.CHANCE, 9, 9, 2, 4, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            new DiceAndPoints( new Integer[] {6,6,6,4,4}, ScoreCategory.CHANCE, 26, 26, 0, 0, 0, 8, 0, 18, 0, 0, 0, 0, 0, 25, 0, 0, 26),
            new DiceAndPoints( new Integer[] {6,6,6,5,5}, ScoreCategory.CHANCE, 28, 28, 0, 0, 0, 0, 10, 18, 0, 0, 0, 0, 0, 25, 0, 0, 28),
            new DiceAndPoints( new Integer[] {2,2,3,4,5}, ScoreCategory.SMALLSTRAIGHT, 30, 16, 0, 4, 3, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 30, 0),
            new DiceAndPoints( new Integer[] {5,6,7,8,5}, ScoreCategory.CHANCE, 31, 31, 0, 0, 0, 0, 10, 6, 7, 8, 0, 0, 0, 0, 0, 30, 0),
            new DiceAndPoints( new Integer[] {7,7,7,7,7}, ScoreCategory.ALLSAME, 40, 35, 0, 0, 0, 0, 0, 0, 35, 0, 0, 40, 35, 0, 0, 0, 35),
            new DiceAndPoints( new Integer[] {8,7,6,5,4}, ScoreCategory.ALLDIFFERENT, 40, 30, 0, 0, 0, 4, 5, 6, 7, 8, 40, 0, 0, 0, 40, 30, 0)
    };
    
    private static final DiceAndPoints[] badDice = {
            //Dice error checks
            new DiceAndPoints( new Integer[] {1,2,3,4}, ScoreCategory.ALLDIFFERENT, INVALID_DICE_QTY_USER_ERROR),
            new DiceAndPoints( new Integer[] {1,2,3,4,4,1}, ScoreCategory.ALLDIFFERENT, INVALID_DICE_QTY_USER_ERROR),
            new DiceAndPoints( new Integer[] {}, ScoreCategory.ALLDIFFERENT, INVALID_DICE_QTY_USER_ERROR),
            new DiceAndPoints( new Integer[] {null,1,1,1,1}, ScoreCategory.ALLDIFFERENT, INVALID_DICE_USER_ERROR),
            new DiceAndPoints( new Integer[] {-1,1,1,1,1}, ScoreCategory.ALLDIFFERENT, INVALID_DICE_USER_ERROR),
            new DiceAndPoints( new Integer[] {9,1,1,1,1}, ScoreCategory.ALLDIFFERENT, INVALID_DICE_USER_ERROR),
            new DiceAndPoints( new Integer[] {-1,1,1,1}, ScoreCategory.ALLDIFFERENT, INVALID_DICE_QTY_USER_ERROR),
            new DiceAndPoints( null, ScoreCategory.ALLDIFFERENT, INVALID_DICE_QTY_USER_ERROR),
    };

    public static void main(String[] args) {
        testSuggestion(safe);
        testScore(safe);
        testTypes(safe);
        testAllScores(safe);

        testSuggestion(badDice);
        testScore(badDice);
        testTypes(badDice);
        testAllScores(badDice);

        //Since mode is an enum, about the only "wrong" thing you can pass in is null, and the only function that takes
        //a mode as an arg is getScore, so...
        testScore(new DiceAndPoints[] {new DiceAndPoints( new Integer[] {1,1,1,1,1}, null, INVALID_SCOREMODE)});

        System.out.println("Test results: " + passed +" Passed, "+failed+" Failed, "+(passed+failed)+" Total.");
        System.out.println("Please note that \"Errored\" Tests count as Failed for this report.");
    }

    private static void testSuggestion(DiceAndPoints[] diceAndPoints){
        ScoreCategory scoreCategory = null;
        for (DiceAndPoints dap : diceAndPoints) {
            try {
                scoreCategory = getSuggestion(dap.dice);
                if (!scoreCategory.equals(dap.scoreCategory)) {
                    failed++;
                    System.out.println(dap + " did not result in the correct suggestion");
                } else{
                    passed++;
                }
            } catch (Exception e) {
                handleException("testSuggestion @ "+ scoreCategory, dap, e);
            }
        }
    }

    private static void testScore(DiceAndPoints[] diceAndPoints){
        for (DiceAndPoints dap : diceAndPoints) {
            try {
                Integer points = getScore(dap.scoreCategory, dap.dice);
                if (!points.equals(dap.points)) {
                    failed++;
                    System.out.println(dap + " did not result in the correct points");
                } else{
                    passed++;
                }
            } catch (Exception e) {
                handleException("testScore", dap, e);
            }
        }
    }

    private static void testAllScores(DiceAndPoints[] diceAndPoints){
        ScoreCategory[] modes = ScoreCategory.values();
        for (DiceAndPoints dap : diceAndPoints) {
            StringBuilder expected = new StringBuilder();
            String delim = "";
            for (int i = 0; i < dap.scoreModePoints.length; i++) {
                expected
                        .append(delim)
                        .append(modes[i])
                        .append(": ")
                        .append(dap.scoreModePoints[i]);
                delim = "\n";
            }
            try {
                if (expected.toString().equals(getAllScores(dap.dice))) {
                    passed++;
                } else {
                    failed++;
                    System.out.println(dap + " did not result in the correct allScores string");
                }
            } catch (Exception e) {
                handleException("testAllScores", dap, e);
            }
        }
    }

    private static void testTypes(DiceAndPoints[] diceAndPoints){
        int dapPos = 0;
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            for (DiceAndPoints dap : diceAndPoints) {
                try {
                    if (getScore(scoreCategory, dap.dice).equals(dap.scoreModePoints[dapPos])) {
                        passed++;
                    } else {
                        System.out.println(dap + " FAILED for " + scoreCategory + ".");
                        failed++;
                    }
                } catch (Exception e) {
                    handleException("testTypes @ " + scoreCategory, dap, e);
                }
            }
            dapPos++;
        }
    }

    private static void handleException(String methodHandle, DiceAndPoints dap, Exception e){
        if (null == dap.expectedMessage) {
            failed++;
            System.out.println(dap + " caused Unexpected Error: ");
            e.printStackTrace();
        } else if (dap.expectedMessage.equals(e.getMessage())) {
            passed++;
        } else {
            failed++;
            System.out.println(dap + " caused incorrect error in " + methodHandle +".");
            System.out.println("Expected: " + dap.expectedMessage);
            System.out.println("But Have: " + e.getMessage());
        }
    }

    static class DiceAndPoints{
        Integer[] dice;
        Integer points;
        ScoreCategory scoreCategory;
        Integer[] scoreModePoints;
        String expectedMessage;

        DiceAndPoints(Integer[] dice, ScoreCategory scoreCategory, Integer points, Integer... scoreModePoints){
            this.dice = dice;
            this.points = points;
            this.scoreCategory = scoreCategory;
            this.scoreModePoints = scoreModePoints;
            this.expectedMessage = null;
        }

        DiceAndPoints(Integer[] dice, ScoreCategory scoreCategory, String expectedMessage){

            this(dice, scoreCategory,0,new Integer[ScoreCategory.values().length]);
            this.expectedMessage = expectedMessage;
        }

        public String toString(){
            String me = "[";
            String delim = "";
            if(null != dice) {
                for (Integer die : dice) {
                    me += delim + die;
                    delim = ",";
                }
            } else{
                me += "null";
            }
            return me+"]";
        }
    }
}
