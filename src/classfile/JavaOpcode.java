package classfile;

import java.util.Hashtable;

public class JavaOpcode {

    public static final int NOP = 0;
    public static final int ACONST_NULL = 1;
    public static final int ICONST_M1 = 2;
    public static final int ICONST_0 = 3;
    public static final int ICONST_1 = 4;
    public static final int ICONST_2 = 5;
    public static final int ICONST_3 = 6;
    public static final int ICONST_4 = 7;
    public static final int ICONST_5 = 8;
    public static final int LCONST_0 = 9;
    public static final int LCONST_1 = 10;
    public static final int FCONST_0 = 11;
    public static final int FCONST_1 = 12;
    public static final int FCONST_2 = 13;
    public static final int DCONST_0 = 14;
    public static final int DCONST_1 = 15;
    public static final int BIPUSH = 16;
    public static final int SIPUSH = 17;
    public static final int LDC = 18;
    public static final int LDC_W = 19;
    public static final int LDC2_W = 20;
    public static final int ILOAD = 21;
    public static final int LLOAD = 22;
    public static final int FLOAD = 23;
    public static final int DLOAD = 24;
    public static final int ALOAD = 25;
    public static final int ILOAD_0 = 26;
    public static final int ILOAD_1 = 27;
    public static final int ILOAD_2 = 28;
    public static final int ILOAD_3 = 29;
    public static final int LLOAD_0 = 30;
    public static final int LLOAD_1 = 31;
    public static final int LLOAD_2 = 32;
    public static final int LLOAD_3 = 33;
    public static final int FLOAD_0 = 34;
    public static final int FLOAD_1 = 35;
    public static final int FLOAD_2 = 36;
    public static final int FLOAD_3 = 37;
    public static final int DLOAD_0 = 38;
    public static final int DLOAD_1 = 39;
    public static final int DLOAD_2 = 40;
    public static final int DLOAD_3 = 41;
    public static final int ALOAD_0 = 42;
    public static final int ALOAD_1 = 43;
    public static final int ALOAD_2 = 44;
    public static final int ALOAD_3 = 45;
    public static final int IALOAD = 46;
    public static final int LALOAD = 47;
    public static final int FALOAD = 48;
    public static final int DALOAD = 49;
    public static final int AALOAD = 50;
    public static final int BALOAD = 51;
    public static final int CALOAD = 52;
    public static final int SALOAD = 53;
    public static final int ISTORE = 54;
    public static final int LSTORE = 55;
    public static final int FSTORE = 56;
    public static final int DSTORE = 57;
    public static final int ASTORE = 58;
    public static final int ISTORE_0 = 59;
    public static final int ISTORE_1 = 60;
    public static final int ISTORE_2 = 61;
    public static final int ISTORE_3 = 62;
    public static final int LSTORE_0 = 63;
    public static final int LSTORE_1 = 64;
    public static final int LSTORE_2 = 65;
    public static final int LSTORE_3 = 66;
    public static final int FSTORE_0 = 67;
    public static final int FSTORE_1 = 68;
    public static final int FSTORE_2 = 69;
    public static final int FSTORE_3 = 70;
    public static final int DSTORE_0 = 71;
    public static final int DSTORE_1 = 72;
    public static final int DSTORE_2 = 73;
    public static final int DSTORE_3 = 74;
    public static final int ASTORE_0 = 75;
    public static final int ASTORE_1 = 76;
    public static final int ASTORE_2 = 77;
    public static final int ASTORE_3 = 78;
    public static final int IASTORE = 79;
    public static final int LASTORE = 80;
    public static final int FASTORE = 81;
    public static final int DASTORE = 82;
    public static final int AASTORE = 83;
    public static final int BASTORE = 84;
    public static final int CASTORE = 85;
    public static final int SASTORE = 86;
    public static final int POP = 87;
    public static final int POP2 = 88;
    public static final int DUP = 89;
    public static final int DUP_X1 = 90;
    public static final int DUP_X2 = 91;
    public static final int DUP2 = 92;
    public static final int DUP2_X1 = 93;
    public static final int DUP2_X2 = 94;
    public static final int SWAP = 95;
    public static final int IADD = 96;
    public static final int LADD = 97;
    public static final int FADD = 98;
    public static final int DADD = 99;
    public static final int ISUB = 100;
    public static final int LSUB = 101;
    public static final int FSUB = 102;
    public static final int DSUB = 103;
    public static final int IMUL = 104;
    public static final int LMUL = 105;
    public static final int FMUL = 106;
    public static final int DMUL = 107;
    public static final int IDIV = 108;
    public static final int LDIV = 109;
    public static final int FDIV = 110;
    public static final int DDIV = 111;
    public static final int IREM = 112;
    public static final int LREM = 113;
    public static final int FREM = 114;
    public static final int DREM = 115;
    public static final int INEG = 116;
    public static final int LNEG = 117;
    public static final int FNEG = 118;
    public static final int DNEG = 119;
    public static final int ISHL = 120;
    public static final int LSHL = 121;
    public static final int ISHR = 122;
    public static final int LSHR = 123;
    public static final int IUSHR = 124;
    public static final int LUSHR = 125;
    public static final int IAND = 126;
    public static final int LAND = 127;
    public static final int IOR = 128;
    public static final int LOR = 129;
    public static final int IXOR = 130;
    public static final int LXOR = 131;
    public static final int IINC = 132;
    public static final int I2L = 133;
    public static final int I2F = 134;
    public static final int I2D = 135;
    public static final int L2I = 136;
    public static final int L2F = 137;
    public static final int L2D = 138;
    public static final int F2I = 139;
    public static final int F2L = 140;
    public static final int F2D = 141;
    public static final int D2I = 142;
    public static final int D2L = 143;
    public static final int D2F = 144;
    public static final int I2B = 145;
    public static final int I2C = 146;
    public static final int I2S = 147;
    public static final int LCMP = 148;
    public static final int FCMPL = 149;
    public static final int FCMPG = 150;
    public static final int DCMPL = 151;
    public static final int DCMPG = 152;
    public static final int IFEQ = 153;
    public static final int IFNE = 154;
    public static final int IFLT = 155;
    public static final int IFGE = 156;
    public static final int IFGT = 157;
    public static final int IFLE = 158;
    public static final int IF_ICMPEQ = 159;
    public static final int IF_ICMPNE = 160;
    public static final int IF_ICMPLT = 161;
    public static final int IF_ICMPGE = 162;
    public static final int IF_ICMPGT = 163;
    public static final int IF_ICMPLE = 164;
    public static final int IF_ACMPEQ = 165;
    public static final int IF_ACMPNE = 166;
    public static final int GOTO = 167;
    public static final int JSR = 168;
    public static final int RET = 169;
    public static final int TABLESWITCH = 170;
    public static final int LOOKUPSWITCH = 171;
    public static final int IRETURN = 172;
    public static final int LRETURN = 173;
    public static final int FRETURN = 174;
    public static final int DRETURN = 175;
    public static final int ARETURN = 176;
    public static final int RETURN = 177;
    public static final int GETSTATIC = 178;
    public static final int PUTSTATIC = 179;
    public static final int GETFIELD = 180;
    public static final int PUTFIELD = 181;
    public static final int INVOKEVIRTUAL = 182;
    public static final int INVOKESPECIAL = 183;
    public static final int INVOKESTATIC = 184;
    public static final int INVOKEINTERFACE = 185;
    public static final int XXXUNUSEDXXX = 186;
    public static final int NEW = 187;
    public static final int NEWARRAY = 188;
    public static final int ANEWARRAY = 189;
    public static final int ARRAYLENGTH = 190;
    public static final int ATHROW = 191;
    public static final int CHECKCAST = 192;
    public static final int INSTANCEOF = 193;
    public static final int MONITORENTER = 194;
    public static final int MONITOREXIT = 195;
    public static final int WIDE = 196;
    public static final int MULTIANEWARRAY = 197;
    public static final int IFNULL = 198;
    public static final int IFNONNULL = 199;
    public static final int GOTO_W = 200;
    public static final int JSR_W = 201;
    public static final int BREAKPOINT = 202;
    public static final int IMPDEP1 = 254;
    public static final int IMPDEP2 = 255;
    private static final String[] opcodes = {
        "nop",
        "aconst_null",
        "iconst_m1",
        "iconst_0",
        "iconst_1",
        "iconst_2",
        "iconst_3",
        "iconst_4",
        "iconst_5",
        "lconst_0",
        "lconst_1",
        "fconst_0",
        "fconst_1",
        "fconst_2",
        "dconst_0",
        "dconst_1",
        "bipush",
        "sipush",
        "ldc",
        "ldc_w",
        "ldc2_w",
        "iload",
        "lload",
        "fload",
        "dload",
        "aload",
        "iload_0",
        "iload_1",
        "iload_2",
        "iload_3",
        "lload_0",
        "lload_1",
        "lload_2",
        "lload_3",
        "fload_0",
        "fload_1",
        "fload_2",
        "fload_3",
        "dload_0",
        "dload_1",
        "dload_2",
        "dload_3",
        "aload_0",
        "aload_1",
        "aload_2",
        "aload_3",
        "iaload",
        "laload",
        "faload",
        "daload",
        "aaload",
        "baload",
        "caload",
        "saload",
        "istore",
        "lstore",
        "fstore",
        "dstore",
        "astore",
        "istore_0",
        "istore_1",
        "istore_2",
        "istore_3",
        "lstore_0",
        "lstore_1",
        "lstore_2",
        "lstore_3",
        "fstore_0",
        "fstore_1",
        "fstore_2",
        "fstore_3",
        "dstore_0",
        "dstore_1",
        "dstore_2",
        "dstore_3",
        "astore_0",
        "astore_1",
        "astore_2",
        "astore_3",
        "iastore",
        "lastore",
        "fastore",
        "dastore",
        "aastore",
        "bastore",
        "castore",
        "sastore",
        "pop",
        "pop2",
        "dup",
        "dup_x1",
        "dup_x2",
        "dup2",
        "dup2_x1",
        "dup2_x2",
        "swap",
        "iadd",
        "ladd",
        "fadd",
        "dadd",
        "isub",
        "lsub",
        "fsub",
        "dsub",
        "imul",
        "lmul",
        "fmul",
        "dmul",
        "idiv",
        "ldiv",
        "fdiv",
        "ddiv",
        "irem",
        "lrem",
        "frem",
        "drem",
        "ineg",
        "lneg",
        "fneg",
        "dneg",
        "ishl",
        "lshl",
        "ishr",
        "lshr",
        "iushr",
        "lushr",
        "iand",
        "land",
        "ior",
        "lor",
        "ixor",
        "lxor",
        "iinc",
        "i2l",
        "i2f",
        "i2d",
        "l2i",
        "l2f",
        "l2d",
        "f2i",
        "f2l",
        "f2d",
        "d2i",
        "d2l",
        "d2f",
        "i2b",
        "i2c",
        "i2s",
        "lcmp",
        "fcmpl",
        "fcmpg",
        "dcmpl",
        "dcmpg",
        "ifeq",
        "ifne",
        "iflt",
        "ifge",
        "ifgt",
        "ifle",
        "if_icmpeq",
        "if_icmpne",
        "if_icmplt",
        "if_icmpge",
        "if_icmpgt",
        "if_icmple",
        "if_acmpeq",
        "if_acmpne",
        "goto",
        "jsr",
        "ret",
        "tableswitch",
        "lookupswitch",
        "ireturn",
        "lreturn",
        "freturn",
        "dreturn",
        "areturn",
        "return",
        "getstatic",
        "putstatic",
        "getfield",
        "putfield",
        "invokevirtual",
        "invokespecial",
        "invokestatic",
        "invokeinterface",
        "xxxunusedxxx",
        "new",
        "newarray",
        "anewarray",
        "arraylength",
        "athrow",
        "checkcast",
        "instanceof",
        "monitorenter",
        "monitorexit",
        "wide",
        "multianewarray",
        "ifnull",
        "ifnonnull",
        "goto_w",
        "jsr_w",
        "breakpoint",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "reserved",
        "impdep1",
        "impdep2"
    };
    public static final int CALC_FROM_CONST_POOL = 0xBEEF;


