package main;

import classfile.Disasm;

public class SearchMethod implements Runnable{
    Main main;
    String src,search;
    int[] mIndex=new int[]{-1};
    public SearchMethod(Main main,String search){
        this.main=main;
        this.search=search;
        new Thread(this).start();
    }
    private void addInt(int i){
        int[] temp=mIndex;
        int count=mIndex.length;
        mIndex=new int[count+1];
        System.arraycopy(temp,0,mIndex,0,count);
        mIndex[count]=i;
    }
    
    
    public int getIndex(int i){
        return mIndex[i+1];
    }
    
        
    private void search(){
        String[] str=main.classFile.getMethodNames();
        String src="";
        Disasm dm=new Disasm(main.classFile);
        for(int i=0;i<str.length;i++){
            main.methodList.setTitle(str[i]);
            int[] code=main.classFile.getMethodCode(i);
            if( code == null )
                continue;
            src=dm.disasm(code);
            if(src.indexOf(search) != -1)
            {
                main.methodList.append(str[i],main.image[5]);
                addInt(i);
            }
        }
    }
    
    public void run(){
        search();
        main.methodList.setTitle(search);
    }
}