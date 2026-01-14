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
public class BoardHashtag {
	/*
	 * DROP TABLE IF EXISTS board_hashtag; CREATE TABLE IF NOT EXISTS board_hashtag
	 * ( board_id INT NOT NULL, hashtag_id INT NOT NULL, PRIMARY KEY (board_id,
	 * hashtag_id), CONSTRAINT fk_bh_board FOREIGN KEY (board_id) REFERENCES
	 * board(board_id) ON DELETE CASCADE, CONSTRAINT fk_bh_hashtag FOREIGN KEY
	 * (hashtag_id) REFERENCES hashtag(hashtag_id) ON DELETE CASCADE ) ENGINE=InnoDB
	 * DEFAULT CHARSET=utf8mb4;
	 */
	
	private int board_id;
	private int hashtag_id;
}