    private  static Hashtable hash=new Hashtable(257);

    public static void initOpcode(){
        hash.put("nop" , new Integer(0) );
        hash.put("aconst_null" , new Integer(1) );
        hash.put("iconst_m1" , new Integer(2) );
        hash.put("iconst_0" , new Integer(3) );
        hash.put("iconst_1" , new Integer(4) );
        hash.put("iconst_2" , new Integer(5) );
        hash.put("iconst_3" , new Integer(6) );
        hash.put("iconst_4" , new Integer(7) );
        hash.put("iconst_5" , new Integer(8) );
        hash.put("lconst_0" , new Integer(9) );
        hash.put("lconst_1" , new Integer(10) );
        hash.put("fconst_0" , new Integer(11) );
        hash.put("fconst_1" , new Integer(12) );
        hash.put("fconst_2" , new Integer(13) );
        hash.put("dconst_0" , new Integer(14) );
        hash.put("dconst_1" , new Integer(15) );
        hash.put("bipush" , new Integer(16) );
        hash.put("sipush" , new Integer(17) );
        hash.put("ldc" , new Integer(18) );
        hash.put("ldc_w" , new Integer(19) );
        hash.put("ldc2_w" , new Integer(20) );
        hash.put("iload" , new Integer(21) );
        hash.put("lload" , new Integer(22) );
        hash.put("fload" , new Integer(23) );
        hash.put("dload" , new Integer(24) );
        hash.put("aload" , new Integer(25) );
        hash.put("iload_0" , new Integer(26) );
        hash.put("iload_1" , new Integer(27) );
        hash.put("iload_2" , new Integer(28) );
        hash.put("iload_3" , new Integer(29) );
        hash.put("lload_0" , new Integer(30) );
        hash.put("lload_1" , new Integer(31) );
        hash.put("lload_2" , new Integer(32) );
        hash.put("lload_3" , new Integer(33) );
        hash.put("fload_0" , new Integer(34) );
        hash.put("fload_1" , new Integer(35) );
        hash.put("fload_2" , new Integer(36) );
        hash.put("fload_3" , new Integer(37) );
        hash.put("dload_0" , new Integer(38) );
        hash.put("dload_1" , new Integer(39) );
        hash.put("dload_2" , new Integer(40) );
        hash.put("dload_3" , new Integer(41) );
        hash.put("aload_0" , new Integer(42) );
        hash.put("aload_1" , new Integer(43) );
        hash.put("aload_2" , new Integer(44) );
        hash.put("aload_3" , new Integer(45) );
        hash.put("iaload" , new Integer(46) );
        hash.put("laload" , new Integer(47) );
        hash.put("faload" , new Integer(48) );
        hash.put("daload" , new Integer(49) );
        hash.put("aaload" , new Integer(50) );
        hash.put("baload" , new Integer(51) );
        hash.put("caload" , new Integer(52) );
        hash.put("saload" , new Integer(53) );
        hash.put("istore" , new Integer(54) );
        hash.put("lstore" , new Integer(55) );
        hash.put("fstore" , new Integer(56) );
        hash.put("dstore" , new Integer(57) );
        hash.put("astore" , new Integer(58) );
        hash.put("istore_0" , new Integer(59) );
        hash.put("istore_1" , new Integer(60) );
        hash.put("istore_2" , new Integer(61) );
        hash.put("istore_3" , new Integer(62) );
        hash.put("lstore_0" , new Integer(63) );
        hash.put("lstore_1" , new Integer(64) );
        hash.put("lstore_2" , new Integer(65) );
        hash.put("lstore_3" , new Integer(66) );
        hash.put("fstore_0" , new Integer(67) );
        hash.put("fstore_1" , new Integer(68) );
        hash.put("fstore_2" , new Integer(69) );
        hash.put("fstore_3" , new Integer(70) );
        hash.put("dstore_0" , new Integer(71) );
        hash.put("dstore_1" , new Integer(72) );
        hash.put("dstore_2" , new Integer(73) );
        hash.put("dstore_3" , new Integer(74) );
        hash.put("astore_0" , new Integer(75) );
        hash.put("astore_1" , new Integer(76) );
        hash.put("astore_2" , new Integer(77) );
        hash.put("astore_3" , new Integer(78) );
        hash.put("iastore" , new Integer(79) );
        hash.put("lastore" , new Integer(80) );
        hash.put("fastore" , new Integer(81) );
        hash.put("dastore" , new Integer(82) );
        hash.put("aastore" , new Integer(83) );
        hash.put("bastore" , new Integer(84) );
        hash.put("castore" , new Integer(85) );
        hash.put("sastore" , new Integer(86) );
        hash.put("pop" , new Integer(87) );
        hash.put("pop2" , new Integer(88) );
        hash.put("dup" , new Integer(89) );
        hash.put("dup_x1" , new Integer(90) );
        hash.put("dup_x2" , new Integer(91) );
        hash.put("dup2" , new Integer(92) );
        hash.put("dup2_x1" , new Integer(93) );
        hash.put("dup2_x2" , new Integer(94) );
        hash.put("swap" , new Integer(95) );
        hash.put("iadd" , new Integer(96) );
        hash.put("ladd" , new Integer(97) );
        hash.put("fadd" , new Integer(98) );
        hash.put("dadd" , new Integer(99) );
        hash.put("isub" , new Integer(100) );
        hash.put("lsub" , new Integer(101) );
        hash.put("fsub" , new Integer(102) );
        hash.put("dsub" , new Integer(103) );
        hash.put("imul" , new Integer(104) );
        hash.put("lmul" , new Integer(105) );
        hash.put("fmul" , new Integer(106) );
        hash.put("dmul" , new Integer(107) );
        hash.put("idiv" , new Integer(108) );
        hash.put("ldiv" , new Integer(109) );
        hash.put("fdiv" , new Integer(110) );
        hash.put("ddiv" , new Integer(111) );
        hash.put("irem" , new Integer(112) );
        hash.put("lrem" , new Integer(113) );
        hash.put("frem" , new Integer(114) );
        hash.put("drem" , new Integer(115) );
        hash.put("ineg" , new Integer(116) );
        hash.put("lneg" , new Integer(117) );
        hash.put("fneg" , new Integer(118) );
        hash.put("dneg" , new Integer(119) );
        hash.put("ishl" , new Integer(120) );
        hash.put("lshl" , new Integer(121) );
        hash.put("ishr" , new Integer(122) );
        hash.put("lshr" , new Integer(123) );
        hash.put("iushr" , new Integer(124) );
        hash.put("lushr" , new Integer(125) );
        hash.put("iand" , new Integer(126) );
        hash.put("land" , new Integer(127) );
        hash.put("ior" , new Integer(128) );
        hash.put("lor" , new Integer(129) );
        hash.put("ixor" , new Integer(130) );
        hash.put("lxor" , new Integer(131) );
        hash.put("iinc" , new Integer(132) );
        hash.put("i2l" , new Integer(133) );
        hash.put("i2f" , new Integer(134) );
        hash.put("i2d" , new Integer(135) );
        hash.put("l2i" , new Integer(136) );
        hash.put("l2f" , new Integer(137) );
        hash.put("l2d" , new Integer(138) );
        hash.put("f2i" , new Integer(139) );
        hash.put("f2l" , new Integer(140) );
        hash.put("f2d" , new Integer(141) );
        hash.put("d2i" , new Integer(142) );
        hash.put("d2l" , new Integer(143) );
        hash.put("d2f" , new Integer(144) );
        hash.put("i2b" , new Integer(145) );
        hash.put("i2c" , new Integer(146) );
        hash.put("i2s" , new Integer(147) );
        hash.put("lcmp" , new Integer(148) );
        hash.put("fcmpl" , new Integer(149) );
        hash.put("fcmpg" , new Integer(150) );
        hash.put("dcmpl" , new Integer(151) );
        hash.put("dcmpg" , new Integer(152) );
        hash.put("ifeq" , new Integer(153) );
        hash.put("ifne" , new Integer(154) );
        hash.put("iflt" , new Integer(155) );
        hash.put("ifge" , new Integer(156) );
        hash.put("ifgt" , new Integer(157) );
        hash.put("ifle" , new Integer(158) );
        hash.put("if_icmpeq" , new Integer(159) );
        hash.put("if_icmpne" , new Integer(160) );
        hash.put("if_icmplt" , new Integer(161) );
        hash.put("if_icmpge" , new Integer(162) );
        hash.put("if_icmpgt" , new Integer(163) );
        hash.put("if_icmple" , new Integer(164) );
        hash.put("if_acmpeq" , new Integer(165) );
        hash.put("if_acmpne" , new Integer(166) );
        hash.put("goto" , new Integer(167) );
        hash.put("jsr" , new Integer(168) );
        hash.put("ret" , new Integer(169) );
        hash.put("tableswitch" , new Integer(170) );
        hash.put("lookupswitch" , new Integer(171) );
        hash.put("ireturn" , new Integer(172) );
        hash.put("lreturn" , new Integer(173) );
        hash.put("freturn" , new Integer(174) );
        hash.put("dreturn" , new Integer(175) );
        hash.put("areturn" , new Integer(176) );
        hash.put("return" , new Integer(177) );
        hash.put("getstatic" , new Integer(178) );
        hash.put("putstatic" , new Integer(179) );
        hash.put("getfield" , new Integer(180) );
        hash.put("putfield" , new Integer(181) );
        hash.put("invokevirtual" , new Integer(182) );
        hash.put("invokespecial" , new Integer(183) );
        hash.put("invokestatic" , new Integer(184) );
        hash.put("invokeinterface" , new Integer(185) );
        hash.put("xxxunusedxxx" , new Integer(186) );
        hash.put("new" , new Integer(187) );
        hash.put("newarray" , new Integer(188) );
        hash.put("anewarray" , new Integer(189) );
        hash.put("arraylength" , new Integer(190) );
        hash.put("athrow" , new Integer(191) );
        hash.put("checkcast" , new Integer(192) );
        hash.put("instanceof" , new Integer(193) );
        hash.put("monitorenter" , new Integer(194) );
        hash.put("monitorexit" , new Integer(195) );
        hash.put("wide" , new Integer(196) );
        hash.put("multianewarray" , new Integer(197) );
        hash.put("ifnull" , new Integer(198) );
        hash.put("ifnonnull" , new Integer(199) );
        hash.put("goto_w" , new Integer(200) );
        hash.put("jsr_w" , new Integer(201) );
        hash.put("breakpoint" , new Integer(202) );
        hash.put("impdep1" , new Integer(254) );
        hash.put("impdep" , new Integer(255) );
        hash.put("wide&iinc",new Integer(WIDE) );
    }

