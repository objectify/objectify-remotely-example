package com.googlecode.objectify.remotely.example;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Data;

/**
 */
@Entity
@Data
public class Thing {
	@Id
	private Long id;

	private String name;
}
