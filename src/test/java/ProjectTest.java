package test.java;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import main.java.memoranda.Project;
import main.java.memoranda.ProjectImpl;
import nu.xom.Attribute;
import nu.xom.Element;

public class ProjectTest {
  Project prj;
  
  @Before
  public void setup() {
    Element el = new Element("project");
    el.addAttribute(new Attribute("id", "1"));
    
    // US35 added Repo attribute. 
    el.addAttribute(new Attribute("Repo", ""));
    
    el.addAttribute(new Attribute("names", ""));
    el.addAttribute(new Attribute("gitnames", ""));
    prj = new ProjectImpl(el);
  }

  /**
   * Test to make sure addRepoName requires a non-empty string.
   */
  @Test (expected=java.lang.RuntimeException.class)
  public void GitRepoNameShouldNotAcceptEmptyString() {
    prj.addRepoName("");
  }
  
  /**
   * Tests to make sure that addRepoName can only accept repos that
   * are formatted correctly (ie. hello/world)
   */
  @Test (expected=java.lang.RuntimeException.class)
  public void GitRepoShouldFailIfRepoInvalidFormat() {
    prj.addRepoName("justowner");
  }
  
  /**
   * Tests to make sure that addRepoName can only accept repos that
   * are formatted correctly (ie. hello/world)
   */
  @Test (expected=java.lang.RuntimeException.class)
  public void GitRepoShouldFailIfRepoInvalidFormat2() {
    prj.addRepoName("/");
  }
  
  /**
   * Tests to make sure that addRepoName fails when the repo
   * doesn't exist on gitHub.
   */
  @Test (expected=java.lang.RuntimeException.class)
  public void GitRepoShouldFailWhenRepoDNE() {
    // We'll pick a repo name that definitely doesn't exist.
    String testRepo= "hello/world";
    prj.addRepoName(testRepo);
  }
  
  /**
   * Tests to make sure that addRepoName adds a valid repo when given
   */
  @Test
  public void GitRepoShouldAcceptGoodRepo() {
    // We'll pick a repo name that definitely doesn't exist.
    String testRepo= "regexhq/regex-username";
    prj.addRepoName(testRepo);
    assertTrue("name is updated", prj.getGitHubRepoName().equals(testRepo));
  }

}
