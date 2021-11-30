package com.khodko.mongo.mongogridfs.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    GridFsTemplate template;

    @InjectMocks
    FileService fileService;

    @Test
    void addFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "data",
                "filename.txt",
                "text/plain",
                "some text".getBytes()
        );

        ObjectId objectId = new ObjectId("507f1f77bcf86cd799439011");
        when(template.store(any(), any(), any(), any())).thenReturn(objectId);

        String id = fileService.addFile(file);
        assertEquals(objectId.toString(), id);
    }

    @Test
    void downloadFile() {
        String id = "507f1f77bcf86cd799439011";

    }
}