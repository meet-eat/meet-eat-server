package meet_eat.server.service;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordValueSupplierTest {

    @Test
    public void testConstructor() {
        // Test data
        int basicCharCount = 20;
        int specialCharCount = 3;
        int digitCount = 7;

        // Execution
        PasswordValueSupplier passwordSupplier = new PasswordValueSupplier(basicCharCount, specialCharCount, digitCount);

        // Assertions
        assertNotNull(passwordSupplier);
        assertEquals(basicCharCount, passwordSupplier.getBasicCharCount());
        assertEquals(specialCharCount, passwordSupplier.getSpecialCharCount());
        assertEquals(digitCount, passwordSupplier.getDigitCount());
    }

    @Test
    public void testLength() {
        // Test data
        int basicCharCount = 20;
        int specialCharCount = 3;
        int digitCount = 7;

        // Execution
        PasswordValueSupplier passwordSupplier = new PasswordValueSupplier(basicCharCount, specialCharCount, digitCount);

        // Assertions
        assertEquals(basicCharCount + specialCharCount + digitCount, passwordSupplier.get().length());
    }

    @Test
    public void testRandomness() {
        // Test data
        int basicCharCount = 20;
        int specialCharCount = 3;
        int digitCount = 7;

        // Execution
        PasswordValueSupplier passwordSupplier = new PasswordValueSupplier(basicCharCount, specialCharCount, digitCount);
        String passwordValueFst = passwordSupplier.get();
        String passwordValueSnd = passwordSupplier.get();

        // Assertions
        assertNotEquals(passwordValueFst, passwordValueSnd);
    }
}
