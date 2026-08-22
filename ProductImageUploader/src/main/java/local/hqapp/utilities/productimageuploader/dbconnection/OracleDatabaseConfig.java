package local.hqapp.utilities.productimageuploader.dbconnection;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OracleDatabaseConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	private String databaseName; 
	private String portNo;
	private String user;
	private String password;
	private String serverName;
	
	private String dsk; // databaseName key
	private String ds; // databaseName salt
	private String psk; // portNo key
	private String ps; // portNo salt
	private String usk; // user key
	private String us; // user salt
	private String pssk; // password key
	private String psss; // password salt
	private String snk; // serverName key
	private String sns; // serverName salt
	
	@XmlElement
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	@XmlElement
	public String getPortNo() {
		return portNo;
	}
	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}
	
	@XmlElement
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	@XmlElement
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@XmlElement
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	@XmlElement
	public String getDsk() {
		return dsk;
	}
	public void setDsk(String dsk) {
		this.dsk = dsk;
	}
	
	@XmlElement
	public String getDs() {
		return ds;
	}
	public void setDs(String ds) {
		this.ds = ds;
	}
	
	@XmlElement
	public String getPsk() {
		return psk;
	}
	public void setPsk(String psk) {
		this.psk = psk;
	}
	
	@XmlElement
	public String getPs() {
		return ps;
	}
	public void setPs(String ps) {
		this.ps = ps;
	}
	
	@XmlElement
	public String getUsk() {
		return usk;
	}
	public void setUsk(String usk) {
		this.usk = usk;
	}
	
	@XmlElement
	public String getUs() {
		return us;
	}
	public void setUs(String us) {
		this.us = us;
	}
	
	@XmlElement
	public String getPssk() {
		return pssk;
	}
	public void setPssk(String pssk) {
		this.pssk = pssk;
	}
	
	@XmlElement
	public String getPsss() {
		return psss;
	}
	public void setPsss(String psss) {
		this.psss = psss;
	}
	
	@XmlElement
	public String getSnk() {
		return snk;
	}
	public void setSnk(String snk) {
		this.snk = snk;
	}
	
	@XmlElement
	public String getSns() {
		return sns;
	}
	public void setSns(String sns) {
		this.sns = sns;
	}
}