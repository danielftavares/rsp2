package com.procergs.rsp.profile.ed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "PROFILE_FIELD")
public class ProfileField {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID_PROFILE_FIELD")
	private Long idProfileField;
	
	@Column(name = "LABEL")
	private String label;
	
	@Column(name = "INDEX")
	private int index;
	
	@Column(name = "MULTILINE")
	private boolean multiline;

	public Long getIdProfileField() {
		return idProfileField;
	}

	public void setIdProfileField(Long idProfileField) {
		this.idProfileField = idProfileField;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isMultiline() {
		return multiline;
	}

	public void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idProfileField == null) ? 0 : idProfileField.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfileField other = (ProfileField) obj;
		if (idProfileField == null) {
			if (other.idProfileField != null)
				return false;
		} else if (!idProfileField.equals(other.idProfileField))
			return false;
		return true;
	}
	
}
