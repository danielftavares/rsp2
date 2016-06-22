package com.procergs.rsp.post.ed;

import java.util.Calendar;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import com.procergs.rsp.list.ed.ListED;
import com.procergs.rsp.opengraph.ed.OpenGraphED;
import com.procergs.rsp.user.ed.UserEd;

@Entity
@Table(name = "post")
public class PostED {

	public PostED() {
	}

	public PostED(Long idPost) {
		this.idPost = idPost;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_POST")
	private Long idPost;

	@Column(name = "TEXTO")
	private String texto;

	@Column(name = "DATA")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar data;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private UserEd userEd;

	@ManyToMany
	@JoinTable( name="LIST_POST",
			joinColumns=@JoinColumn(name="ID_POST"),
			inverseJoinColumns=@JoinColumn(name="ID_LIST"))
	private List<ListED> lists;

//	@OneToMany(mappedBy = "postED", fetch=FetchType.EAGER)
//	private List<ImageED> images;

//	@OneToMany(mappedBy = "postED", fetch=FetchType.EAGER)
//	private List<LikeED> likes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_POST_PARENT")
	@XmlTransient
	private PostED parent;

//	@OneToMany(mappedBy = "parent", fetch=FetchType.EAGER)
//	private List<PostED> replies;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_OPEN_GRAPH")
    private OpenGraphED openGraphED;


	public Long getIdPost() {
		return idPost;
	}

	public void setIdPost(Long idPost) {
		this.idPost = idPost;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public UserEd getUserEd() {
		return userEd;
	}

	public void setUserEd(UserEd userEd) {
		this.userEd = userEd;
	}

	@XmlTransient
	public PostED getParent() {
		return parent;
	}

	public void setParent(PostED parent) {
		this.parent = parent;
	}

	public List<ListED> getLists() {
		return lists;
	}

	public void setLists(List<ListED> lists) {
		this.lists = lists;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPost == null) ? 0 : idPost.hashCode());
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
		PostED other = (PostED) obj;
		if (idPost == null) {
			if (other.idPost != null)
				return false;
		} else if (!idPost.equals(other.idPost))
			return false;
		return true;
	}


    public OpenGraphED getOpenGraphED() {
        return openGraphED;
    }

    public void setOpenGraphED(OpenGraphED openGraphED) {
        this.openGraphED = openGraphED;
    }
}
