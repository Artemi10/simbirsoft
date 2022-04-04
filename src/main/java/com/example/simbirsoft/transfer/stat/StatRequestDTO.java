package com.example.simbirsoft.transfer.stat;

import java.sql.Timestamp;

public record StatRequestDTO(String email, Timestamp from, Timestamp to) {
}
