package com.guohao.object.pull;

import android.view.View;

public interface Pull {
	//��������ͷ
	public void setHeaderView(View view);
	//��������β
	public void setFooterView(View view);
	
	//��ȡ����ͷ�ĳߴ�
	public void getHeaderSize();
	//��ȡ����ͷ�ĳߴ�
	public void getFooterSize();
	
	//��������������
	public void setPullDownListener();
	//��������������
	public void setPullUpListener();
	
	//�ص�����---����
	public void onPullDown();
	//�ص�����---����
	public void onPullUp();
	
}
