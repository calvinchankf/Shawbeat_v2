package com.network;

import java.util.List;

import com.shawbeat.Article;

public interface DownloadArticleCompleteListener {
	void downloadArticleResultCallback(List<Article> s);
}
