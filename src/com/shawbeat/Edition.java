package com.shawbeat;

public class Edition {
	private int _edition_ID;
	private String _edition_name="";
	private String _date="";
	
	public int getEdition_ID()
	{ 
	    return _edition_ID; 
	}
	public String getEdition_name()
	{ 
	    return _edition_name; 
	}
	public String getDate()
	{ 
	    return _date; 
	}
	public void setEdition_ID(int edition_ID)
	{ 
		_edition_ID=edition_ID; 
	}
	public void setEdition_name(String edition_name)
	{ 
		_edition_name=edition_name; 
	}
	public void setDate(String date)
	{ 
		_date=date; 
	}

}
