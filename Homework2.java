package homework2;
import java.io.*;
import java.util.*;
public class Homework2 {
	static class Node{
		int x,y,v,num;
		Node(int a,int b,int c,int d){
			this.x=a;this.y=b;this.v=c;this.num=d;
		}
	}
	static class records
	{
		int len; int num; double retime; int dep; int branch; double usedtime;
		records(int a,int b,double c,int d,int e,double f) {
			this.len = a; this.num = b; this.retime = c; this.dep = d; this.branch = e; this.usedtime = f;
		}
	};
	//need a new comparator
	private static char[][]result;
	private static void gravity(char[][]result){
		int length=result.length;ArrayList<Character>temp1=new ArrayList<Character>();
		ArrayList<Character>temp2=new ArrayList<Character>();
		int hang,lie,i;
		for(lie=0;lie<length;lie++){
			temp1.clear();temp2.clear();
			for(hang=length-1;hang>=0;hang--){
				if(result[hang][lie]!='*'&&result[hang][lie]!='#'&&result[hang][lie]!='&')
				{
					temp1.add(result[hang][lie]);
				}
				else{
					temp2.add(result[hang][lie]);
				}
			}
			hang=length-1;
			for(i=0;i<temp1.size();i++){
				result[hang][lie]=temp1.get(i);hang--;
			}
			for(i=0;i<temp2.size();i++){
				result[hang][lie]=temp2.get(i);hang--;
			}
		}
		return;
	}
	private static void gai(char[][]result,boolean[][]visited,int i,int j,char value,int mode){
		int length=result.length;
		if(i<0||i>=length||j<0||j>=length||result[i][j]!=value||visited[i][j]){
			return;
		}
		if(mode==1){
			result[i][j]='*';visited[i][j]=true;
		}
		if(mode==2) {
			result[i][j]='#';visited[i][j]=true;
		}
		if(mode==3) {
			result[i][j]='&';visited[i][j]=true;
		}
		gai(result,visited,i+1,j,value,mode);
		gai(result,visited,i-1,j,value,mode);
		gai(result,visited,i,j+1,value,mode);
		gai(result,visited,i,j-1,value,mode);
		return;
	}
	private static int count(char[][]result){
		int num=0;int i,j;int length=result.length;
		for(i=0;i<length;i++){
			for(j=0;j<length;j++){
				if(result[i][j]=='*'){
					num++;
				}
			}
		}
		return num;
	}
	static class MyComparator implements Comparator{
		public int compare(Object obj1,Object obj2){
			Node a=(Node)obj1;Node b=(Node)obj2;
			if (a.num == b.num) {
				if (a.x < b.x) return -1;
				else {
					if (a.x == b.x) {
						if (a.y <= b.y) return -1;
						else return 1;
					}
					else return 1;
				}
			}
			else {
				if (a.num > b.num) return -1;
				else return 1;
			}
		}
	}
	private static ArrayList<Node>solution(char[][]result){
		int x=0;int y=0;int v=-1;int length=result.length;int num=count(result);
		int delta=0;ArrayList<Node>sol=new ArrayList<Node>();
		int i,j;char value;boolean[][]visited=new boolean[length][length];
		for(i=0;i<length;i++){
			for(j=0;j<length;j++){
				visited[i][j]=false;
			}
		}
		/*System.out.println("before search");
		for(i=0;i<length;i++){
			for(j=0;j<length;j++){
				System.out.print(result[i][j]);
			}
			System.out.println();
		}*/
		for(i=0;i<length;i++){
			for(j=0;j<length;j++){
				if(result[i][j]!='*'&&result[i][j]!='#'&&result[i][j]!='&'){
					value=result[i][j];gai(result,visited,i,j,value,1);
					delta=count(result)-num;x=i;y=j;v=value-'0';
					Node re=new Node(x,y,v,delta);
					sol.add(re);num=delta+num;
				}
			}
		}
		Collections.sort(sol,new MyComparator());
		return sol;
	}
	private static int estimateValue(char player,ArrayList<Integer>fvalue,ArrayList<Integer>lvalue){
		int fnum=0;int lnum=0;int i,j;
		for(i=0;i<fvalue.size();i++){
			fnum+=(fvalue.get(i)*fvalue.get(i));
		}
		for(j=0;j<lvalue.size();j++){
			lnum+=(lvalue.get(j)*lvalue.get(j));
		}
		if(player=='f') return fnum-lnum;
		else return lnum-fnum;
	}
	private static boolean exist(char[][]result){
		int i,j;int length=result.length;
		for(i=length-1;i>=0;i--){
			for(j=length-1;j>=0;j--){
				if(result[i][j]!='*'&&result[i][j]!='#'&&result[i][j]!='&'){
					return true;
				}
			}
		}
		return false;
	}
	private static void takefruits(char player,char[][]result,int i,int j){
		char val=result[i][j];int length=result.length;boolean[][]visited=new boolean[length][length];
		for(int r=0;r<length;r++){
			for(int c=0;c<length;c++){
				visited[r][c]=false;
			}
		}
		if(player=='f') gai(result,visited,i,j,val,2);
		if(player=='l') gai(result,visited,i,j,val,3);
		gravity(result);
	}
	private static void rollback(char player,char[][]result,char[][]base){
		result=base;
	}
	private static char playerExchange(char player){
		if(player=='f') return 'l';
		else return 'f';
	}
	private static int alphaBeta(char[][]result1,char player,int depth,int alpha,int beta,int length,int maxdepth,int branch,ArrayList<Integer>fvalue,ArrayList<Integer>lvalue)
	{
		char[][]cal=new char[length][length];int alpha_pr=alpha;int beta_pr=beta;
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++)
			{
				cal[i][j]=result1[i][j];
			}
		}
		if(depth==maxdepth){
			return estimateValue(player,fvalue,lvalue);
		}
		boolean downFlag=false;
		if(exist(cal)){
			downFlag=true;
			/*System.out.println("Before Solution");
			for(int i=0;i<length;i++){
				for(int j=0;j<length;j++){
					System.out.print(result1[i][j]);
				}
				System.out.println();
			}*/
			ArrayList<Node>solutions=solution(cal);
			/*System.out.println("After Solution");
			for(int i=0;i<length;i++){
				for(int j=0;j<length;j++){
					System.out.print(result1[i][j]);
				}
				System.out.println();
			}*/
			ArrayList<Node>choose=new ArrayList<Node>();
			int ss=solutions.size();
			for(int i=0;i<Math.min(branch, ss);i++){
				Node maxs=solutions.get(i);choose.add(maxs);
			}
			for(int i=0;i<length;i++){
				for(int j=0;j<length;j++)
				{
					cal[i][j]=result1[i][j];
				}
			}
			char[][]rebuild=new char[length][length];
			for(int i=0;i<length;i++){
				for(int j=0;j<length;j++)
				{
					rebuild[i][j]=result1[i][j];
				}
			}
			for(int i=0;i<choose.size();i++){
				takefruits(player,cal,choose.get(i).x,choose.get(i).y);
				if((depth&1)==1) lvalue.add(choose.get(i).num);
				else fvalue.add(choose.get(i).num);
				int v_now=alphaBeta(cal, playerExchange(player), depth + 1, alpha, beta, length, maxdepth,branch,fvalue,lvalue);
				rollback(player,cal,rebuild);
				if((depth&1)==1) lvalue.remove(lvalue.size()-1);
				else fvalue.remove(fvalue.size()-1);
				if((depth&1)==1){
					beta=Math.min(beta, v_now);
					if(beta<=alpha) return beta;
				}
				else{
					if(v_now>alpha&&depth==0){
						x_next=choose.get(i).x;y_next=choose.get(i).y;//System.out.println(x_next+"-"+y_next);
					}
					alpha=Math.max(alpha, v_now);
					if(alpha>=beta) return alpha;
				}
			}	
		}
		if(!downFlag) return estimateValue(player,fvalue,lvalue);
		int v_rt;
		if((depth&1)==1) v_rt=beta;
		else v_rt=alpha;
		alpha=alpha_pr;beta=beta_pr;
		return v_rt;
	}
	private static int x_next=-1;
	private static int y_next=-1;
	private static int[] findposition(char[][]result,double remaintime,int kinds,int chosedep,int chosebranch){
		int[]resultposition=new int[2];char play_first='f';
		char[][]cal=result;int alpha=-1000000;int beta=1000000;
		int length=result.length;ArrayList<Integer>fvalue=new ArrayList<Integer>();ArrayList<Integer>lvalue=new ArrayList<Integer>();
		/*for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				System.out.print(result[i][j]);
			}
			System.out.println();
		}*/
		int val=alphaBeta(cal,play_first,0,alpha,beta,length,chosedep,chosebranch,fvalue,lvalue);
		/*for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				System.out.print(result[i][j]);
			}
			System.out.println();
		}*/
		resultposition[0]=x_next;resultposition[1]=y_next; 
		//System.out.println(resultposition[0]+"-"+resultposition[1]);
		return resultposition;
	}
	public static void main(String[] args) {
		int leng=0;int num=0;double rtime=0;long a=System.currentTimeMillis();
		try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("input.txt")));
            String length = reader.readLine();
            String number = reader.readLine();
            String remaintime = reader.readLine();
            leng = Integer.valueOf(length);
            num = Integer.valueOf(number);
            rtime=Double.valueOf(remaintime);

            result = new char[leng][leng];

            int i = 0;
            String line = reader.readLine();
            while (line != null) {
                for (int j = 0; j < line.toCharArray().length; j++) {
                   result[i][j]=line.charAt(j);
                }
                i++;
                line = reader.readLine();
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		int choseddep=-1;int chosedbranch=-1;
		int len;int numtext;double rti;int dep;int branch;
		ArrayList<Double> ts=new ArrayList<Double>();double usedt;
		try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("calibrate.txt")));
            int i=0;int beg=0,end=0;
            String line = reader.readLine();
            while (line != null) {
                for (int j = 0; j < line.toCharArray().length; j++) {
                   if(line.charAt(j)=='('){
                	   beg=j+1;end=beg;
                   }
                   if(line.charAt(j)==')'){
                	   end=j;ts.add(Double.valueOf(line.substring(beg, end)));
                   }
                }
                len=ts.get(0).intValue();numtext=ts.get(1).intValue();rti=ts.get(2);dep=ts.get(3).intValue();branch=ts.get(4).intValue();usedt=ts.get(5);
                if(usedt<=rtime){
                	records rs=new records(len,numtext,rti,dep,branch,usedt);
                	if(Math.abs((leng-rs.len))<=2&&Math.abs(num-rs.num)<=2&&Math.abs(rtime-rs.retime)<=60.00){
                		if ((rs.usedtime <= (rtime*0.2)&&(rtime>=60))||(rs.usedtime<=(rtime*0.1)&&(rtime<60))) {
    						choseddep = Math.max(choseddep, rs.dep); chosedbranch = Math.max(chosedbranch,rs.branch);
    					}
                	}
                }
                i++;ts.clear();
                line = reader.readLine();
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		/*for(int i=0;i<leng;i++){
			for(int j=0;j<leng;j++){
				System.out.print(result[i][j]);
			}
			System.out.println();
		}*/
		int []position=findposition(result,rtime,num,choseddep,chosedbranch);
		/*for(int i=0;i<leng;i++){
			for(int j=0;j<leng;j++){
				System.out.print(result[i][j]);
			}
			System.out.println();
		}*/
		int selecthang=position[0];int selectlie=position[1];char value=result[selecthang][selectlie];
		boolean[][]visited=new boolean[leng][leng];
		for(int r=0;r<leng;r++){
			for(int c=0;c<leng;c++){
				visited[r][c]=false;
			}
		}
		/*for(int i=0;i<leng;i++){
			for(int j=0;j<leng;j++){
				System.out.print(result[i][j]);
			}
			System.out.println();
		}*/
		gai(result,visited,selecthang,selectlie,value,1);
		gravity(result);char wz=(char)(selectlie+'A');
		String weizhi1=""+wz;//'A'+lie
		String weizhi2=String.valueOf(selecthang+1);//hang+1
		String weizhi=weizhi1+weizhi2;
		BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File("output.txt")));
            writer.write(weizhi);
            for (int i = 0; i < leng; i++) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < leng; j++) {
                    builder.append(result[i][j]);
                }
                writer.newLine();
                writer.write(builder.toString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\rÖ´ÐÐºÄÊ± : "+(System.currentTimeMillis()-a)/1000f+" Ãë ");
	}
}
