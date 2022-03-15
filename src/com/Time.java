package com;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Time extends Thread {
	Main main;
	boolean isRun = true;
	int i = 10;

	public Time(Main m, int i) {
		this.main = m;
		this.i = i;
	}

	@Override
	public void run() {

		while (i > -1 && isRun) {
			main.time[1].setText("����ʱ:" + i--);
			second(1);// ��һ��
		}
		if (i == -1)// �����սᣬ˵����ʱ
			main.time[1].setText("����");
		main.landlord[0].setVisible(false);
		main.landlord[1].setVisible(false);
		for (Card card2 : main.playerList[1])
			card2.canClick = true;// �ɱ����
		// ����Լ���������
		if (main.time[1].getText().equals("������")) {
			// �õ�������
			main.playerList[1].addAll(main.lordList);
			openlord(true);
			second(2);// �ȴ�����
			Common.order(main.playerList[1]);
			Common.rePosition(main, main.playerList[1], 1);
			setlord(1);
		} else {
			// ����ѡ����
			if (Common.getScore(main.playerList[0]) < Common
					.getScore(main.playerList[2])) {
				main.time[2].setText("������");
				main.time[2].setVisible(true);
				setlord(2);// �趨����
				openlord(true);
				second(3);
				main.playerList[2].addAll(main.lordList);
				Common.order(main.playerList[2]);
				Common.rePosition(main, main.playerList[2], 2);
				openlord(false);

			} else {
				main.time[0].setText("������");
				main.time[0].setVisible(true);
				setlord(0);// �趨����
				openlord(true);
				second(3);
				main.playerList[0].addAll(main.lordList);
				Common.order(main.playerList[0]);
				Common.rePosition(main, main.playerList[0], 0);
				//openlord(false);

			}
		}
		// ѡ������� �رյ�����ť
		main.landlord[0].setVisible(false);
		main.landlord[1].setVisible(false);
		turnOn(false);
		for (int i = 0; i < 3; i++)
		{
			main.time[i].setText("��Ҫ");
			main.time[i].setVisible(false);
		}
		// ��ʼ��Ϸ ���ݵ�����ͬ˳��ͬ
		main.turn=main.dizhuFlag;
		while (true) {
			
			if(main.turn==1) //��
			{
				turnOn(true);// ���ư�ť --�ҳ���
				timeWait(30, 1);// ���Լ��Ķ�ʱ��
				turnOn(false);//ѡ��ر�
				main.turn=(main.turn+1)%3;
				if(win())//�ж���Ӯ
					break;
			}
			if (main.turn==0) 
			{
				computer0();
				main.turn=(main.turn+1)%3;
				if(win())//�ж���Ӯ
					break;
			}
			if(main.turn==2)
			{
				computer2();
				main.turn=(main.turn+1)%3;
				if(win())//�ж���Ӯ
					break;
			}
		}
	}

	// �ȴ�i��
	public void second(int i) {
		try {
			Thread.sleep(i * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �����Ʒ���
	public void openlord(boolean is) {
		for (int i = 0; i < 3; i++) {
			if (is)
				main.lordList.get(i).turnFront(); // �����Ʒ���
			else {
				main.lordList.get(i).turnRear(); // �����Ʊպ�
			}
			main.lordList.get(i).canClick = true;// �ɱ����
		}
	}

	// �趨����
	public void setlord(int i) {
		Point point = new Point();
		if (i == 1)// ���ǵ���
		{
			point.x = 80;
			point.y = 430;
			main.dizhuFlag = 1;// �趨����
		}
		if (i == 0) {
			point.x = 80;
			point.y = 20;
			main.dizhuFlag = 0;
		}
		if (i == 2) {
			point.x = 700;
			point.y = 20;
			main.dizhuFlag = 2;
		}
		main.dizhu.setLocation(point);
		main.dizhu.setVisible(true);
	}

	// �򿪳��ư�ť
	public void turnOn(boolean flag) {
		main.publishCard[0].setVisible(flag);
		main.publishCard[1].setVisible(flag);
	}

	// ����0����(�Ҵ���1)
	public void computer0() {
		timeWait(1, 0); // ��ʱ
		ShowCard(0); // ����
		
	}

	// ����2����(�Ҵ���1)
	public void computer2() {
		timeWait(1, 2); // ��ʱ
		ShowCard(2); // ����
		
	}

	// ����
	public void ShowCard(int role) {
		Model model = Common.getModel(main.playerList[role]);
		// ���ߵ���
		List<String> list = new ArrayList();
		// �������������
		if (main.time[(role + 1) % 3].getText().equals("��Ҫ")
				&& main.time[(role + 2) % 3].getText().equals("��Ҫ")) {
			// �е����� (����3�����ɻ��ܴ��ĵ���)
			if (model.a1.size() > (model.a111222.size() * 2 + model.a3.size())) {
				list.add(model.a1.get(model.a1.size() - 1));
			}// �ж��ӳ����� (����3�����ɻ�)
			else if (model.a2.size() > (model.a111222.size() * 2 + model.a3
					.size())) {
				list.add(model.a2.get(model.a2.size() - 1));
			}// ��˳�ӳ�˳��
			else if (model.a123.size() > 0) {
				list.add(model.a123.get(model.a123.size() - 1));
			}// ��3���ͳ�3����û�оͳ���3
			else if (model.a3.size() > 0) {
				// 3����,�ҷǹؼ�ʱ�̲��ܴ�����2
				if (model.a1.size() > 0) {
					list.add(model.a1.get(model.a1.size() - 1));
				}// 3����
				else if (model.a2.size() > 0) {
					list.add(model.a2.get(model.a2.size() - 1));
				}
				list.add(model.a3.get(model.a3.size() - 1));
			}// ��˫˳��˫˳
			else if (model.a112233.size() > 0) {
				list.add(model.a112233.get(model.a112233.size() - 1));
			}// �зɻ����ɻ�
			else if (model.a111222.size() > 0) {
				String name[] = model.a111222.get(0).split(",");
				// ����
				if (name.length / 3 <= model.a1.size()) {
					list.add(model.a111222.get(model.a111222.size() - 1));
					for (int i = 0; i < name.length / 3; i++)
						list.add(model.a1.get(i));
				} else if (name.length / 3 <= model.a2.size())// ��˫
				{
					list.add(model.a111222.get(model.a111222.size() - 1));
					for (int i = 0; i < name.length / 3; i++)
						list.add(model.a2.get(i));
				}
				// ��ը����ը��
			} else if (model.a4.size() > 0) {
				// 4��2,1
				int sizea1 = model.a1.size();
				int sizea2 = model.a2.size();
				if (sizea1 >= 2) {
					list.add(model.a1.get(sizea1 - 1));
					list.add(model.a1.get(sizea1 - 2));
					list.add(model.a4.get(0));
				
				} else if (sizea2 >= 2) {
					list.add(model.a2.get(sizea1 - 1));
					list.add(model.a2.get(sizea1 - 2));
					list.add(model.a4.get(0));
					
				} else {// ֱ��ը
					list.add(model.a4.get(0));
					
				}

			}
		}// ����Ǹ���
		else {
			List<Card> player = main.currentList[(role + 2) % 3].size() > 0 
					? main.currentList[(role + 2) % 3]
					: main.currentList[(role + 1) % 3];
			
			CardType cType=Common.jugdeType(player);
			//����ǵ���
			if(cType==CardType.c1)
			{
				AI_1(model.a1, player, list, role);
			}//����Ƕ���
			else if(cType==CardType.c2)
			{
				AI_1(model.a2, player, list, role);
			}//3��
			else if(cType==CardType.c3)
			{
				AI_1(model.a3, player, list, role);
			}//ը��
			else if(cType==CardType.c4)
			{
				AI_1(model.a4, player, list, role);
			}//�����3��1
			else if(cType==CardType.c31){
				 //ƫ�� �漰������
				//if((role+1)%3==main.dizhuFlag)
					AI_2(model.a3, model.a1, player, list, role);
			}//�����3��2
			else if(cType==CardType.c32){
				 //ƫ��
				//if((role+1)%3==main.dizhuFlag)
					AI_2(model.a3, model.a2, player, list, role);
			}//�����4��11
			else if(cType==CardType.c411){
					AI_5(model.a4, model.a1, player, list, role);
			}
			//�����4��22
			else if(cType==CardType.c422){
					AI_5(model.a4, model.a2, player, list, role);
			}
			//˳��
			else if(cType==CardType.c123){
				AI_3(model.a123, player, list, role);
			}
			//˫˳
			else if(cType==CardType.c1122){
				AI_3(model.a112233, player, list, role);
			}
			//�ɻ�����
			else if(cType==CardType.c11122234){
				AI_4(model.a111222,model.a1, player, list, role);
			}
			//�ɻ�����
			else if(cType==CardType.c1112223344){
				AI_4(model.a111222,model.a2, player, list, role);
			}
			//ը��
			if(list.size()==0)
			{
				int len4=model.a4.size();
				if(len4>0)
					list.add(model.a4.get(len4-1));
			}
		}

		// ��λ����
		main.currentList[role].clear();
		if (list.size() > 0) {
			Point point = new Point();
			if (role == 0)
				point.x = 200;
			if (role == 2)
				point.x = 550;
			point.y = (400 / 2) - (list.size() + 1) * 15 / 2;// ��Ļ�в�
			// ��nameת����Card
			for (int i = 0, len = list.size(); i < len; i++) {
				List<Card> cards = getCardByName(main.playerList[role],
						list.get(i));
				for (Card card : cards) {
					Common.move(card, card.getLocation(), point);
					point.y += 15;
					main.currentList[role].add(card);
					main.playerList[role].remove(card);
				}
			}
			Common.rePosition(main, main.playerList[role], role);
		} else {
			main.time[role].setVisible(true);
			main.time[role].setText("��Ҫ");
		}
		for(Card card:main.currentList[role])
			card.turnFront();
	}

	// ��name���Card�������Modelȡ��
	public List getCardByName(List<Card> list, String n) {
		String[] name = n.split(",");
		List cardsList = new ArrayList<Card>();
		int j = 0;
		for (int i = 0, len = list.size(); i < len; i++) {
			if (j < name.length && list.get(i).name.equals(name[j])) {
				cardsList.add(list.get(i));
				i = 0;
				j++;
			}
		}
		return cardsList;
	}
	//˳��
	public void AI_3(List<String> model,List<Card> player,List<String> list,int role){
		
		for(int i=0,len=model.size();i<len;i++)
		{
			String []s=model.get(i).split(",");
			if(s.length==player.size()&&getValueInt(model.get(i))>Common.getValue(player.get(0)))
			{
				list.add(model.get(i));
				return;
			}
		}
	}
	//�ɻ�������˫
	public void AI_4(List<String> model1,List<String> model2,List<Card> player,List<String> list,int role){
		//�����ظ���
		player=Common.getOrder2(player);
		int len1=model1.size();
		int len2=model2.size();
		
		if(len1<1 || len2<1)
			return;
		for(int i=0;i<len1;i++){
			String []s=model1.get(i).split(",");
			String []s2=model2.get(0).split(",");
			if((s.length/3<=len2)&&(s.length*(3+s2.length)==player.size())&&getValueInt(model1.get(i))>Common.getValue(player.get(0)))
			{
				list.add(model1.get(i));
				for(int j=1;j<=s.length/3;j++)
					list.add(model2.get(len2-j));
			}
		}
	}
	//4��1��2
	public void AI_5(List<String> model1,List<String> model2,List<Card> player,List<String> list,int role){
		//�����ظ���
		player=Common.getOrder2(player);
		int len1=model1.size();
		int len2=model2.size();
		
		if(len1<1 || len2<2)
			return;
		for(int i=0;i<len1;i++){
			if(getValueInt(model1.get(i))>Common.getValue(player.get(0)))
			{
				list.add(model1.get(i));
				for(int j=1;j<=2;j++)
					list.add(model2.get(len2-j));
			}
		}
	}
	//���ƣ����ӣ�3����4��,ͨ��
	public void AI_1(List<String> model,List<Card> player,List<String> list,int role){
		//����
		if((role+1)%3==main.dizhuFlag)
		{
			
			for(int i=0,len=model.size();i<len;i++)
			{
				if(getValueInt(model.get(i))>Common.getValue(player.get(0)))
				{
					list.add(model.get(i));
					break;
				}
			}
		}else {//ƫ��
			
			for(int len=model.size(),i=len-1;i>=0;i--)
			{
				if(getValueInt(model.get(i))>Common.getValue(player.get(0)))
				{
					list.add(model.get(i));
					break;
				}
			}
		}
	}
	//3��1,2,4��1,2
	public void AI_2(List<String> model1,List<String> model2,List<Card> player,List<String> list,int role){
		//model1������,model2�Ǵ���,player����ҳ�����,,list��׼���ص���
		//�����ظ���
		player=Common.getOrder2(player);
		int len1=model1.size();
		int len2=model2.size();
		//�������ֱ��ը��
		if(len1>0&&model1.get(0).length()<10)
		{
			list.add(model1.get(0));
			System.out.println("��ը");
			return;
		}
		if(len1<1 || len2<1)
			return;
		for(int len=len1,i=len-1;i>=0;i--)
		{	
			if(getValueInt(model1.get(i))>Common.getValue(player.get(0)))
			{
				list.add(model1.get(i));
				break;
			}
		} 
		list.add(model2.get(len2-1));
		if(list.size()<2)
			list.clear();
	}
	// ��ʱ��ģ��ʱ��
	public void timeWait(int n, int player) {

		if (main.currentList[player].size() > 0)
			Common.hideCards(main.currentList[player]);
		if (player == 1)// ������ң�10�뵽��ֱ����һ�ҳ���
		{
			int i = n;

			while (main.nextPlayer == false && i >= 0) {
				// main.container.setComponentZOrder(main.time[player], 0);
				main.time[player].setText("����ʱ:" + i);
				main.time[player].setVisible(true);
				second(1);
				i--;
			}
			if (i == -1) {
				main.time[player].setText("��ʱ");
			}
			main.nextPlayer = false;
		} else {
			for (int i = n; i >= 0; i--) {
				second(1);
				// main.container.setComponentZOrder(main.time[player], 0);
				main.time[player].setText("����ʱ:" + i);
				main.time[player].setVisible(true);
			}
		}
		main.time[player].setVisible(false);
	}
	//ͨ��name��ֵ
	public  int getValueInt(String n){
		String name[]=n.split(",");
		String s=name[0];
		int i=Integer.parseInt(s.substring(2, s.length()));
		if(s.substring(0, 1).equals("5"))
			i+=3;
		if(s.substring(2, s.length()).equals("1")||s.substring(2, s.length()).equals("2"))
			i+=13;
		return i;
	}
	//�ж���Ӯ
	public boolean win(){
		for(int i=0;i<3;i++){
			if(main.playerList[i].size()==0)
			{
				String s;
				if(i==1)
				{
					s="��ϲ�㣬ʤ����!";
				}else {
					s="��ϲ����"+i+",Ӯ��! ��������д����Ŷ";
				}
				JOptionPane.showMessageDialog(main, s);
				return true;
			}
		}
		return false;
	}
}
