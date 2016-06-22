package com.procergs.rsp.list.ed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LIST")
public class ListED {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID_LIST")
	private Long idList;
	
	@Column(name = "NAME")
	private String name;

	public ListED() {
	}
	
	public ListED(Long idList) {
		this.idList = idList;
	}

	public ListED(String name) {
		this.name = name;
	}

	public Long getIdList() {
		return idList;
	}

	public void setIdList(Long idList) {
		this.idList = idList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
