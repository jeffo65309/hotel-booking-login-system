package security;

public class securityUser {
	
	
	    private String username;
	    private String hashedPassword;
	    private String salt;          //Password will be stored as a hash

	    public securityUser(String username, String hashedPassword, String salt) {
	        this.username = username;
	        this.hashedPassword = hashedPassword;
	        this.salt = salt;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public String getHashedPassword() {
	        return hashedPassword;
	    }
	    
	    public String getSalt() {
	    	return salt;
	    
	    }
	}
