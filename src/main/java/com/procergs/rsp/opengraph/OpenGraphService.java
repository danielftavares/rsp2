package com.procergs.rsp.opengraph;

import com.procergs.rsp.opengraph.ed.OpenGraphED;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by daniel-tavares on 01/03/16.
 */
@Stateless
public class OpenGraphService {

  @PersistenceContext(unitName = "RSP_PU")
  EntityManager em;

  OpenGraphBd openGraphBd;

  @PostConstruct
  public void init() {
    openGraphBd = new OpenGraphBd(em);
  }

  public void insert(OpenGraphED openGraphED) {
    openGraphBd.insert(openGraphED);
  }

  public OpenGraphED findByUrl(OpenGraphED openGraphED) {
    return openGraphBd.findByUrl(openGraphED);
  }
}
