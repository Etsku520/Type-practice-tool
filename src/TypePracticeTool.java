import java.util.Random;

public class TypePracticeTool {
    private final static String DEFAULT_CHARACTER_POOL = new String("qwertyuiopåasdfghjklöäzxcvbnmQWERTYUIOPÅASDFGHJKLÖÄZXCVBNM1234567890,.-;:_<>'*¨^´`\"!#¤%&/()=?\\~|§½@£$€{[]}+");
    private String currentCharacterPool;
    private String[] currentText;
    private int currentWord;
    private long startTime;
    private long endTime;
    private int wpm;
    private double cpm;
    private int errors;
    private int totalCharacters;
    private boolean errorMode;
    private Random rand;

    public TypePracticeTool() {
        this.currentCharacterPool = DEFAULT_CHARACTER_POOL;
        this.rand = new Random();
        this.errors = 0;
        this.errorMode = false;
    }

    public void resetCharacterPool() {
        currentCharacterPool = DEFAULT_CHARACTER_POOL;
    }

    public void startPractice(int minWLength, int maxWLenght, int textLength) {
        this.errors = 0;
        this.totalCharacters = 0;
        this.errorMode = false;
        createNewText(minWLength, maxWLenght, textLength);
        this.startTime = System.currentTimeMillis();
        
    }

    public boolean checkWord(String input) {
        boolean isStart = this.currentText[this.currentWord].startsWith(input);
        if (!isStart && !errorMode) {
            this.errors += 1;
        }
        this.errorMode = !isStart;

        boolean isEqual = this.currentText[this.currentWord].equals(input);

        return isEqual;
    }

    public boolean nextWord() {
        boolean textEnd = this.currentWord >= this.currentText.length-1;
        if (!textEnd) {
            this.currentWord = this.currentWord +1;
        } else {
            this.endTime = System.currentTimeMillis();
            this.wpm = (int) Math.round((this.currentText.length)/((this.endTime - this.startTime)/(1000.0*60)));
            this.cpm = (this.totalCharacters)/((this.endTime - this.startTime)/(1000.0*60));
        }

        return !textEnd;
    }

    public boolean previousWord() {
        boolean textStart = this.currentWord <= 0;
        if (!textStart) {
            this.currentWord = this.currentWord - 1;
        }

        return !textStart;
    }

    private void createNewText(int minWLength, int maxWLenght, int textLength) {
        String[] newText = new String[textLength];

        for (int i = 0; i < textLength; i++) {
            int wordLength = rand.nextInt(maxWLenght - minWLength) + minWLength;
            this.totalCharacters += wordLength;

            String newWord = this.createNewWord(wordLength);
            if (i != textLength-1) {
                newWord = newWord + " ";
                this.totalCharacters += 1;
            }
            newText[i] = newWord;
        }

        this.currentWord = 0;
        this.currentText = newText;
    }

    private String createNewWord(int wordLength) {
        int poolSize = this.currentCharacterPool.length();
        String currentWord = "";

        for (int i=0; i < wordLength; i++ ) {
            int randomIndex = this.rand.nextInt(poolSize);
            if (randomIndex < poolSize-1) {
                currentWord = currentWord + this.currentCharacterPool.substring(randomIndex, randomIndex+1);
            } else {
                currentWord = currentWord + this.currentCharacterPool.substring(randomIndex);
            }
        }

        return currentWord;
    }

    public double getAccuracy() {
        return 1.0*(this.totalCharacters-this.errors)/this.totalCharacters;
    }

    public int getCurrentWord() {
        return this.currentWord;
    }

    public String[] getCurrentText() {
        return this.currentText;
    }

    public int getWpm() {
        return wpm;
    }

    public String getCurrentCharacterPool() {
        return currentCharacterPool;
    }

    public void setCurrentCharacterPool(String currentCharacterPool) {
        this.currentCharacterPool = currentCharacterPool;
    }

    public double getCpm() {
        return cpm;
    }
}
