package com.khodko.mongo.mongogridfs.service;

import com.khodko.mongo.mongogridfs.dto.LoadFileDto;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    private final GridFsTemplate template;

    @Autowired
    public FileService(GridFsTemplate template) {
        this.template = template;
    }

    public String addFile(MultipartFile upload) throws IOException {
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        ObjectId fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);

        return fileID.toString();
    }

    public LoadFileDto downloadFile(String id) throws IOException {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        LoadFileDto loadFileDto = new LoadFileDto();

        if (gridFSFile == null || gridFSFile.getMetadata() == null) {
            throw new IOException();
        }

        loadFileDto.setFilename(gridFSFile.getFilename());
        loadFileDto.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
        loadFileDto.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());
        loadFileDto.setFile((template.getResource(gridFSFile).getInputStream()).readAllBytes());

        return loadFileDto;
    }
}
