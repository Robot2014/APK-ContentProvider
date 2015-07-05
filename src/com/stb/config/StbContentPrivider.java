package com.stb.config;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.util.Log;
import android.text.TextUtils;

public class StbContentPrivider extends ContentProvider {
	
	public static final String TAG = "StbContentPrivider";
    DBlite StbConfigDb;
    SQLiteDatabase db ;
 

    private static final UriMatcher sMatcher;
    static{
            sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            sMatcher.addURI(StbConfig.AUTHORITY, "stbconfig",StbConfig.STBCONFIG_TYPE);
            sMatcher.addURI(StbConfig.AUTHORITY, "stbconfig"+"/#", StbConfig.STBCONFIG_ITEM);
            sMatcher.addURI(StbConfig.AUTHORITY, "authentication",StbConfig.ATTRIBUTE_TYPE);
            sMatcher.addURI(StbConfig.AUTHORITY, "authentication"+"/#", StbConfig.ATTRIBUTE_ITEM);
    }
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
	     	SQLiteDatabase db = StbConfigDb.getWritableDatabase();
	        int count;
	        switch (sMatcher.match(uri)) {    // 这里要对不同表的匹配结果做不同处理，注意下面用到的表名不要弄错了
	        case StbConfig.STBCONFIG_TYPE:
	            count = db.delete(StbConfig.TNAME1, where, whereArgs);
	            break;

	        case StbConfig.STBCONFIG_ITEM:
	            String stbconfigId = uri.getPathSegments().get(1);
	            count = db.delete(StbConfig.TNAME1, StbConfig._ID + "=" + stbconfigId
	                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
	            break;
	            
	        case StbConfig.ATTRIBUTE_TYPE:
	        	count = db.delete(StbConfig.TNAME2, where, whereArgs);
	        	break;
	        	
	        case StbConfig.ATTRIBUTE_ITEM:
	        	String attributeId = uri.getPathSegments().get(1);
	        	count = db.delete(StbConfig.TNAME2, StbConfig._ID + "=" + attributeId
	        			+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
	        	break;

	        default:
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }

	        getContext().getContentResolver().notifyChange(uri, null);
	        return count;
	}

	@Override
	public String getType(Uri uri) {
        switch (sMatcher.match(uri)) {    // 这里也要增加匹配项
        case StbConfig.STBCONFIG_ITEM :
        case StbConfig.ATTRIBUTE_TYPE:
        	return StbConfig.CONTENT_TYPE ;

        case StbConfig.STBCONFIG_TYPE :
        case StbConfig.ATTRIBUTE_ITEM :
            return StbConfig.CONTENT_ITEM_TYPE;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		// TODO Auto-generated method stub
		
	   db = StbConfigDb.getWritableDatabase();
       long id = 0; 
       String tableName = "";
	   if(contentValues != null )
	   {
		   if(sMatcher.match(uri) != StbConfig.STBCONFIG_TYPE && sMatcher.match(uri) != StbConfig.ATTRIBUTE_TYPE )
		   {
			   
			   throw new IllegalArgumentException("Error Uri: " + uri); 
		   }
	        Log.i(TAG + " InsertMethod", "--- tableName=" + sMatcher.match(uri) + " ---");
	        switch (sMatcher.match(uri) ) {
	        
	        case StbConfig.STBCONFIG_TYPE:
	        	tableName = StbConfig.TNAME1 ;
	        	break;
	        case StbConfig.ATTRIBUTE_TYPE:
	        	tableName = StbConfig.TNAME2 ;
	          //  String path = uri.toString();
	          //   return Uri.parse(path.substring(0, path.lastIndexOf("/"))+id); // 替换掉id
	        	break;
	        default:
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }
	        
            id = db.insert(tableName, StbConfig._ID, contentValues);    
            if( id > 0 )
            {
            	  Uri noteUri = ContentUris.withAppendedId(uri, id);
            	  getContext().getContentResolver().notifyChange(noteUri, null); 
            	  return noteUri ;
            }
	            
	   }
	   return null;
}

	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
	   db = StbConfigDb.getWritableDatabase();
	   Cursor ch; 
       switch (sMatcher.match(uri)) {
             
       case StbConfig.STBCONFIG_TYPE:
   
    	    ch = db.query(StbConfig.TNAME1, projection, selection, selectionArgs, null, null, null);
    	    break ;
    case StbConfig.STBCONFIG_ITEM :
    	    ch = db.query(StbConfig.TNAME1, projection,  StbConfig._ID + "=",new String[] { (String)uri.getPathSegments().get(1) }, null, null, sortOrder);
       	
    	    break ;

       case StbConfig.ATTRIBUTE_TYPE :
    	   ch = db.query(StbConfig.TNAME2, projection, selection, selectionArgs, null, null, null);
    	   break;
       case StbConfig.ATTRIBUTE_ITEM:
   	      ch = db.query(StbConfig.TNAME2, projection,  StbConfig._ID + "=",new String[] { (String)uri.getPathSegments().get(1) }, null, null, sortOrder);

           break;
       default:
               Log.d("!!!!!!", "Unknown URI"+uri);
               throw new IllegalArgumentException("Unknown URI"+uri);
       }
         ch.setNotificationUri(getContext().getContentResolver(), uri);
		return ch;
	}
	
	  @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = StbConfigDb.getWritableDatabase();
        int count;
        switch (sMatcher.match(uri)) { // 这里要对不同表的匹配结果做不同处理，注意下面用到的表名不要弄错了
        case StbConfig.STBCONFIG_TYPE:
            count = db.update(StbConfig.TNAME1, values, where, whereArgs);
            break;

        case StbConfig.STBCONFIG_ITEM:
            String noteId = uri.getPathSegments().get(1);
            count = db.update(StbConfig.TNAME1, values, StbConfig._ID + "=" + noteId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;
        case StbConfig.ATTRIBUTE_TYPE:
        	count = db.update(StbConfig.TNAME2 , values, where, whereArgs);
        	break;
        	
        case StbConfig.ATTRIBUTE_ITEM:
        	String leaderId = uri.getPathSegments().get(1);
        	count = db.update(StbConfig.TNAME2, values, StbConfig._ID + "=" + leaderId
        			+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
        	break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

	@Override
	public boolean onCreate() {
		
        this.StbConfigDb = new DBlite(this.getContext());
        db = StbConfigDb.getWritableDatabase();
        
        	/* Create stbconfig db table */
        StbConfigDb.add(db,StbConfig.TNAME1 , 1,"Lang","2","3");
        StbConfigDb.add(db,StbConfig.TNAME1 ,2,"Device.LAN.IPPingDiagnostics.MinimumResponseTime",	"0","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,3,"Device.DeviceInfo.FirstUseDate","2010-10-14T09:00:00","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,4,"STBType","EC6106V6","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,5,"IPTV.BTV_CATEGORYID","BTV","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,6,"EPGUrl","http://122.229.7.68:8082/EPG/jsp/indexHWCTC.jsp","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,7,"Device.ManagementServer.ParameterKey","NbqVFbVCrl7QuU0Pv/Ua2w==","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,8,"UpgradeServerBackup","http://itvbf.zj.vnet.cn:8082","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,9,"Device.ManagementServer.ConnectionRequestURL","http://172.11.11.164:7547/cpe","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,10,"Device.LAN.IPPingDiagnostics.Host","100.1.1.20","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,11,"CSM_IP","null","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,12,"Device.X_00E0FC.StatisticConfiguration.BitRateR1","100","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,13,"Device.X_00E0FC.IsEncryptMark","1","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,14,"Device.X_00E0FC.StatisticConfiguration.BitRateR5","86","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,15,"Device.X_00E0FC.StatisticConfiguration.BitRateR4","88","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,16,"Device.X_00E0FC.StatisticConfiguration.BitRateR3","92","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,17,"Device.LAN.TraceRouteDiagnostics.DiagnosticsState","Requested","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,18,"Device.X_00E0FC.StatisticConfiguration.BitRateR2","96","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,19,"Device.LAN.IPPingDiagnostics.MaximumResponseTime","0","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,20,"Device.LAN.IPPingDiagnostics.Timeout","20","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,21,"ManagementDomain","http://122.229.6.226:37020/acs","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,22,"zeroTouchEnable","1","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,23,"Iptv.OTTEDSAddress","http://182.138.3.142:8082/EDS/jsp/AuthenticationURL","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,24,"NetDiagnoseTool.DiagnoseMode",null,"3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,25,"VodShowMode","DEFAULT","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,26,"Device.LAN.TraceRouteDiagnostics.DataBlockSize","60","3");
		StbConfigDb.add(db,StbConfig.TNAME1 ,27,"STBID","","3"); 

    	/* Create attribute db table */
		StbConfigDb.add(db,StbConfig.TNAME2 ,1,"STBID",""); 
		
		StbConfigDb.add(db,StbConfig.TNAME2 ,1,"username","" );
		StbConfigDb.add(db,StbConfig.TNAME2	,2,"password","NbqVFbVCrl7QuU0Pv/Ua2w==");
	    StbConfigDb.add(db,StbConfig.TNAME2	,3,"user_token","");
		StbConfigDb.add(db,StbConfig.TNAME2	,4,"epg_info","	");
		StbConfigDb.add(db,StbConfig.TNAME2	,5,"HostingAccountID","default");
		StbConfigDb.add(db,StbConfig.TNAME2	,6,"HostingPassword","WqZPWq3iA4vtIBdIJKmNjA==");
	    StbConfigDb.add(db,StbConfig.TNAME2	,7,"HostingSessionID","");
		StbConfigDb.add(db,StbConfig.TNAME2	,8,"HostingUserPhoto","");
		StbConfigDb.add(db,StbConfig.TNAME2	,9,"HostingNickname","");
		StbConfigDb.add(db,StbConfig.TNAME2	,10,"HostingMobileNumber","");
		StbConfigDb.add(db,StbConfig.TNAME2	,12,"authErrorCode","");
		StbConfigDb.add(db,StbConfig.TNAME2	,13,"authErrorType","");
		StbConfigDb.add(db,StbConfig.TNAME2	,14,"authErrorName","");
		StbConfigDb.add(db,StbConfig.TNAME2	,15,"authErrorDesc","");
		StbConfigDb.add(db,StbConfig.TNAME2	,16,"authErrorResolve","");
		StbConfigDb.add(db,StbConfig.TNAME2	,17,"usertoken","");
		StbConfigDb.add(db,StbConfig.TNAME2	,18,"LastEPGAdd","");
		StbConfigDb.add(db,StbConfig.TNAME2	,19,"IPTV_USER","");
		StbConfigDb.add(db,StbConfig.TNAME2	,20,"IPTV_USER_PASSWORD","NbqVFbVCrl7QuU0Pv/Ua2w==","");
		StbConfigDb.add(db,StbConfig.TNAME2	,21,"TVDesktopID",null);
		StbConfigDb.add(db,StbConfig.TNAME2	,22,"TVDesktopGetURL","");
		StbConfigDb.add(db,StbConfig.TNAME2	,23,"tvid","");
		StbConfigDb.add(db,StbConfig.TNAME2	,24,"cdn_slb","");
		StbConfigDb.add(db,StbConfig.TNAME2	,41,"authStatus","InAuth");
				
       return (db == null)?false:true;
	}



}
