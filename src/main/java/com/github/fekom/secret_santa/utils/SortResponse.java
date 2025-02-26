package com.github.fekom.secret_santa.utils;

import java.util.HashMap;

public record SortResponse(Long id, String name, HashMap<String, String> draw) {
    
}
