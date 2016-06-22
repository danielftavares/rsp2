package com.procergs.rsp.opengraph.ed;

import javax.persistence.*;

/**
 * Created by daniel-tavares on 29/02/16.
 */
@Entity
@Table(name = "OPEN_GRAPH")
public class OpenGraphED {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Column(name = "ID_OPEN_GRAPH")
  private Long idOpenGraph;

  @Column(name = "URL")
  private String url;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "IMAGE")
  private String image;

  public Long getIdOpenGraph() {
    return idOpenGraph;
  }

  public void setIdOpenGraph(Long idOpenGraph) {
    this.idOpenGraph = idOpenGraph;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
