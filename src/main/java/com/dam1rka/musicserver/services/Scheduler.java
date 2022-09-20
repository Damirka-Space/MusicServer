package com.dam1rka.musicserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private PlaylistService playlistService;

    @Autowired
    public Scheduler(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    // Run every day
    @Scheduled(cron = "0 0 0 * * *")
    public void updatePlaylistOfDay() {
        playlistService.updatePlaylistOfDay();

        playlistService.updateAllInOnePlaylist();
    }

    // Run every week
    @Scheduled(cron = "0 0 0 * * 0")
    public void updatePlaylistOfWeek() {
        playlistService.updatePlaylistOfWeek();
    }
}
