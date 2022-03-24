package com.example.simbirsoft.transfer.application;

import com.example.simbirsoft.entity.App;

import java.text.SimpleDateFormat;

public record AppResponseDTO(long id, String name, String creationTime) {
    private static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("dd MMMM yyyy HH:mm:s");

    public AppResponseDTO(App app) {
        this(app.getId(), app.getName(), DATE_FORMAT.format(app.getCreationTime()));
    }
}
