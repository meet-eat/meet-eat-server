package meet_eat.server.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PasswordValueSupplier implements Supplier<String> {

    private static final char[] SPECIAL_CHARACTERS = "!#$%&*_+,-./:;'<=>?@^|~(){}".toCharArray();
    private static final char[] BASIC_CHARACTERS = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ".toCharArray();
    private static final char[] DIGITS = "1234567890".toCharArray();

    private int basicCharCount;
    private int specialCharCount;
    private int digitCount;

    public PasswordValueSupplier(int basicCharCount, int specialCharCount, int digitCount) {
        this.basicCharCount = basicCharCount;
        this.specialCharCount = specialCharCount;
        this.digitCount = digitCount;
    }

    @Override
    public String get() {
        SecureRandom random = new SecureRandom();
        List<Character> characters = new ArrayList<>();

        // Generate random special characters.
        for (int i = 0; i < specialCharCount; i++) {
            int randomIndex = random.nextInt(SPECIAL_CHARACTERS.length);
            characters.add(SPECIAL_CHARACTERS[randomIndex]);
        }

        // Generate random basic characters.
        for (int i = 0; i < basicCharCount; i++) {
            int randomIndex = random.nextInt(BASIC_CHARACTERS.length);
            characters.add(BASIC_CHARACTERS[randomIndex]);
        }

        // Generate random digits.
        for (int i = 0; i < digitCount; i++) {
            int randomIndex = random.nextInt(DIGITS.length);
            characters.add(DIGITS[randomIndex]);
        }

        // Shuffle the collection, join and return it.
        Collections.shuffle(characters, random);
        return characters.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public int getBasicCharCount() {
        return basicCharCount;
    }

    public int getSpecialCharCount() {
        return specialCharCount;
    }

    public int getDigitCount() {
        return digitCount;
    }

    public void setBasicCharCount(int basicCharCount) {
        this.basicCharCount = basicCharCount;
    }

    public void setSpecialCharCount(int specialCharCount) {
        this.specialCharCount = specialCharCount;
    }

    public void setDigitCount(int digitCount) {
        this.digitCount = digitCount;
    }
}
