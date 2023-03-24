package classfile;

public class JavaCodeAnalyser {

    public static int getMaxStackDepth(int[] code, int start, ClassFile cf) {
        // Note this algorithm only works when jumps are forwards only!!
        int[] delta = new int[code.length];
        for (int i = 0; i < delta.length; i++) {
            delta[i] = Integer.MIN_VALUE;
        }

        int[] depth = new int[code.length];
        for (int i = 0; i < depth.length; i++) {
            depth[i] = Integer.MIN_VALUE;
        }
        for (int i = 0; i < depth.length;) {
            int thisDelta = JavaOpcode.getStackDelta(code, i);
            if (thisDelta == JavaOpcode.CALC_FROM_CONST_POOL) {
                thisDelta = calcStackDeltaFromConstPool(code, i, cf);
            }

            delta[i] = thisDelta;
            i += JavaOpcode.getOpcodeLength(code, i);
        }
        int maxDepth = 0;
        int currentDepth = 0;
        for (int i = start; i < depth.length;) {
            int jumpOffset = JavaOpcode.getJumpOffset(code, i);
            currentDepth += delta[i];
            depth[i] = currentDepth;

            maxDepth = Math.max(maxDepth, currentDepth);

            if (JavaOpcode.isBranchInstruction(code[i])) {
                int target = i + jumpOffset;


                depth[target] = currentDepth + delta[target];
            } else if (jumpOffset != 0) {
                int target = i + jumpOffset;

                depth[target] = currentDepth + delta[target];

                i += JavaOpcode.getOpcodeLength(code, i);

                while (depth[i] == Integer.MIN_VALUE) {
                    i += JavaOpcode.getOpcodeLength(code, i);
                }

                currentDepth = depth[i];
            } else if (JavaOpcode.isReturn(code[i])) {
                i += JavaOpcode.getOpcodeLength(code, i);

                while ((i < depth.length) && (depth[i] == Integer.MIN_VALUE)) {
                    i += JavaOpcode.getOpcodeLength(code, i);
                }

                if (i >= depth.length) {
                    break;
                }

                currentDepth = depth[i];

// 		break;
            }

            i += JavaOpcode.getOpcodeLength(code, i);
        }

        return maxDepth;
    }

    public static int getMaxLocalVariables(int[] code) {
        int currentMax = 0;
        int varAccess = 0;

        int pc = 0;
        while (pc < code.length) {
            varAccess = JavaOpcode.getLocalVariableAccess(code, pc);
            if (varAccess > currentMax) {
                currentMax = varAccess;
            }
            pc += JavaOpcode.getOpcodeLength(code, pc);
        }

        // add one to give size
        return currentMax + 1;
    }

    private static int calcStackDeltaFromConstPool(int[] code, int i, ClassFile classFile) {
        int opcode = code[i];
        int cpIndex = (code[i + 1] << 8) | (code[i + 2]);

        int length = 0;
        switch (opcode) {
            case JavaOpcode.GETFIELD:
            case JavaOpcode.GETSTATIC:
            case JavaOpcode.PUTFIELD:
            case JavaOpcode.PUTSTATIC:
                String fieldDescriptor = classFile.getConstantPoolFieldDescriptor(cpIndex);
                length = classFile.getFieldLength(fieldDescriptor);
                break;
            case JavaOpcode.INVOKESPECIAL:
            case JavaOpcode.INVOKESTATIC:
            case JavaOpcode.INVOKEVIRTUAL:
            case JavaOpcode.INVOKEINTERFACE:
                String methodDescriptor = classFile.getConstantPoolMethodDescriptor(cpIndex);
                length = classFile.getMethodStackDelta(methodDescriptor);
                break;
        }

        switch (opcode) {
            case JavaOpcode.GETFIELD:
                return length - 1;
            case JavaOpcode.GETSTATIC:
                return length;
            case JavaOpcode.INVOKESPECIAL:
            case JavaOpcode.INVOKEVIRTUAL:
            case JavaOpcode.INVOKEINTERFACE:
                return -(length + 1);
            case JavaOpcode.INVOKESTATIC:
            case JavaOpcode.PUTSTATIC:
                return -length;
            case JavaOpcode.PUTFIELD:
                return -(length + 1);
        }

        return 0;
    }
}
