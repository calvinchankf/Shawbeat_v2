package com.shawbeat;

public class Category {
	private int _edition_ID;
	private int _category_id;
	private String _category_name="";
	
	public int getEdition_ID()
	{ 
	    return _edition_ID; 
	}
	public int getCategory_id()
	{ 
	    return _category_id; 
	}
	public String getCategory_name()
	{ 
	    return _category_name; 
	}
	public void setEdition_ID(int edition_ID)
	{ 
	    _edition_ID=edition_ID; 
	}
	public void setCategory_id(int category_id)
	{ 
		_category_id=category_id; 
	}
	public void setCategory_name(String category_name)
	{ 
		_category_name=category_name; 
	}
}
