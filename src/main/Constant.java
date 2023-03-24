package main;

public class Constant{
    public static final String[] title={"关于","类编辑","快捷键","常量编辑","指令","其他"};
    public static final String[] msg={"      Java字节码编辑器\n",
"    可添加及删除方法、域，可修改方法、域的访问控制符、名字及Descriptor，可添加修改方法异常表。\r\n    搜索功能：\n    搜索文件夹下所有class文件〔包括子目录〕、对类中方法进行搜索、从zip文件中搜索。\r\n    代码编辑：\r\n    关于ldc指令: \n    若要修改ldc指令后的参数, 需将ldc指令替换为ldc_w(宽索引)。\r\n",
"代码编辑界面快捷键\n   #键：切换输入模式\r\n   快捷模式\r\n   1键：标记文本,复制选中文本\n   2键：剪切\n   3键：粘贴\n   4，6键：上下翻页\n   5键：字节偏移\n   7键：替换\n   8键：搜索\n   9键：指令说明\n   笔形键：粘贴〔S60〕\r\r\n常量编辑界面快捷键\r\n   0键：跳转\n   1键：跳至开头\n   2键：谷歌翻译\n   3键：跳至末尾\n   4，6键：上下翻页\n   7键：替换\n   8键：搜索\n   *, #键：过滤常量",
"关于过滤常量\n   两中过滤方式：第一种：保留了utf8结构的常量及int, long, ……第二种：只保留部分utf8结构常量〔适合汉化〕\n" , 
"    本程序没有包含所有JVM指令，如 goto_w, ret, jsr……这些指令有的在J2me中没有，有的出现机率太小，所以就没加进去。\n关于tableswitch, lookupswitch指令：\n    如tableswitch Label68 0:Label24,1:Label34,2:Label56,3:Label65，Label68是默认执行的代码位置，所有添转都以Label标记,Label后面可以是数字或其他如LabelX ……\ntableswitch,lookupswitch代码形式一样…\n \r\n关于wide指令\r\n    只实现了wide: iinc 被写成wide&iinc这种形式，无法处理wide与其他指令搭配。\r\n",
"   代码导入导出功能，导出代码文件名以类名+方法索引，后缀为i。\r\n   请保留导出代码第一行的信息〔因为导入代码时会忽略第一行〕。\r\r\n           vx：\n           m\r\r\n"};
}