    public static String toString(int value) {
        return opcodes[value];
    }
    public static int toCode(String key){
        try{
            return Integer.parseInt( hash.get(key).toString() );
        }catch(Exception e){
            return 0;
        }
    }
    public static boolean isField(int code)
    {
        switch(code){
            case PUTFIELD:
            case GETFIELD:
            case PUTSTATIC:
            case GETSTATIC:
                return true;
            default:
                return false;
        }
    }
    public static boolean isMethod(int code)
    {
        switch(code){
            case INVOKESTATIC:
            case INVOKEVIRTUAL:
            case INVOKESPECIAL:
                return true;
            default:
                return false;
        }
    }

    public static boolean isBranchInstruction(int code) {
        switch (code) {
            case IFEQ:
            case IFNE:
            case IFLT:
            case IFGE:
            case IFGT:
            case IFLE:
            case IF_ICMPEQ:
            case IF_ICMPNE:
            case IF_ICMPLT:
            case IF_ICMPGE:
            case IF_ICMPGT:
            case IF_ICMPLE:
            case IF_ACMPEQ:
            case IF_ACMPNE:
            case IFNULL:
            case IFNONNULL:
            case GOTO:
                return true;

            default:
                return false;
//         case GOTO:
//         case JSR:
//         case RET:
//         case GOTO_W:
//         case JSR_W:
        }
    }

