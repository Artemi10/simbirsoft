package com.example.simbirsoft.service.application;

import com.example.simbirsoft.transfer.application.AppRequestDTO;
import com.example.simbirsoft.transfer.application.AppResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface AppService {
    AppResponseDTO findUserApp(long appId, String email);
    List<AppResponseDTO> findUserApps(int page, String email);
    List<AppResponseDTO> findUserApps(String email);
    int getPageAmount(String email);
    void addUserApp(long userId, AppRequestDTO appDTO);
    void updateUserApp(long appId, AppRequestDTO appDTO, String email);
    void deleteUserApp(long appId, String email);
}
