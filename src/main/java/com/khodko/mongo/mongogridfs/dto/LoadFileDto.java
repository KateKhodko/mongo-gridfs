package com.khodko.mongo.mongogridfs.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoadFileDto {
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
}
