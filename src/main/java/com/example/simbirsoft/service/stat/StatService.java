package com.example.simbirsoft.service.stat;

import com.example.simbirsoft.transfer.stat.StatRequestDTO;
import com.example.simbirsoft.transfer.stat.StatResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatService {
    List<StatResponseDTO> createStats(long appId, StatRequestDTO statistics);
    List<StatResponseDTO> createStats(long appId, String email);
}
