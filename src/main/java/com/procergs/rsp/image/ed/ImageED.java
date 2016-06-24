package com.procergs.rsp.image.ed;

import java.util.Calendar;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.procergs.rsp.post.ed.PostED;

@Entity
@Table(name = "RSP_IMAGE")
@XmlRootElement
public class ImageED {

	@Id
	@SequenceGenerator(name = "ID_RSP_IMAGE_SEQ", sequenceName = "ID_RSP_IMAGE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_RSP_IMAGE_SEQ")
	@Column(name = "ID_IMAGE")
	private Long idImage;

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "IMAGE")
	private byte[] image;

	@Column(name = "DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;
	
	@Column(name = "TYPE")
	private String type;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_POST")
	@XmlTransient
	private PostED postED;
	

	public Long getIdImage() {
		return idImage;
	}

	public void setIdImage(Long idImage) {
		this.idImage = idImage;
	}

	@XmlTransient
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	@XmlTransient
	public PostED getPostED() {
		return postED;
	}

	public void setPostED(PostED postED) {
		this.postED = postED;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idImage == null) ? 0 : idImage.hashCode());
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
		ImageED other = (ImageED) obj;
		if (idImage == null) {
			if (other.idImage != null)
				return false;
		} else if (!idImage.equals(other.idImage))
			return false;
		return true;
	}
	
	

}
