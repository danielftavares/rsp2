package com.procergs.rsp.opengraph;

import com.procergs.rsp.opengraph.ed.OpenGraphED;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by daniel-tavares on 01/03/16.
 */
public class OpenGraphBd {
  EntityManager em;

  public OpenGraphBd(EntityManager em) {
    this.em = em;
  }

  public void insert(OpenGraphED openGraphED) {
    em.persist(openGraphED);
  }

  public OpenGraphED findByUrl(OpenGraphED openGraphED) {
      Query query = em.createQuery("SELECT o FROM OpenGraphED o WHERE o.url = :url");
      query.setParameter("url", openGraphED.getUrl());

      List<OpenGraphED> list = query.getResultList();

      if (list.isEmpty()){
          return  null;
      } else {
          return list.get(0);
      }
  }
}
