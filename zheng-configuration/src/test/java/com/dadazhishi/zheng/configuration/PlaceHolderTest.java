package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.dadazhishi.zheng.configuration.ex.UnresolvablePlaceholdersException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class PlaceHolderTest {

  @Test
  public void shouldReturnNullWhenNullIsProvided() {
    Map<String, String> props = new HashMap<>();
    PlaceHolder placeHolder = new PlaceHolder(new NamespaceConfiguration(props));
    assertNull(placeHolder.replace(null));
  }

  @Test
  public void shouldReplaceVariables() {
    Map<String, String> props = new HashMap<>();
    props.put("animal", "quick brown fox");
    props.put("target", "lazy dog");
    String templateString = "The ${animal} jumped over the ${target}.";
    PlaceHolder sub = new PlaceHolder(new NamespaceConfiguration(props));
    String resolvedString = sub.replace(templateString);
    assertEquals("The quick brown fox jumped over the lazy dog.", resolvedString);
  }

  @Test
  public void shouldReplaceVariablesHavingBackslashes() {
    Map<String, String> props = new HashMap<>();
    props.put("animal", "quick\\brown\\fox");
    props.put("target", "lazy\\dog");
    String templateString = "The\\${animal}\\jumped\\over\\the\\${target}.";
    PlaceHolder sub = new PlaceHolder(new NamespaceConfiguration(props));
    String resolvedString = sub.replace(templateString);
    assertEquals("The\\quick\\brown\\fox\\jumped\\over\\the\\lazy\\dog.", resolvedString);
  }

  @Test
  public void shouldReplaceVariablesWithBackSlashesAndShouldWorkWithRecursion() {
    Map<String, String> props = new HashMap<>();
    props.put("color", "bro\\wn");
    props.put("animal", "qui\\ck\\${color}\\fo\\x");
    props.put("target.attribute", "la\\zy");
    props.put("target.animal", "do\\g");
    props.put("target", "${target.attribute}\\${target.animal}");
    props.put("template", "The ${animal} jum\\ped over the ${target}.");
    props.put("wrapper", "\\foo\\${template}\\bar\\");
    props.put("wrapper2", "\\baz\\${wrapper}\\qux\\");
    PlaceHolder sub = new PlaceHolder(new NamespaceConfiguration(props));
    String resolvedString = sub.replace("${wrapper2}");
    assertEquals(
        "\\baz\\\\foo\\The qui\\ck\\bro\\wn\\fo\\x jum\\ped over the la\\zy\\do\\g.\\bar\\\\qux\\",
        resolvedString);
  }

  @Test
  public void testRecoursiveResolution() {
    Map<String, String> props = new HashMap<>();
    props.put("color", "brown");
    props.put("animal", "quick ${color} fox");
    props.put("target.attribute", "lazy");
    props.put("target.animal", "dog");
    props.put("target", "${target.attribute} ${target.animal}");
    props.put("template", "The ${animal} jumped over the ${target}.");
    String templateString = "${template}";
    PlaceHolder sub = new PlaceHolder(new NamespaceConfiguration(props));
    String resolvedString = sub.replace(templateString);
    assertEquals("The quick brown fox jumped over the lazy dog.", resolvedString);
  }

  @Test(expected = UnresolvablePlaceholdersException.class)
  public void testMissingPropertyIsReplacedWithEmptyString() {
    Map<String, String> props = new HashMap<>();

    props.put("foo", "fooValue");
    props.put("baz", "bazValue");

    String template = "Test: ${foo} ${bar} ${baz} :Test";
    new PlaceHolder(new NamespaceConfiguration(props)).replace(template);
  }

  @Test
  public void testParametrization() {
    Map<String, String> props = new HashMap<>();

    props.put("foo", "fooValue");
    props.put("baz", "bazValue");

    PlaceHolder sub = new PlaceHolder(new NamespaceConfiguration(props));
    assertEquals("foo1", sub.replace("foo%d", 1));
    assertEquals("baz", sub.replace("baz"));
    assertEquals("foo.1.sfx", sub.replace("foo.%d.%s", 1, "sfx"));
  }
}