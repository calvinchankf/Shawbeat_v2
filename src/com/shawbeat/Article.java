package com.shawbeat;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable{
	private int _article_ID;
	private int _edition_ID;
	private int _position;
	private String _title="";
	private String _author="";
	private String _content="";
	private int _category_id;
	private String _category_name;
	
	public int getArticleID()
	{ 
	    return _article_ID; 
	}
	public int getEdition_ID()
	{ 
	    return _edition_ID; 
	}
	public int getPosition()
	{ 
	    return _position; 
	}
	public String getTitle()
	{ 
	    return _title; 
	}
	public String getAuthor()
	{ 
	    return _author; 
	}
	public String getContent()
	{ 
	    return _content;
	}
	public int getCategory_id()
	{ 
	    return _category_id; 
	}
	public String getCategory_name()
	{ 
	    return _category_name; 
	}
	public void setArticleID(int article_ID)
	{ 
	    _article_ID=article_ID; 
	}
	public void setEdition_ID(int edition_ID)
	{ 
	    _edition_ID=edition_ID; 
	}
	public void setPosition(int position)
	{ 
	    _position=position; 
	}
	public void setTitle(String title)
	{ 
	    _title=title; 
	}
	public void setAuthor(String author)
	{ 
	    _author=author; 
	}
	public void setContent(String content)
	{ 
	    _content=content; 
	}
	public void setCategory_id(int category_id)
	{ 
	    _category_id=category_id; 
	}
	public void setCategory_name(String category_name)
	{ 
	    _category_name=category_name; 
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
	

}