    public static boolean isReturn(int code) {
        switch (code) {
            case IRETURN:
            case LRETURN:
            case FRETURN:
            case DRETURN:
            case ARETURN:
            case RETURN:
            case ATHROW:
                return true;
            default:
                return false;
        }
    }

    public static int getJumpOffset(int[] code, int i) {
        switch (code[i]) {
            case IFEQ:
            case IFNE:
            case IFLT:
            case IFGE:
            case IFGT:
            case IFLE:
            case IF_ICMPEQ:
            case IF_ICMPNE:
            case IF_ICMPLT:
            case IF_ICMPGE:
            case IF_ICMPGT:
            case IF_ICMPLE:
            case IF_ACMPEQ:
            case IF_ACMPNE:
            case IFNULL:
            case IFNONNULL:
                return (short) ((code[i + 1] << 8) | code[i + 2]);

            case GOTO:
            case JSR:
                return (short) ((code[i + 1] << 8) | code[i + 2]);

            case GOTO_W:
            case JSR_W:
                return (code[i + 1] << 24) | (code[i + 2] << 16) | (code[i + 3] << 8) | code[i + 4];

            default:
                return 0;
        }
    }

    public static int getStackDelta(int[] code, int i) {
        switch (code[i]) {
            case AALOAD:
                return -1;
            case AASTORE:
                return -3;
            case ACONST_NULL:
                return +1;
            case ALOAD:
                return +1;
            case ALOAD_0:
                return +1;
            case ALOAD_1:
                return +1;
            case ALOAD_2:
                return +1;
            case ALOAD_3:
                return +1;
            case ANEWARRAY:
                return 0;
            case ARRAYLENGTH:
                return 0;
            case ASTORE:
                return -1;
            case ASTORE_0:
                return -1;
            case ASTORE_1:
                return -1;
            case ASTORE_2:
                return -1;
            case ASTORE_3:
                return -1;
            case ATHROW:
                // technically this isn't true, but I'm not sure what is...
                return 0;
            case BALOAD:
                return -1;
            case BASTORE:
                return -3;
            case BIPUSH:
                return +1;
            case CALOAD:
                return -1;
            case CASTORE:
                return -3;
            case CHECKCAST:
                return 0;
            case D2F:
                return -1;
            case D2I:
                return -1;
            case D2L:
                return 0;
            case DADD:
                return -2;
            case DALOAD:
                return 0;
            case DASTORE:
                return -4;
            case DCMPG:
                return -3;
            case DCMPL:
                return -3;
            case DCONST_0:
                return +2;
            case DCONST_1:
                return +2;
            case DDIV:
                return -2;
            case DLOAD:
                return +2;
            case DLOAD_0:
                return +2;
            case DLOAD_1:
                return +2;
            case DLOAD_2:
                return +2;
            case DLOAD_3:
                return +2;
            case DMUL:
                return -2;
            case DNEG:
                return 0;
            case DREM:
                return -2;
            case DSTORE:
                return -2;
            case DSTORE_0:
                return -2;
            case DSTORE_1:
                return -2;
            case DSTORE_2:
                return -2;
            case DSTORE_3:
                return -2;
            case DSUB:
                return -2;
            case DUP2:
                return +2;
            case DUP2_X1:
                return +2;
            case DUP2_X2:
                return +2;
            case DUP:
                return +1;
            case DUP_X1:
                return +1;
            case DUP_X2:
                return +1;
            case F2D:
                return +1;
            case F2I:
                return 0;
            case F2L:
                return +1;
            case FADD:
                return -1;
            case FALOAD:
                return -1;
            case FASTORE:
                return -3;
            case FCMPG:
                return -1;
            case FCMPL:
                return -1;
            case FCONST_0:
                return +1;
            case FCONST_1:
                return +1;
            case FCONST_2:
                return +1;
            case FDIV:
                return -1;
            case FLOAD:
                return +1;
            case FLOAD_0:
                return +1;
            case FLOAD_1:
                return +1;
            case FLOAD_2:
                return +1;
            case FLOAD_3:
                return +1;
            case FMUL:
                return -1;
            case FNEG:
                return 0;
            case FREM:
                return -1;
            case FSTORE:
                return -1;
            case FSTORE_0:
                return -1;
            case FSTORE_1:
                return -1;
            case FSTORE_2:
                return -1;
            case FSTORE_3:
                return -1;
            case FSUB:
                return -1;
            case GETFIELD:
                return CALC_FROM_CONST_POOL;
            case GETSTATIC:
                return CALC_FROM_CONST_POOL;
            case GOTO:
                return 0;
            case GOTO_W:
                return 0;
            case I2B:
                return 0;
            case I2C:
                return 0;
            case I2D:
                return +1;
            case I2F:
                return 0;
            case I2L:
                return +1;
            case I2S:
                return 0;
            case IADD:
                return -1;
            case IALOAD:
                return -1;
            case IAND:
                return -1;
            case IASTORE:
                return -3;
            case ICONST_0:
                return +1;
            case ICONST_1:
                return +1;
            case ICONST_2:
                return +1;
            case ICONST_3:
                return +1;
            case ICONST_4:
                return +1;
            case ICONST_5:
                return +1;
            case ICONST_M1:
                return +1;
            case IDIV:
                return -1;
            case IFEQ:
                return -1;
            case IFGE:
                return -1;
            case IFGT:
                return -1;
            case IFLE:
                return -1;
            case IFLT:
                return -1;
            case IFNE:
                return -1;
            case IFNONNULL:
                return -1;
            case IFNULL:
                return -1;
            case IF_ACMPEQ:
                return -2;
            case IF_ACMPNE:
                return -2;
            case IF_ICMPEQ:
                return -2;
            case IF_ICMPGE:
                return -2;
            case IF_ICMPGT:
                return -2;
            case IF_ICMPLE:
                return -2;
            case IF_ICMPLT:
                return -2;
            case IF_ICMPNE:
                return -2;
            case IINC:
                return 0;
            case ILOAD:
                return +1;
            case ILOAD_0:
                return +1;
            case ILOAD_1:
                return +1;
            case ILOAD_2:
                return +1;
            case ILOAD_3:
                return +1;
            case IMUL:
                return -1;
            case INEG:
                return 0;
            case INSTANCEOF:
                return 0;
            case INVOKEINTERFACE:
                return CALC_FROM_CONST_POOL;
//         case INVOKEINTERFACE:
//             return -(code[i+3] + 1);
            case INVOKESPECIAL:
                return CALC_FROM_CONST_POOL;
            case INVOKESTATIC:
                return CALC_FROM_CONST_POOL;
            case INVOKEVIRTUAL:
                return CALC_FROM_CONST_POOL;
            case IOR:
                return -1;
            case IREM:
                return -1;
            case ISHL:
                return -1;
            case ISHR:
                return -1;
            case ISTORE:
                return -1;
            case ISTORE_0:
                return -1;
            case ISTORE_1:
                return -1;
            case ISTORE_2:
                return -1;
            case ISTORE_3:
                return -1;
            case ISUB:
                return -1;
            case IUSHR:
                return -1;
            case IXOR:
                return -1;
            case JSR:
                return +1;
            case JSR_W:
                return +1;
            case L2D:
                return 0;
            case L2F:
                return -1;
            case L2I:
                return -1;
            case LADD:
                return -2;
            case LALOAD:
                return 0;
            case LAND:
                return -2;
            case LASTORE:
                return -4;
            case LCMP:
                return -3;
            case LCONST_0:
                return +2;
            case LCONST_1:
                return +2;
            case LDC2_W:
                return +2;
            case LDC:
                return +1;
            case LDC_W:
                return +1;
            case LDIV:
                return -2;
            case LLOAD:
                return +2;
            case LLOAD_0:
                return +2;
            case LLOAD_1:
                return +2;
            case LLOAD_2:
                return +2;
            case LLOAD_3:
                return +2;
            case LMUL:
                return -2;
            case LNEG:
                return 0;
            case LOOKUPSWITCH:
                return -1;
            case LOR:
                return -2;
            case LREM:
                return -2;
            case LSHL:
                return -1;
            case LSHR:
                return -1;
            case LSTORE:
                return -2;
            case LSTORE_0:
                return -2;
            case LSTORE_1:
                return -2;
            case LSTORE_2:
                return -2;
            case LSTORE_3:
                return -2;
            case LSUB:
                return -2;
            case LUSHR:
                return -1;
            case LXOR:
                return -2;
            case MONITORENTER:
                return -1;
            case MONITOREXIT:
                return -1;
            case MULTIANEWARRAY:
                return 1 - code[i + 3];
            case NEW:
                return +1;
            case NEWARRAY:
                return 0;
            case NOP:
                return 0;
            case POP2:
                return -2;
            case POP:
                return -1;
            case PUTFIELD:
                return CALC_FROM_CONST_POOL;
            case PUTSTATIC:
                return CALC_FROM_CONST_POOL;
            case RET:
                return 0;
            case SALOAD:
                return -1;
            case SASTORE:
                return -3;
            case SIPUSH:
                return +1;
            case SWAP:
                return 0;
            case TABLESWITCH:
                return -1;
            case WIDE:
                return getStackDelta(code, i + 1);
            // all returns actually flatten the stack completely....
            case ARETURN:
            case FRETURN:
            case IRETURN:
                return -1;
            case DRETURN:
            case LRETURN:
                return -2;
            case RETURN:
                return 0;
            case BREAKPOINT:
            case IMPDEP1:
            case IMPDEP2:
            case XXXUNUSEDXXX:
            default:
                return 0;
        }
    }

