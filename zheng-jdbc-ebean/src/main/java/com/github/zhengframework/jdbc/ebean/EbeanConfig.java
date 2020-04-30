package com.github.zhengframework.jdbc.ebean;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EnumType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.ebean", supportGroup = true)
public class EbeanConfig implements ConfigurationDefine {

  private boolean enable = true;

  /**
   * The Database name. auto setting group name.
   */
  private String name;

  /**
   * Set to true to register this Database with the DB singleton.
   */
  private boolean register = true;

  /**
   * Set to true if this is the default/primary database.
   */
  private boolean defaultServer = true;

  /**
   * List of interesting classes such as entities, embedded, ScalarTypes, Listeners, Finders,
   * Controllers etc.
   */
  private List<Class<?>> classes = new ArrayList<>();

  /**
   * The packages that are searched for interesting classes. Only used when classes is empty/not
   * explicitly specified.
   */
  private List<String> packages = new ArrayList<>();

  /**
   * The database platform name. Used to imply a DatabasePlatform to use.
   */
  private String databasePlatformName;


  private EnumType defaultEnumType = EnumType.ORDINAL;

  private boolean ddlGenerate = true;

  private boolean ddlRun = true;

  private boolean ddlExtra = true;

  private boolean ddlCreateOnly;

  /**
   * The mappingLocations for searching xml mapping.
   */
  private List<String> mappingLocations = new ArrayList<>();

  /**
   * When true we do not need explicit GeneratedValue mapping.
   */
  private boolean idGeneratorAutomatic = true;

}