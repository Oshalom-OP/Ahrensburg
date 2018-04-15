package main.java.memoranda.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Class to contain a commit to a GitHub repo
 * @author Nergal Givarkes, Jordan Wine
 * @Version 1.1
 *
 */
public class Commit {
	private String _sha, _authorName, _dateString, _message, _htmlUrl, _authorLogin;
	private int _additions, _deletions, _totalLoc;
	private int _numTests;	// US37, number of tests in a commit from parsing commit message. Nick Okerberg.
	private Date _date;
	
	/**
	 * An unlikely form of constructor to be used but helpful to include
	 * Defaults all attributes to null
	 */
  public Commit() {
    this.setSha(null);
    this.setAuthorName(null);
    this.setDateString(null);
    this.setDate(null);
    this.setMessage(null);
    this.setHtmlUrl(null);
    this.setAuthorLogin(null);
    this.setAdditions(0);
    this.setDeletions(0);
    this.setTotalLoc(0);
    this._numTests = 0;	// US37 default numTests is 0. 
    //System.out.println("Commit object built - default const");
  }
	
  /**
   * Initializes all attributes based on specified values
   * @param sha sha of commit
   * @param authorName Full name of author of commit
   * @param dateString The date of the commit as a string
   * @param message The message associated with the commit
   * @param htmlUrl The String representation of the HTML version of the URL
   * @param authorLogin The GitHub login name of the author
   */
	public Commit(String sha, String authorName, 
	    String dateString, String message, 
	    String htmlUrl, String authorLogin,
	    int add, int del, int totalLoc) {
    this.setSha(sha);
    this.setAuthorName(authorName);
    this.setDateString(dateString);
    this.setDate(parseDate(dateString));
    this.setMessage(message);
    this.setHtmlUrl(htmlUrl);
    this.setAuthorLogin(authorLogin);
    this.setAdditions(add);
    this.setDeletions(del);
    this.setTotalLoc(totalLoc);
    // US37 - Parse the message to get numTests. 
    this._numTests = getNumTestsFromMessage(message);
    //System.out.println("Commit object built - all-params const");
	}
	
	/**
	 * Constructor to accept a JSON object and parse out relevant info
	 * @param json JSON object in the form of a GitHub API Commit
	 */
	public Commit (JSONObject json) throws JSONException{
	  this._sha = json.getString("sha");
	  this._authorName = json.getJSONObject("commit").getJSONObject("author").getString("name");
	  this.setDateString(json.getJSONObject("commit").getJSONObject("author").getString("date"));
	  this.setDate(parseDate(_dateString));
	  this._message = json.getJSONObject("commit").getString("message");
	  this.setHtmlUrl(json.getString("html_url"));
	  this._numTests = getNumTestsFromMessage(_message);	// US37
    // Handle if author information is null
    try {
      this._authorLogin = json.getJSONObject("author").getString("login");
    } catch (JSONException ex) {
      this._authorLogin = "null";
    }
    try {
      JSONObject stats = json.getJSONObject("stats");
      this.setAdditions(stats.getInt("additions"));
      this.setDeletions(stats.getInt("deletions"));
      this.setTotalLoc(stats.getInt("total"));
    } catch (JSONException ex) {
      this.setAdditions(0);
      this.setDeletions(0);
      this.setTotalLoc(0);
      ex.printStackTrace();
    }
    //System.out.println("Commit object built - Json const");
	}
		
	
	public String getSha() {
		return _sha;
	}
	public void setSha(String sha) {
		this._sha = sha;
	}

  public String getAuthorName() {
    return _authorName;
  }
  public void setAuthorName(String authorName) {
    this._authorName = authorName;
  }
  public String getDateString() {
    return _dateString;
  }
  public void setDateString(String dateString) {
    this._dateString = dateString;
  }
  public Date getDate() {
    return _date;
  }
  public void setDate(Date date) {
    this._date = date;
  }
  public String getMessage() {
    return _message;
  }
  public void setMessage(String message) {
    this._message = message;
  }
  public String getHtmlUrl() {
    return _htmlUrl;
  }

  public void setHtmlUrl(String htmlUrl) {
    this._htmlUrl = htmlUrl;
  }
	public String getAuthorLogin() {
		return _authorLogin;
	}
	public void setAuthorLogin(String authorLogin) {
		this._authorLogin = authorLogin;
	}
	
	public int getAdditions() {
    return _additions;
  }

  public void setAdditions(int additions) {
    this._additions = additions;
  }

  public int getDeletions() {
    return _deletions;
  }

  public void setDeletions(int deletions) {
    this._deletions = deletions;
  }

  public int getTotalLoc() {
    return _totalLoc;
  }

  public void setTotalLoc(int totalLoc) {
    this._totalLoc = totalLoc;
  }
  
  // US37 getter for number of tests. 
  public int getNumTests() {
	  return _numTests;
  }
  
  /**
   * Parses a date string in ISO 8601 format and returns a Date object
   * @param sDate the ISO 8601 string to parse
   * @return the Date object representing the input string
   */
  private Date parseDate(String sDate) {
	  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    Date date= null;
    
	  try {
      date = sf.parse(sDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
	  return date;
	}
  
  /**
   * US37 Nick Okerberg
   * 
   * Extracts the number of tests from a specific commit message string. The 
   * message of the commit follows a standard defined in the Ahrensburg team
   * quality policy. The format is specified as follows: 
   * 
   * 	"JUNIT-XX#" where "XX" represents the number of tests included in this commit.
   * 
   * Returns an integer value representing the number of tests. 
   * 
   * @param msg The input String representing the commit message. 
   * @return The number of tests. 
   */
  private int getNumTestsFromMessage(String msg) {
	  // Initialize the result to 0. 
	  int result = 0;
	  
	  /*
	   * Parse the numbers out of the input string. 
	   * 	Format:  "JUNIT-XX#"
	   * This will parse the XX number out of the above example. 
	   * Find the first " - " and end at the " # " symbol. 
	   */
	  if (msg.contains("JUNIT-") && msg.contains("#") && (msg.indexOf("JUNIT-") < msg.indexOf("#")) ){
		  
		  String parsed = msg.substring(msg.indexOf("JUNIT-")+6, msg.indexOf("#"));
		  
		  // For debugging:
		  //System.out.println("[DEBUG] commit message: " + msg);
		  //System.out.println("   parsed: " + parsed);
		  //System.out.println("[DEBUG] parsed test value to return: " + Integer.parseInt(parsed));
		  
		  // Convert the string result representing the integer, to an int. 
		  result = Integer.parseInt(parsed);  
	  }
	  
	  return result;
  } // End of US37 method getNumTestsFromMessage 
  
} // End of class. 
