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

/*
DROP TABLE IF EXISTS board_reply;
CREATE TABLE IF NOT EXISTS board_reply (
    reply_id INT AUTO_INCREMENT PRIMARY KEY,
    board_id INT NOT NULL,
    writer_id VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reply_board
        FOREIGN KEY (board_id) REFERENCES board(board_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_reply_writer
        FOREIGN KEY (writer_id) REFERENCES user(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
*/