    public static int getLocalVariableAccess(int[] code, int i) {
        switch (code[i]) {
            case ALOAD:
                return code[i + 1];
            case ALOAD_0:
                return 0;
            case ALOAD_1:
                return 1;
            case ALOAD_2:
                return 2;
            case ALOAD_3:
                return 3;
            case ASTORE:
                return code[i + 1];
            case ASTORE_0:
                return 0;
            case ASTORE_1:
                return 1;
            case ASTORE_2:
                return 2;
            case ASTORE_3:
                return 3;
            case DLOAD:
                return code[i + 1] + 1;
            case DLOAD_0:
                return 1;
            case DLOAD_1:
                return 2;
            case DLOAD_2:
                return 3;
            case DLOAD_3:
                return 4;
            case DSTORE:
                return code[i + 1] + 1;
            case DSTORE_0:
                return 1;
            case DSTORE_1:
                return 2;
            case DSTORE_2:
                return 3;
            case DSTORE_3:
                return 4;
            case FLOAD:
                return code[i + 1];
            case FLOAD_0:
                return 0;
            case FLOAD_1:
                return 1;
            case FLOAD_2:
                return 2;
            case FLOAD_3:
                return 3;
            case FSTORE:
                return code[i + 1];
            case FSTORE_0:
                return 0;
            case FSTORE_1:
                return 1;
            case FSTORE_2:
                return 2;
            case FSTORE_3:
                return 3;
            case ILOAD:
                return code[i + 1];
            case ILOAD_0:
                return 0;
            case ILOAD_1:
                return 1;
            case ILOAD_2:
                return 2;
            case ILOAD_3:
                return 3;
            case ISTORE:
                return code[i + 1];
            case ISTORE_0:
                return 0;
            case ISTORE_1:
                return 1;
            case ISTORE_2:
                return 2;
            case ISTORE_3:
                return 3;
            case LLOAD:
                return code[i + 1] + 1;
            case LLOAD_0:
                return 1;
            case LLOAD_1:
                return 2;
            case LLOAD_2:
                return 3;
            case LLOAD_3:
                return 4;
            case LSTORE:
                return code[i + 1] + 1;
            case LSTORE_0:
                return 1;
            case LSTORE_1:
                return 2;
            case LSTORE_2:
                return 3;
            case LSTORE_3:
                return 4;
            case IINC:
                return code[i + 1];
            case RET:
                return code[i + 1];
            default:
                return 0;
        }
    }

