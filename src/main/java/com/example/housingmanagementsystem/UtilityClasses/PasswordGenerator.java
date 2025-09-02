package com.example.housingmanagementsystem.UtilityClasses;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {
    private static final String UPPERCASE="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE="abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS="0123456789";
    private static final String CHARACTERS="!£$%^&*(){}@#~";
    private static final String ALL=UPPERCASE+LOWERCASE+DIGITS+CHARACTERS;
    private static final int passwordLength=10;
    private static final SecureRandom random=new SecureRandom();

    public static String generatePassword(){
        List<Character> passwordCharacters=new ArrayList<>();

        //Ensures atleast 1 element from each
        passwordCharacters.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        passwordCharacters.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        passwordCharacters.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordCharacters.add(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));

        //Randomly filling from ALL
        for (int i=4;i<passwordLength;i++){
            passwordCharacters.add(ALL.charAt(random.nextInt(ALL.length())));
        }

        //Mixing up of the characters
        Collections.shuffle(passwordCharacters,random);

        //Building a password String
        StringBuilder password=new StringBuilder(passwordLength);
        for (char c:passwordCharacters){
            password.append(c);
        }

        return password.toString();
    }
}
