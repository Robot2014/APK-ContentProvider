package com.stb.config;
import android.net.Uri; 
public class StbConfig {
	 
    public static final String DBNAME =  "stbconfig"; 
    public static final String TNAME1 =  "stbconfig";
    public static final String TNAME2 =  "authentication";
    public static final int VERSION = 3;
     
	public static final String _ID = "_id";

	public static final String NAME = "name";

	public static final String VALUE = "value";
	
	public static final String  ATTRIBUTE  = "attribute" ;

	public static final String AUTHORITY  = "stbconfig";
     
    public static final int STBCONFIG_TYPE  = 1;
    public static final int STBCONFIG_ITEM  = 2;  
    public static final int ATTRIBUTE_TYPE =  3;
    public static final int ATTRIBUTE_ITEM =  4;
    
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.stbconfig.login";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.stbconfig.login";
    
    /* content URI  */
    public static final Uri CONTENT_STBCONFIG_URI = Uri.parse("content://" + AUTHORITY + "/stbconfig");  
    public static final Uri CONTENT_AUTHENTICATION_URI = Uri.parse("content://" + AUTHORITY + "/authentication"); 
     
}