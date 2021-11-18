package com.khodko.mongo.mongogridfs.service;

import com.khodko.mongo.mongogridfs.dto.LoadFileDto;
import com.khodko.mongo.mongogridfs.exception.MyFileNotFoundException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@Service
public class FileService {

    private final GridFsTemplate template;
    private static final String CAN_NOT_READ_FILE_MSG = "Can not read file";

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

    public LoadFileDto downloadFile(String id) {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        LoadFileDto loadFileDto = new LoadFileDto();

        if (gridFSFile == null || gridFSFile.getMetadata() == null) {
            throw new MyFileNotFoundException("File not found");
        }

        loadFileDto.setFilename(gridFSFile.getFilename());
        loadFileDto.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
        loadFileDto.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());
        try {
            loadFileDto.setFile((template.getResource(gridFSFile).getInputStream()).readAllBytes());
        } catch (IOException e) {
            log.error(CAN_NOT_READ_FILE_MSG);
            throw new IllegalStateException(CAN_NOT_READ_FILE_MSG);
        }

        return loadFileDto;
    }
}
