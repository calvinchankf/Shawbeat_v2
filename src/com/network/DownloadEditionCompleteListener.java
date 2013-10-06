package com.network;
import java.util.List;
import com.shawbeat.Edition;

public interface DownloadEditionCompleteListener {
	//void downloadEditionResultCallback(String s);
	void downloadEditionResultCallback(List<Edition> s);
}
