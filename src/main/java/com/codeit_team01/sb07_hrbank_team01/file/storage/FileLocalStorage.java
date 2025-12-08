package com.codeit_team01.sb07_hrbank_team01.file.storage;

import com.codeit_team01.sb07_hrbank_team01.file.dto.FileResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Path;

public interface FileLocalStorage {

    void put(Long id, byte[] bytes);

    InputStream get(Long id);

    ResponseEntity<Resource> download(FileResponseDto fileResponseDto);

    Writer getWriter(Long id) throws IOException;

    long size(Long id) throws IOException;
}