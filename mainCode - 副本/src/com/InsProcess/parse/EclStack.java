package com.InsProcess.parse;

import java.util.HashMap;

import com.badlogic.gdx.math.RandomXS128;
import com.meng.TaiHunDanmaku.ui.FightScreen;
import com.meng.TaiHunDanmaku.ui.GameMain;

public class EclStack implements Cloneable{

    private final int maxDepth = 32;
    private int depth = 0;
    private int[] stack = new int[maxDepth];
    private int[] varArray;
    private static HashMap<Integer, Integer> values = new HashMap<>();
    private RandomXS128 random=new RandomXS128();

    @Override
    public EclStack clone() throws CloneNotSupportedException { 
    	EclStack stack=(EclStack) super.clone();
    	stack.values=(HashMap<Integer, Integer>) values.clone();
    	return stack;
    }
    
    public void initVarSize(int size, int[] bs) {
        if (size != 0) {
        	size/=4;
            varArray = new int[size];
            if(bs.length>0){
            	for (int i =0; i < bs.length; ++i) {
            		varArray[i] = bs[i];
            	}
            }
        }
        values.put(-9949, 0);
        values.put(-9948, 0);
        values.put(-9947, 1);
        //	values.put(-9985,0);
        values.put(-971246592, 0);
    }

    public void dec(int pos) {
        --varArray[pos / 4];
    }

    public int getInt(int i) {
        if (i >= 0) {
            return varArray[i / 4];
        } else {
            switch (i) {
            case -1:
            case -2:
                    return popInt();
                case -9904:
                case -9907:
                    return -1;
                case -9947:
                case -9948:
                case -9949:
                    return values.get(i);
                case -9954:
                    return (int) FightScreen.instence.boss.hp;
                case -9959:
                    return GameMain.difficulty;
                case -9978:
                case -9979:
                case -9980:
                case -9985:
                    return values.get(i);
                case -9986:
                    return 0;
                case -9988:
                    return FightScreen.instence.gameTimeFlag;
                case -10000:
                    return random.nextInt();
                default:
                    throw new NullPointerException("unexpect value:" + i);
            }
        }
    }

    public float getFloat(int intBytes) {
        int fvalue = (int) Float.intBitsToFloat(intBytes);
        if (fvalue >= 0) {
            return Float.intBitsToFloat(varArray[fvalue / 4]);
        } else {
            switch (intBytes) {
                case -1082130432: // -1.0
                case -1073741824: // -2.0
                    return popFloat();
                case -971314176: // -9915.0
                    break;
                case -971313152: // -9916.0
                    break;
                case -971312128: // -9917.0
                    break;
                case -971311104: // -9918.0
                    break;
                case -971310080: // -9919.0
                    break;
                case -971309056: // -9920.0
                    break;
                case -971308032: // -9921.0
                    break;
                case -971307008: // -9922.0
                    break;
                case -971296768: // -9932.0
                    break;
                case -971295744: // -9933.0
                    break;
                case -971294720: // -9934.0
                    break;
                case -971293696: // -9935.0
                    break;
                case -971292672: // -9936.0
                    break;
                case -971291648: // -9937.0
                    break;
                case -971290624: // -9938.0
                    break;
                case -971289600: // -9939.0
                    break;
                case -971284480: // -9944.0
                    break;
                case -971273216: // -9955.0
                    break;
                case -971272192: // -9956.0
                    break;
                case -971270144: // -9958.0
                    break;
                case -971266048: // -9962.0
                    break;
                case -971265024: // -9963.0
                    break;
                case -971264000: // -9964.0
                    break;
                case -971262976: // -9965.0
                    break;
                case -971261952: // -9966.0
                    break;
                case -971260928: // -9967.0
                    break;
                case -971259904: // -9968.0
                    break;
                case -971258880: // -9969.0
                    break;
                case -971257856: // -9970.0
                    break;
                case -971256832: // -9971.0
                    break;
                case -971255808: // -9972.0
                    break;
                case -971254784: // -9973.0
                    break;
                case -971253760: // -9974.0
                    break;
                case -971252736: // -9975.0
                    break;
                case -971251712: // -9976.0
                    break;
                case -971250688: // -9977.0
                    break;
                case -971249664: // -9978.0
                    break;
                case -971248640: // -9979.0
                    break;
                case -971247616: // -9980.0
                    break;
                case -971246592: // -9981.0
                    return Float.intBitsToFloat(values.get(intBytes));
                case -971240448: // -9987.0
                    return random.nextFloat() * 2f - 1f;
                case -971238400: // -9989.0
                    return 1.5f;
                case -971237376: // -9990.0
                    break;
                case -971236352: // -9991.0
                    break;
                case -971235328: // -9992.0
                    break;
                case -971234304: // -9993.0
                    break;
                case -971233280: // -9994.0
                    break;
                case -971232256: // -9995.0
                    break;
                case -971231232: // -9996.0
                    break;
                case -971230208: // -9997.0
                    break;
                case -971229184: // -9998.0
                    return random.nextFloat() * 6.28f - 3.14f;
                case -971228160: // -9999.0
                    break;
                default:
                    throw new NullPointerException("unexpect value:" + Float.intBitsToFloat(intBytes) + " bytes:" + intBytes);
            }
            throw new NullPointerException("unexpect value:" + intBytes);
        }
    }

    public void putGlobal(int key, int value) {
        if (key >= 0) {
            varArray[key / 4] = value;
        } else {
            values.put(key, value);
        }
    }

    public void putGlobal(int key, float v) {
        int value = Float.floatToIntBits(v);
        if (key >= 0) {
            varArray[key / 4] = value;
        } else {
            values.put(key, value);
        }
    }

    public void push(int n) {
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
        }
        stack[depth++] = n;
    }

    public void push(float n) {
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
        }
        stack[depth++] = Float.floatToIntBits(n);
    }

    public int popInt() {
        if (depth == 0) {
            throw new RuntimeException("stack blank");
        }
        return stack[--depth];
    }

    public float popFloat() {
        if (depth == 0) {
            throw new RuntimeException("stack blank");
        }
        return Float.intBitsToFloat(stack[--depth]);
    }

    public int peekInt() {
        if (depth == 0) {
            throw new RuntimeException("stack blank");
        }
        return stack[depth - 1];
    }

    public float peekFloat() {
        if (depth == 0) {
            throw new RuntimeException("stack blank");
        }
        return Float.intBitsToFloat(stack[depth - 1]);
    }
}
