package com.example.simbirsoft.service.application;

import com.example.simbirsoft.repository.AppRepository;
import com.example.simbirsoft.transfer.application.AppRequestDTO;
import com.example.simbirsoft.transfer.application.AppResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService {
    private final AppRepository appRepository;

    @Override
    public AppResponseDTO findUserApp(long appId, String email) {
        return null;
    }

    @Override
    public List<AppResponseDTO> findUserApps(int page, String email) {
        return null;
    }

    @Override
    public List<AppResponseDTO> findUserApps(String email) {
        return null;
    }

    @Override
    public int getPageAmount(String email) {
        return 0;
    }

    @Override
    public void addUserApp(long userId, AppRequestDTO noteDTO) {

    }

    @Override
    public void updateUserApp(long appId, AppRequestDTO noteDTO, String email) {

    }

    @Override
    public void deleteUserApp(long appId, String email) {

    }
}
