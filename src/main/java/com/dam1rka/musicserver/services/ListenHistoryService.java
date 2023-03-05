package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.ListenHistoryDto;
import com.dam1rka.musicserver.entities.ListenHistoryEntity;
import com.dam1rka.musicserver.entities.TrackEntity;
import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.ListenHistoryRepository;
import com.dam1rka.musicserver.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ListenHistoryService {

    private final ListenHistoryRepository listenHistoryRepository;
    private final TrackRepository trackRepository;

    @Autowired
    public ListenHistoryService(ListenHistoryRepository listenHistoryRepository, TrackRepository trackRepository) {
        this.listenHistoryRepository = listenHistoryRepository;
        this.trackRepository = trackRepository;
    }

    public void saveToHistory(UserEntity user, long trackId) {
        TrackEntity track = trackRepository.findById(trackId).orElse(null);

        if(Objects.nonNull(track)) {
            ListenHistoryEntity listenHistory = new ListenHistoryEntity();
            listenHistory.setUser(user);
            listenHistory.setListened(new Date());
            listenHistory.setTrack(track);

            listenHistoryRepository.save(listenHistory);
        }
    }

    public List<ListenHistoryDto> getHistory(UserEntity user) {
        List<ListenHistoryEntity> historyEntities = listenHistoryRepository.findAllByUserOrderByListenedDesc(user);

        List<ListenHistoryDto> historyDtos = new LinkedList<>();

        historyEntities.forEach(v -> {
            ListenHistoryDto historyDto = new ListenHistoryDto();
            historyDto.setListened(v.getListened());
            historyDto.setTrackName(v.getTrack().getTitle());
            historyDtos.add(historyDto);
        });

        return historyDtos;
    }

}
