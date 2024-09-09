package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    @Autowired
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void createFile(MultipartFile fileUpload, int userid) throws IOException {
        File file = new File();
        try {
            file.setContenttype(fileUpload.getContentType());
            file.setFiledata(fileUpload.getBytes());
            file.setFilename(fileUpload.getOriginalFilename());
            file.setFilesize(Long.toString(fileUpload.getSize()));
            file.setUserid(userid);
        } catch (IOException exception) {
            throw exception;
        }
        fileMapper.storeFile(file);
    }

    public List<File> getUploadedFiles(Integer userid){
        return fileMapper.getAllFiles(userid);
    }

    public boolean isFileAvailable(String filename, Integer userid) {
        File file = fileMapper.getFile(userid, filename);
        return file == null;
    }

    public int deleteFile(int fileId) {
        return fileMapper.deleteFile(fileId);
    }

    public File getFileById(Integer fileId){
        return fileMapper.getFileById(fileId);
    }
}
