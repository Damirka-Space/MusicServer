package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.ListenHistoryDto;
import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.AlbumListenHistoryRepository;
import com.dam1rka.musicserver.repositories.AlbumRepository;
import com.dam1rka.musicserver.repositories.ListenHistoryRepository;
import com.dam1rka.musicserver.repositories.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ListenHistoryService {

    private final AlbumListenHistoryRepository albumListenHistoryRepository;
    private final ListenHistoryRepository listenHistoryRepository;
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;

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

    public void saveAlbumToHistory(UserEntity user, long albumId) {
        AlbumEntity album = albumRepository.findById(albumId).orElse(null);

        if(Objects.nonNull(album)) {
            AlbumListenHistoryEntity listenHistory = new AlbumListenHistoryEntity();
            listenHistory.setUser(user);
            listenHistory.setListened(new Date());
            listenHistory.setAlbum(album);

            albumListenHistoryRepository.save(listenHistory);
        }
    }

    public List<AlbumEntity> getRecentlyListenedAlbums(UserEntity user) {
        List<AlbumListenHistoryEntity> historyEntities = albumListenHistoryRepository.findAllByUserOrderByListenedDesc(user, PageRequest.of(0, 10));
        List<AlbumEntity> albums = new LinkedList<>();

        historyEntities.stream().map(AlbumListenHistoryEntity::getAlbum).forEach((album -> {
            if(!albums.contains(album))
                albums.add(album);
        }));

        return albums;
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