    public static int getConstantPoolIndexSize(int code) {
        switch (code) {
            case LDC:
                return 1;

            case ANEWARRAY:
            case CHECKCAST:
            case GETFIELD:
            case GETSTATIC:
            case INSTANCEOF:
            case INVOKEINTERFACE:
            case INVOKESPECIAL:
            case INVOKESTATIC:
            case INVOKEVIRTUAL:
            case LDC2_W:
            case LDC_W:
            case MULTIANEWARRAY:
            case NEW:
            case PUTFIELD:
            case PUTSTATIC:
                return 2;

            default:
                return 0;
        }
    }
    public static int getOpcodeLengthNew(int code){
        int[] i=new int[1];
        i[0]=code;
        return getOpcodeLengthNew(i,0);
    }

    public static int getOpcodeLengthNew(int[] code, int i) {
        switch (code[i]) {
            case AALOAD:
            case AASTORE:
            case ACONST_NULL:
            case ALOAD_0:
            case ALOAD_1:
            case ALOAD_2:
            case ALOAD_3:
            case ARETURN:
            case ARRAYLENGTH:
            case ASTORE_0:
            case ASTORE_1:
            case ASTORE_2:
            case ASTORE_3:
            case ATHROW:
            case BALOAD:
            case BASTORE:
            case CALOAD:
            case CASTORE:
            case D2F:
            case D2I:
            case D2L:
            case DADD:
            case DALOAD:
            case DASTORE:
            case DCMPG:
            case DCMPL:
            case DCONST_0:
            case DCONST_1:
            case DDIV:
            case DLOAD_0:
            case DLOAD_1:
            case DLOAD_2:
            case DLOAD_3:
            case DMUL:
            case DNEG:
            case DREM:
            case DRETURN:
            case DSTORE_0:
            case DSTORE_1:
            case DSTORE_2:
            case DSTORE_3:
            case DSUB:
            case DUP2:
            case DUP2_X1:
            case DUP2_X2:
            case DUP:
            case DUP_X1:
            case DUP_X2:
            case F2D:
            case F2I:
            case F2L:
            case FADD:
            case FALOAD:
            case FASTORE:
            case FCMPG:
            case FCMPL:
            case FCONST_0:
            case FCONST_1:
            case FCONST_2:
            case FDIV:
            case FLOAD_0:
            case FLOAD_1:
            case FLOAD_2:
            case FLOAD_3:
            case FMUL:
            case FNEG:
            case FREM:
            case FRETURN:
            case FSTORE_0:
            case FSTORE_1:
            case FSTORE_2:
            case FSTORE_3:
            case FSUB:
            case I2B:
            case I2C:
            case I2D:
            case I2F:
            case I2L:
            case I2S:
            case IADD:
            case IALOAD:
            case IAND:
            case IASTORE:
            case ICONST_0:
            case ICONST_1:
            case ICONST_2:
            case ICONST_3:
            case ICONST_4:
            case ICONST_5:
            case ICONST_M1:
            case IDIV:
            case ILOAD_0:
            case ILOAD_1:
            case ILOAD_2:
            case ILOAD_3:
            case IMUL:
            case INEG:
            case IOR:
            case IREM:
            case IRETURN:
            case ISHL:
            case ISHR:
            case ISTORE_0:
            case ISTORE_1:
            case ISTORE_2:
            case ISTORE_3:
            case ISUB:
            case IUSHR:
            case IXOR:
            case L2D:
            case L2F:
            case L2I:
            case LADD:
            case LALOAD:
            case LAND:
            case LASTORE:
            case LCMP:
            case LCONST_0:
            case LCONST_1:
            case LDIV:
            case LLOAD_0:
            case LLOAD_1:
            case LLOAD_2:
            case LLOAD_3:
            case LMUL:
            case LNEG:
            case LOR:
            case LREM:
            case LRETURN:
            case LSHL:
            case LSHR:
            case LSTORE_0:
            case LSTORE_1:
            case LSTORE_2:
            case LSTORE_3:
            case LSUB:
            case LUSHR:
            case LXOR:
            case MONITORENTER:
            case MONITOREXIT:
            case NOP:
            case POP2:
            case POP:
            case RETURN:
            case SALOAD:
            case SASTORE:
            case SWAP:
                return 1;
            case ALOAD:
            case ASTORE:
            case BIPUSH:
            case DLOAD:
            case DSTORE:
            case FLOAD:
            case FSTORE:
            case ILOAD:
            case ISTORE:
            case LDC:
            case LLOAD:
            case LSTORE:
            case NEWARRAY:
            case RET:
                return 2;
            case ANEWARRAY:
            case CHECKCAST:
            case GETFIELD:
            case GETSTATIC:
            case GOTO:
            case IFEQ:
            case IFGE:
            case IFGT:
            case IFLE:
            case IFLT:
            case IFNE:
            case IFNONNULL:
            case IFNULL:
            case IF_ACMPEQ:
            case IF_ACMPNE:
            case IF_ICMPEQ:
            case IF_ICMPGE:
            case IF_ICMPGT:
            case IF_ICMPLE:
            case IF_ICMPLT:
            case IF_ICMPNE:
            case IINC:
            case INSTANCEOF:
            case INVOKESPECIAL:
            case INVOKESTATIC:
            case INVOKEVIRTUAL:
            case JSR:
            case LDC2_W:
            case LDC_W:
            case NEW:
            case PUTFIELD:
            case PUTSTATIC:
            case SIPUSH:
                return 3;
            case MULTIANEWARRAY:
                return 4;
            case GOTO_W:
            case INVOKEINTERFACE:
            case JSR_W:
                return 5;
            case LOOKUPSWITCH:
                return LOOKUPSWITCH;
            case TABLESWITCH:
                return TABLESWITCH;
            case WIDE:
                return 6;
            default:
                return 1;
        }
    }

