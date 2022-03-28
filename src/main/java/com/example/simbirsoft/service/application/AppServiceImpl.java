package com.example.simbirsoft.service.application;

import com.example.simbirsoft.entity.App;
import com.example.simbirsoft.entity.user.User;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.repository.AppRepository;
import com.example.simbirsoft.transfer.application.AppRequestDTO;
import com.example.simbirsoft.transfer.application.AppResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService {
    private static final int PAGE_SIZE = 3;
    private final AppRepository appRepository;

    @Override
    public AppResponseDTO findUserApp(long appId, String email) {
        return appRepository.findUserAppById(appId, email)
                .map(AppResponseDTO::new)
                .orElseThrow(() -> new EntityException("Приложения не существует"));
    }

    @Override
    public List<AppResponseDTO> findUserApps(int page, String email) {
        if (page > 0){
            var pageable = PageRequest.of(page - 1, PAGE_SIZE);
            return appRepository
                    .findAllByUserEmail(email, pageable).stream()
                    .map(AppResponseDTO::new)
                    .toList();
        }
        else throw new EntityException("Приложений не существует");
    }

    @Override
    public List<AppResponseDTO> findUserApps(String email) {
        return findUserApps(1, email);
    }

    @Override
    public int getPageAmount(String email) {
        var noteAmount = appRepository.getUserAppsAmount(email);
        if (noteAmount > 0 && noteAmount % PAGE_SIZE == 0) {
            return noteAmount / PAGE_SIZE;
        }
        else {
            return noteAmount / PAGE_SIZE + 1;
        }
    }

    @Override
    public void addUserApp(long userId, AppRequestDTO appDTO) {
        appDTO.check();
        var user = User.builder()
                .id(userId)
                .build();
        var currentTime = new Timestamp(new Date().getTime());
        var note = App.builder()
                .name(appDTO.name())
                .creationTime(currentTime)
                .user(user)
                .events(new ArrayList<>())
                .build();
        appRepository.save(note);
    }

    @Override
    public void updateUserApp(long appId, AppRequestDTO appDTO, String email) {
        appDTO.check();
        var app = appRepository.findUserAppById(appId, email)
                .orElseThrow(() -> new EntityException("Приложения не существует"));
        app.setName(appDTO.name());
        appRepository.save(app);
    }

    @Override
    public void deleteUserApp(long appId, String email) {
        appRepository.deleteByIdAndUserEmail(appId, email);
    }

    @Override
    public boolean isUserApp(long appId, String email) {
        return appRepository.findUserAppById(appId, email).isPresent();
    }
}
