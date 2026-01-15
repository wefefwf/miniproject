package com.springbootsmini.app.domain;

import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardReply {

    private int replyId;        
    private int boardId;       
    private String writerId;    
    private String content;     
    private Timestamp createdAt; 
}