    private static int getLookupSwitchLength(int[] code, int i) {
        int initPosition = i;
        // skip the zeros
        for (i = (initPosition + 1); i < (initPosition + 5); i++) {
            if ((i % 4) == 0) {
                break;
            }
        }
        // skip the default byte
        i += 4;
        // read the number of pairs
        int npairs = (code[i] << 24) | (code[i + 1] << 16) | (code[i + 2] << 8) | (code[i + 3]);
        i += 4;
        // skip the pairs
        i += 8 * npairs;

        return i - initPosition;
    }

    private static int getTableSwitchLength(int[] code, int i) {
        int initPosition = i;
        // skip the zeros
        for (i = (initPosition + 1); i < (initPosition + 5); i++) {
            if ((i % 4) == 0) {
                break;
            }
        }
        // skip the default byte
        i += 4;
        // read the lowbyte
        int low = (code[i] << 24) | (code[i + 1] << 16) | (code[i + 2] << 8) | (code[i + 3]);
        i += 4;
        // read the highbyte
        int high = (code[i] << 24) | (code[i + 1] << 16) | (code[i + 2] << 8) | (code[i + 3]);
        i += 4;
        // skip the table
        i += 4 * (high - low + 1);

        return i - initPosition;
    }
    public static int getOpcodeLength(int[] code, int i) {
        switch (code[i]) {
            case AALOAD:
            case AASTORE:
            case ACONST_NULL:
            case ALOAD_0:
            case ALOAD_1:
            case ALOAD_2:
            case ALOAD_3:
            case ARETURN:
            case ARRAYLENGTH:
            case ASTORE_0:
            case ASTORE_1:
            case ASTORE_2:
            case ASTORE_3:
            case ATHROW:
            case BALOAD:
            case BASTORE:
            case CALOAD:
            case CASTORE:
            case D2F:
            case D2I:
            case D2L:
            case DADD:
            case DALOAD:
            case DASTORE:
            case DCMPG:
            case DCMPL:
            case DCONST_0:
            case DCONST_1:
            case DDIV:
            case DLOAD_0:
            case DLOAD_1:
            case DLOAD_2:
            case DLOAD_3:
            case DMUL:
            case DNEG:
            case DREM:
            case DRETURN:
            case DSTORE_0:
            case DSTORE_1:
            case DSTORE_2:
            case DSTORE_3:
            case DSUB:
            case DUP2:
            case DUP2_X1:
            case DUP2_X2:
            case DUP:
            case DUP_X1:
            case DUP_X2:
            case F2D:
            case F2I:
            case F2L:
            case FADD:
            case FALOAD:
            case FASTORE:
            case FCMPG:
            case FCMPL:
            case FCONST_0:
            case FCONST_1:
            case FCONST_2:
            case FDIV:
            case FLOAD_0:
            case FLOAD_1:
            case FLOAD_2:
            case FLOAD_3:
            case FMUL:
            case FNEG:
            case FREM:
            case FRETURN:
            case FSTORE_0:
            case FSTORE_1:
            case FSTORE_2:
            case FSTORE_3:
            case FSUB:
            case I2B:
            case I2C:
            case I2D:
            case I2F:
            case I2L:
            case I2S:
            case IADD:
            case IALOAD:
            case IAND:
            case IASTORE:
            case ICONST_0:
            case ICONST_1:
            case ICONST_2:
            case ICONST_3:
            case ICONST_4:
            case ICONST_5:
            case ICONST_M1:
            case IDIV:
            case ILOAD_0:
            case ILOAD_1:
            case ILOAD_2:
            case ILOAD_3:
            case IMUL:
            case INEG:
            case IOR:
            case IREM:
            case IRETURN:
            case ISHL:
            case ISHR:
            case ISTORE_0:
            case ISTORE_1:
            case ISTORE_2:
            case ISTORE_3:
            case ISUB:
            case IUSHR:
            case IXOR:
            case L2D:
            case L2F:
            case L2I:
            case LADD:
            case LALOAD:
            case LAND:
            case LASTORE:
            case LCMP:
            case LCONST_0:
            case LCONST_1:
            case LDIV:
            case LLOAD_0:
            case LLOAD_1:
            case LLOAD_2:
            case LLOAD_3:
            case LMUL:
            case LNEG:
            case LOR:
            case LREM:
            case LRETURN:
            case LSHL:
            case LSHR:
            case LSTORE_0:
            case LSTORE_1:
            case LSTORE_2:
            case LSTORE_3:
            case LSUB:
            case LUSHR:
            case LXOR:
            case MONITORENTER:
            case MONITOREXIT:
            case NOP:
            case POP2:
            case POP:
            case RETURN:
            case SALOAD:
            case SASTORE:
            case SWAP:
                return 1;
            case ALOAD:
            case ASTORE:
            case BIPUSH:
            case DLOAD:
            case DSTORE:
            case FLOAD:
            case FSTORE:
            case ILOAD:
            case ISTORE:
            case LDC:
            case LLOAD:
            case LSTORE:
            case NEWARRAY:
            case RET:
                return 2;
            case ANEWARRAY:
            case CHECKCAST:
            case GETFIELD:
            case GETSTATIC:
            case GOTO:
            case IFEQ:
            case IFGE:
            case IFGT:
            case IFLE:
            case IFLT:
            case IFNE:
            case IFNONNULL:
            case IFNULL:
            case IF_ACMPEQ:
            case IF_ACMPNE:
            case IF_ICMPEQ:
            case IF_ICMPGE:
            case IF_ICMPGT:
            case IF_ICMPLE:
            case IF_ICMPLT:
            case IF_ICMPNE:
            case IINC:
            case INSTANCEOF:
            case INVOKESPECIAL:
            case INVOKESTATIC:
            case INVOKEVIRTUAL:
            case JSR:
            case LDC2_W:
            case LDC_W:
            case NEW:
            case PUTFIELD:
            case PUTSTATIC:
            case SIPUSH:
                return 3;
            case MULTIANEWARRAY:
                return 4;
            case GOTO_W:
            case INVOKEINTERFACE:
            case JSR_W:
                return 5;
            case LOOKUPSWITCH:
                return getLookupSwitchLength(code, i);
            case TABLESWITCH:
                return getTableSwitchLength(code, i);
            case WIDE:
                if (code[i + 1] == IINC) {
                    return 6;
                }
                return 4;
            default:
                return 1;
        }
    }


