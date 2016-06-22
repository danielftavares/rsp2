package com.procergs.rsp.list.ed;

import javax.persistence.*;

@Entity
@Table(name = "RSP_LIST")
public class ListED {
	
	@Id
	@SequenceGenerator(name = "ID_LIST_SEQ", sequenceName = "ID_LIST_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_LIST_SEQ")
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
