package main.java.memoranda.util;

import org.json.JSONException;
import org.json.JSONObject;

public class Contributor {
  private String _login, _avatar_url, _name;
	
	public Contributor(String login, String avatar_url, String name) {
		
		this._login = login;
		this._avatar_url = avatar_url;
    this._name = name;
	}
  
  /**
   * Constructor to accept a JSON object and parse out relevant info
   * @param json JSON object in the form of a GitHub API User
   */
  public Contributor (JSONObject json) throws JSONException{
    this._login = json.getString("login");
    this._avatar_url = json.getString("avatar_url");
    try {
      this._name = json.getString("name");
    } catch (JSONException ex) {
      this._name = "null";
    }
  }
  
	public String getLogin() {
		return _login;
	}
	public void setLogin(String login) {
		this._login = login;
	}
	public String getAvatar_url() {
		return _avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this._avatar_url = avatar_url;
	}
	public String getName() {
		return _name;
	}
	public void setContributions(String name) {
		this._name = name;
	}
	
}
