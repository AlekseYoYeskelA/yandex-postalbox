package com.example.yandex_interview.postalbox;

import com.example.yandex_interview.postalbox.repo.OrderDB;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CodeManager {
    //Список активных кодов. Делать его общим на все постаматы или каждому постамату свой инстанс?
    private final Set<Integer> activeCodes;
    private static CodeManager instance;

    public static CodeManager getInstance() {
        if (instance == null) {
            instance = new CodeManager();
        }
        return instance;
    }

    private CodeManager() {
        activeCodes = new HashSet<>();
    }

    public int generate() {
        Random random = new Random();
        int attempts = 0;
        int maxAttempts = 100;
        int code;
        do {
            code = random.nextInt(100000, 1000000);
            attempts++;
            if (attempts > maxAttempts) {
                throw new IllegalStateException("Попытки сгенерировать уникальный код закончились");
            }
        } while (activeCodes.contains(code));
        activeCodes.add(code);
        return code;
    }

    public void setInactive(int code) {
        activeCodes.remove(code);
    }

    public Set<Integer> getActiveCodes() {
        return activeCodes;
    }
}
