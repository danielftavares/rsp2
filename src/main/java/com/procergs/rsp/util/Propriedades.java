package com.procergs.rsp.util;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Enumeration;

import java.util.GregorianCalendar;

import java.util.HashMap;

import java.util.Iterator;

import java.util.Properties;

import java.util.Set;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;

/**
 * "copia" do prarqjava
 * Created by daniel-tavares on 01/07/16.
 */

public class Propriedades {

  protected static final Log logger = LogFactory.getLog(Propriedades.class);

  private static HashMap<String, Propriedades> hashSistemas = new HashMap<String, Propriedades>();

  String sistema;

  String ultimaAtualizacao;

  private Properties propriedades = new Properties();

  /**
   * Monta a tabela de propriedades.
   *
   * @throws IOException
   * @throws FileNotFoundException
   */

  private Propriedades(String sistema, String nomeArquivo) throws FileNotFoundException, IOException {

    this(sistema);

    buscaPropsArquivo(nomeArquivo);

    ultimaAtualizacao = System.getProperty(propSolicitarRefresh(sistema));

  }

  public Propriedades(String sistema) {

    logger.warn("Carregando as propriedades do sistema:" + sistema);

    this.sistema = sistema;

  }

  /**
   * Retorna uma inst?ncia da classe Propriedades lendo as
   * <p/>
   * propriedades da tabela do banco e do arquivo de propriedades do sistema
   * <p/>
   * informado. Exemplo:
   * <p/>
   * <p/>
   * <p/>
   * Propriedades.getInstance("IWE"); // L? da tabela do banco do
   * <p/>
   * sistema IWE e do arquivo de propriedades do sistema
   *
   * @param sistema Sistema que se quer obter as propriedades.
   * @return Propriedades
   */

  public static Propriedades getInstance(String sistema) {

    sistema = sistema.toUpperCase();

    String nomeArquivo = System.getProperty(sistema);

    try {

      Propriedades procergsPropriedades = (Propriedades) hashSistemas.get(sistema);

      // Verifica se ? a primeira vez que ? chamado o getInstance

      // ou se a inst?ncia expirou.

      if (procergsPropriedades == null || procergsPropriedades.isRefreshSolicitado()) {

        if (nomeArquivo != null) {

          procergsPropriedades = new Propriedades(sistema, nomeArquivo);

        } else {

          procergsPropriedades = new Propriedades(sistema);

        }

        hashSistemas.put(sistema, procergsPropriedades);

      }

      return procergsPropriedades;

    } catch (Exception e) {

      throw new RuntimeException("Problema ao instanciar ProcergsPropriedades. Sistema = " + sistema + " - Nome arquivo = " + nomeArquivo, e);

    }

  }

  /**
   * For?a a carga das propriedades novamente do banco ou do arquivo.
   *
   * @param sistema
   */

  public static void refresh(String sistema) {

    System.setProperty(propSolicitarRefresh(sistema), System.currentTimeMillis() + "");

    Propriedades.getInstance(sistema);

  }

  private static String propSolicitarRefresh(String sistema) {

    return "propriedades.refreshsolicitado." + sistema.toLowerCase();

  }

  private void buscaPropsArquivo(String nomeArquivo) throws FileNotFoundException, IOException {

    Properties propAux = new Properties();

    FileInputStream fis = new FileInputStream(nomeArquivo);

    propAux.load(fis);

    fis.close();

    Enumeration enumer = propAux.keys();

    while (enumer.hasMoreElements()) {

      String sectionEchave = (String) enumer.nextElement();

      String valor = (String) propAux.get(sectionEchave);

      propriedades.put(sectionEchave.toUpperCase(), valor.trim());

    }

  }

  /**
   * Retorna o valor de uma propriedade.<br />
   * <p/>
   * Caso n?o a propriedade n?o seja encontrada, retorna <i>null</i>. Ex:
   * <p/>
   * getValor("LOG", "SEVERIDADE");
   *
   * @param section Se??o da propridade
   * @param chave   Chave da propriedade
   * @return String contendo o valor da propriedade
   */

  public String getValor(String section, String chave) {

    String jvmprop = getJvmProp(sistema, montaChave(section, chave));

    if (jvmprop != null) {

      return jvmprop;

    }

    try {

      return (String) propriedades.get(montaChave(section, chave));

    } catch (Exception e) {

      return null;

    }

  }

  /**
   * M?todo alternativo para obter um valor passando a se??o e a chave
   * <p/>
   * separados por "." <br>
   * <p/>
   * Caso n?o a propriedade n?o seja encontrada, retorna <i>null</i>. <br>
   * <p/>
   * Exemplo: getValor("LOG.SEVERIDADE")
   *
   * @param secaoPontoChave
   * @return
   */

  public String getValor(String secaoPontoChave) {

    if (secaoPontoChave == null) {

      return null;

    }

    String jvmprop = getJvmProp(sistema, secaoPontoChave);

    if (jvmprop != null) {

      return jvmprop;

    }

    try {

      String conteudo = (String) propriedades.get(secaoPontoChave.toUpperCase());

      return conteudo;

    } catch (Exception e) {

      return null;

    }

  }

  /**
   * Retorna uma propriedade cujo o valor ? booleano (true ou false).
   * <p/>
   * Se a propriedade n?o for encontrada retorna o valor padrao.
   *
   * @param sectionchave
   * @param padrao
   * @return
   */

  public boolean getBoolean(String sectionchave, boolean padrao) {

    String jvmprop = getJvmProp(sistema, sectionchave);

    if (jvmprop != null) {

      return jvmprop.equalsIgnoreCase("true");

    }

    try {

      String str = (String) this.getValor(sectionchave);

      if (str == null) {

        return padrao;

      }

      if (str.equalsIgnoreCase("true")) {

        return true;

      }

      if (str.equalsIgnoreCase("false")) {

        return false;

      }

      return padrao;

    } catch (Exception e) {

      return padrao;

    }

  }

  /**
   * Retorna um conjutos todas as chaves do procergs propriedades no formato
   * <p/>
   * SECAO.CHAVE
   *
   * @return
   */

  public Set getChaves() {

    return propriedades.keySet();

  }

  /**
   * Imprime as Propriedades do objeto na console do sistema.
   */

  public void listaPropConsole() {

    System.out.println("==== Propriedades do Sistema " + sistema + " ====");

    Iterator iter = getChaves().iterator();

    while (iter.hasNext()) {

      String sectionEchave = (String) iter.next();

      String valor = this.getValor(sectionEchave);

      System.out.println(sectionEchave + " = " + valor);

    }

  }

  private String montaChave(String section, String chave) {

    return section.toUpperCase() + "." + chave.toUpperCase();

  }

  private boolean isRefreshSolicitado() {

    String strFazerRefresh = System.getProperty(propSolicitarRefresh(sistema));

    if (strFazerRefresh == null) {

      return false;

    }

    return !strFazerRefresh.equals(ultimaAtualizacao);

  }

  private String getJvmProp(String sistema, String prop) {

    if (sistema == null || prop == null) {

      return null;

    }

    StringBuffer key = new StringBuffer(80);

    key.append("procergs.").append(sistema.toLowerCase());

    key.append(".").append(prop.toLowerCase());

    return System.getProperty(key.toString());

  }

}