    /*
    case NOP:
    case ACONST_NULL:
    case ICONST_M1:
    case ICONST_0:
    case ICONST_1:
    case ICONST_2:
    case ICONST_3:
    case ICONST_4:
    case ICONST_5:
    case LCONST_0:
    case LCONST_1:
    case FCONST_0:
    case FCONST_1:
    case FCONST_2:
    case DCONST_0:
    case DCONST_1:
    case BIPUSH:
    case SIPUSH:
    case LDC:
    case LDC_W:
    case LDC2_W:
    case ILOAD:
    case LLOAD:
    case FLOAD:
    case DLOAD:
    case ALOAD:
    case ILOAD_0:
    case ILOAD_1:
    case ILOAD_2:
    case ILOAD_3:
    case LLOAD_0:
    case LLOAD_1:
    case LLOAD_2:
    case LLOAD_3:
    case FLOAD_0:
    case FLOAD_1:
    case FLOAD_2:
    case FLOAD_3:
    case DLOAD_0:
    case DLOAD_1:
    case DLOAD_2:
    case DLOAD_3:
    case ALOAD_0:
    case ALOAD_1:
    case ALOAD_2:
    case ALOAD_3:
    case IALOAD:
    case LALOAD:
    case FALOAD:
    case DALOAD:
    case AALOAD:
    case BALOAD:
    case CALOAD:
    case SALOAD:
    case ISTORE:
    case LSTORE:
    case FSTORE:
    case DSTORE:
    case ASTORE:
    case ISTORE_0:
    case ISTORE_1:
    case ISTORE_2:
    case ISTORE_3:
    case LSTORE_0:
    case LSTORE_1:
    case LSTORE_2:
    case LSTORE_3:
    case FSTORE_0:
    case FSTORE_1:
    case FSTORE_2:
    case FSTORE_3:
    case DSTORE_0:
    case DSTORE_1:
    case DSTORE_2:
    case DSTORE_3:
    case ASTORE_0:
    case ASTORE_1:
    case ASTORE_2:
    case ASTORE_3:
    case IASTORE:
    case LASTORE:
    case FASTORE:
    case DASTORE:
    case AASTORE:
    case BASTORE:
    case CASTORE:
    case SASTORE:
    case POP:
    case POP2:
    case DUP:
    case DUP_X1:
    case DUP_X2:
    case DUP2:
    case DUP2_X1:
    case DUP2_X2:
    case SWAP:
    case IADD:
    case LADD:
    case FADD:
    case DADD:
    case ISUB:
    case LSUB:
    case FSUB:
    case DSUB:
    case IMUL:
    case LMUL:
    case FMUL:
    case DMUL:
    case IDIV:
    case LDIV:
    case FDIV:
    case DDIV:
    case IREM:
    case LREM:
    case FREM:
    case DREM:
    case INEG:
    case LNEG:
    case FNEG:
    case DNEG:
    case ISHL:
    case LSHL:
    case ISHR:
    case LSHR:
    case IUSHR:
    case LUSHR:
    case IAND:
    case LAND:
    case IOR:
    case LOR:
    case IXOR:
    case LXOR:
    case IINC:
    case I2L:
    case I2F:
    case I2D:
    case L2I:
    case L2F:
    case L2D:
    case F2I:
    case F2L:
    case F2D:
    case D2I:
    case D2L:
    case D2F:
    case I2B:
    case I2C:
    case I2S:
    case LCMP:
    case FCMPL:
    case FCMPG:
    case DCMPL:
    case DCMPG:
    case IFEQ:
    case IFNE:
    case IFLT:
    case IFGE:
    case IFGT:
    case IFLE:
    case IF_ICMPEQ:
    case IF_ICMPNE:
    case IF_ICMPLT:
    case IF_ICMPGE:
    case IF_ICMPGT:
    case IF_ICMPLE:
    case IF_ACMPEQ:
    case IF_ACMPNE:
    case GOTO:
    case JSR:
    case RET:
    case TABLESWITCH:
    case LOOKUPSWITCH:
    case IRETURN:
    case LRETURN:
    case FRETURN:
    case DRETURN:
    case ARETURN:
    case RETURN:
    case GETSTATIC:
    case PUTSTATIC:
    case GETFIELD:
    case PUTFIELD:
    case INVOKEVIRTUAL:
    case INVOKESPECIAL:
    case INVOKESTATIC:
    case INVOKEINTERFACE:
    case XXXUNUSEDXXX:
    case NEW:
    case NEWARRAY:
    case ANEWARRAY:
    case ARRAYLENGTH:
    case ATHROW:
    case CHECKCAST:
    case INSTANCEOF:
    case MONITORENTER:
    case MONITOREXIT:
    case WIDE:
    case MULTIANEWARRAY:
    case IFNULL:
    case IFNONNULL:
    case GOTO_W:
    case JSR_W:
    case BREAKPOINT:
    case IMPDEP1:
    case IMPDEP2:
     */
}
