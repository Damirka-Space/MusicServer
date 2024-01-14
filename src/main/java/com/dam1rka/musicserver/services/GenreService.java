package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.GenreEntity;
import com.dam1rka.musicserver.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public List<GenreEntity> listGenres() {
        return genreRepository.findAll();
    }

}
