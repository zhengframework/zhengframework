
package com.github.zhengframework.validator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class Country {

  @NotNull(groups = {Update.class})
  private Long id;

  @NotNull(groups = {Insert.class})
  private String name;

  @NotNull(groups = {Insert.class})
  @Size(max = 2, groups = {Insert.class, Update.class})
  private String iso2Code;

  @NotNull(groups = {Insert.class})
  @Size(max = 3, groups = {Insert.class, Update.class})
  private String iso3Code;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIso2Code() {
    return iso2Code;
  }

  public void setIso2Code(String iso2Code) {
    this.iso2Code = iso2Code;
  }

  public String getIso3Code() {
    return iso3Code;
  }

  public void setIso3Code(String iso3Code) {
    this.iso3Code = iso3Code;
  }

}