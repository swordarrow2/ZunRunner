package com.meng.zunRunner.ecl.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.meng.zunRunner.ecl.helper.ChangeTask;
import com.meng.zunRunner.ecl.helper.EclBulletShooter;
import com.meng.zunRunner.ecl.helper.MathHelper;
import com.meng.zunRunner.ecl.EclFile;
import com.meng.zunRunner.ecl.EclManager;
import com.meng.zunRunner.ecl.EclStack;
import com.badlogic.gdx.math.Vector2;
import com.meng.gui.planes.Enemy;
import com.meng.gui.task.Task;
import com.meng.gui.task.TaskMoveTo;
import com.meng.gui.ui.FightScreen;
import com.meng.gui.ui.GameMain;

/**
 * @author Administrator th10_sub_t
 */
public class EclSub implements Cloneable {
    public byte[] magic = new byte[4];
    public int data_offset;/* sizeof(th10_sub_t) */
    public int[] zero = new int[2];
    public byte[] data;
    private ArrayList<EclIns> insList = new ArrayList<>();
    private int dataPosition = 0;
    private int insPosition = 0;

    private EclFile eclFile;
    public EclSubPack eclSubPack;
    private EclSub parentSub;
    private int[] tempArg = new int[0];

    private Enemy enemy;

    private EclStack stack = new EclStack();
    private HashMap<Integer, EclBulletShooter> eclBulletShooters = new HashMap<>();

    private int waitFrams = 0;
    private int ins23frame = 0;

    private boolean startByIns11 = false;

    public EclSub(EclFile file, EclSubPack eclSubPack) {
        eclFile = file;
        this.eclSubPack = eclSubPack;
    }

    @Override
    public EclSub clone() throws CloneNotSupportedException {
        EclSub eclSub = (EclSub) super.clone();
        for (int i = 0; i < insList.size(); ++i) {
            eclSub.insList.set(i, insList.get(i).clone());
        }
        eclSub.eclBulletShooters = (HashMap<Integer, EclBulletShooter>) eclBulletShooters.clone();
        eclSub.enemy = enemy.clone();
        eclSub.stack = stack.clone();
        return eclSub;
    }

    public EclSub setManager(Enemy enemy) {
        this.enemy = enemy;
        return this;
    }

    public void readIns() {
        while (dataPosition < data.length) {
            EclIns eclIns = new EclIns(this);
            eclIns.time = (data[dataPosition++] & 0xff) | (data[dataPosition++] & 0xff) << 8
                    | (data[dataPosition++] & 0xff) << 16 | (data[dataPosition++] & 0xff) << 24;
            eclIns.id = (short) (data[dataPosition++] & 0xff | (data[dataPosition++] & 0xff) << 8);
            eclIns.size = (short) (data[dataPosition++] & 0xff | (data[dataPosition++] & 0xff) << 8);
            eclIns.param_mask = (short) (data[dataPosition++] & 0xff | (data[dataPosition++] & 0xff) << 8);
            eclIns.rank_mask = data[dataPosition++];
            eclIns.param_count = data[dataPosition++];
            eclIns.zero = (data[dataPosition++] & 0xff) | (data[dataPosition++] & 0xff) << 8
                    | (data[dataPosition++] & 0xff) << 16 | (data[dataPosition++] & 0xff) << 24;
            int bound = eclIns.size - 16;
            eclIns.data = new byte[bound];
            for (int i = 0; i < bound; ++i) {
                eclIns.data[i] = data[dataPosition];
                ++dataPosition;
            }
            insList.add(eclIns);
        }
    }

    public EclSub setPartent(EclSub parent) {
        parentSub = parent;
        return this;
    }

    public EclSub startByIns11(boolean b) {
        insPosition = 0;
        startByIns11 = b;
        return this;
    }

    public EclSub insertArgs(int... bytes) {
        tempArg = bytes;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (EclIns eclIns : insList) {
            stringBuilder.append("\n").append(eclIns.toString());
        }
        return stringBuilder.toString();
    }

    private void moveToNextInt() {
        if ((dataPosition & 0b11) == 0) {
            return;
        }
        dataPosition |= 0b11;
        ++dataPosition;
    }

    private void gotoIns(int length) {
        if (length > 0) {
            int nowSkipLength = 24;
            while (nowSkipLength != length) {
                nowSkipLength += insList.get(++insPosition).size;
            }
        } else {
            int nowSkipLength = 0;
            while (nowSkipLength != length) {
                nowSkipLength -= insList.get(--insPosition).size;
            }
        }
    }

    public void update() {
        if (enemy.isKilled) {
            enemy = null;
            _10();
            return;
        }
        if (insPosition < insList.size()) {
            EclIns ins = insList.get(insPosition);
            if (((ins.rank_mask >> GameMain.difficulty) & 0b1) == 0) {
                ++insPosition;
                return;
            }
            if (ins.id == 23) {
                if (ins23frame == 0) {
                    ins23frame = (ins.param_mask == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                }
                if (ins23frame < waitFrams++) {
                    waitFrams = 0;
                    ins23frame = 0;
                    ++insPosition;
                }
                return;
            }
            invoke(ins);
            ++insPosition;
            update();
        }
    }

    private void _0() {

    }

    private void _1() {

    }

    public void _10() {
        System.out.println("sub " + eclSubPack.subName + " exit");
        if (startByIns11) {
            EclManager.toAddSubs.add(parentSub);
            EclManager.onPauseSubs.remove(parentSub);
            EclManager.toDeleteSubs.add(this);
        } else {
            EclManager.toDeleteSubs.add(this);
        }
    }

    private void _11(String s0, int[] i1, boolean[] isInt, int paramMask) {
        EclSub toRun = EclManager.getSubPack(s0);
        int[] result = new int[i1.length];
        for (int i = 0; i < result.length; ++i) {
            if (((paramMask >> (i + 1)) & 1) == 1) {
                if (isInt[i]) {
                    result[i] = stack.getInt(i1[i]);
                } else {
                    result[i] = Float.floatToIntBits(stack.getFloat(i1[i]));
                }
            } else {
                if (isInt[i]) {
                    result[i] = i1[i];
                } else {
                    result[i] = Float.floatToIntBits(i1[i]);
                }
            }
        }
        System.out.println("sub " + toRun.eclSubPack.subName + " start by ins_11 arg[0]:"
                + (result.length < 1 ? "" : result[0]));
        EclManager.toAddSubs.add(toRun.insertArgs(result).startByIns11(true).setManager(enemy).setPartent(this));
        EclManager.onPauseSubs.add(this);
        EclManager.toDeleteSubs.add(this);
    }

    private void _12(int i0, int i1) { // goto
        gotoIns(i0);
    }

    private void _13(int i0, int i1) { // unless goto
        if (stack.popInt() == 0) {
            gotoIns(i0);
        }
    }

    private void _14(int i0, int i1) { // if goto
        if (stack.popInt() != 0) {
            gotoIns(i0);
            --insPosition;
        }
    }

    private void _15(String s0, int[] i1, boolean[] isInt, int paramMask) {
        EclSub toRun = EclManager.getSubPack(s0);
        int[] result = new int[i1.length];
        for (int i = 0; i < result.length; ++i) {
            if (((paramMask >> (i + 1)) & 1) == 1) {
                if (isInt[i]) {
                    result[i] = stack.getInt(i1[i]);
                } else {
                    result[i] = Float.floatToIntBits(stack.getFloat(i1[i]));
                }
            } else {
                if (isInt[i]) {
                    result[i] = i1[i];
                } else {
                    result[i] = Float.floatToIntBits(i1[i]);
                }
            }
        }
        System.out.println("sub " + toRun.eclSubPack.subName + " start by ins_15");
        EclManager.toAddSubs.add(toRun.insertArgs(i1).setManager(enemy).startByIns11(false));
    }

    private void _16(String s0, int i1, int i2) {

    }

    private void _17(int i0) {

    }

    private void _22(int i0, String s1) {

    }

    private void _23(int i0) {

    }

    private void _30(String s0, int i1) {

    }

    private void _40(int i0) {
        stack.initVarSize(i0, tempArg);
    }

    private void _42(int i0) {
        stack.push(i0);
    }

    private void _43(int i0) {
        stack.putGlobal(i0, stack.popInt());
    }

    private void _44(float f0) {
        stack.push(f0);
    }

    private void _45(float f0) {
        stack.putGlobal((int) f0, stack.popFloat());
    }

    private void _50() {
        stack.push(stack.popInt() + stack.popInt());
    }

    private void _51() {
        stack.push(stack.popFloat() + stack.popFloat());
    }

    private void _52() {
        int b = stack.popInt();
        int a = stack.popInt();
        stack.push(a - b);
    }

    private void _53() {
        float b = stack.popFloat();
        float a = stack.popFloat();
        stack.push(a - b);
    }

    private void _54() {
        stack.push(stack.popInt() * stack.popInt());
    }

    private void _55() {
        stack.push(stack.popFloat() * stack.popFloat());
    }

    private void _56() {
        int b = stack.popInt();
        int a = stack.popInt();
        stack.push(a / b);
    }

    private void _57() {
        float b = stack.popFloat();
        float a = stack.popFloat();
        stack.push(a / b);
    }

    private void _58() {
        int b = stack.popInt();
        int a = stack.popInt();
        stack.push(a % b);
    }

    private void _59() {
        if (stack.popInt() == stack.popInt()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _60() {
        if (stack.popFloat() == stack.popFloat()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _61() {
        if (stack.popInt() != stack.popInt()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _62() {
        if (stack.popFloat() != stack.popFloat()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _63() {
        if (stack.popInt() > stack.popInt()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _64() {
        if (stack.popFloat() > stack.popFloat()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _65() {
        if (stack.popInt() >= stack.popInt()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _66() {
        if (stack.popFloat() >= stack.popFloat()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _67() {
        if (stack.popInt() < stack.popInt()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _68() {
        if (stack.popFloat() < stack.popFloat()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _69() {
        if (stack.popInt() <= stack.popInt()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _70() {
        if (stack.popFloat() <= stack.popFloat()) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _71() {
        if (stack.popInt() == 0) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _72() {
        if (stack.popFloat() == 0) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _73() {
        if (stack.popInt() != 0 || stack.popInt() != 0) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _74() {
        if (stack.popInt() != 0 && stack.popInt() != 0) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _75() {
        stack.push(stack.popInt() ^ stack.popInt());
    }

    private void _76() {
        stack.push(stack.popInt() | stack.popInt());
    }

    private void _77() {
        stack.push(stack.popInt() & stack.popInt());
    }

    private void _78(int i0) {
        if (i0 > 0) {
            stack.push(1);
        } else {
            stack.push(0);
        }
    }

    private void _81(float f0, float f1, float f2, float f3) {

    }

    private void _82(float f0) {

    }

    private void _83() {

    }

    private void _84() {

    }

    private void _85(float f0, float f1, float f2) {

    }

    private void _86(float f0, float f1, float f2) {

    }

    private void _87(float f0, float f1, float f2, float f3, float f4) {

    }

    private void _88(float f0, float f1, float f2, float f3, float f4) {

    }

    private void _89(float f0, float f1, float f2) {

    }

    private void _90(float f0, float f1, float f2, float f3, float f4) {

    }

    private void _91(int i0, float f1, int i2, int i3, float f4, float f5) {

    }

    private void _92(int i0, float f1, int i2, int i3, float f4, float f5) {

    }

    private void _93(float f0, float f1, float f2, float f3) {

    }

    private void _300(String s0, float f1, float f2, int i3, int i4, int i5) {

    }

    private void _301(String s0, float f1, float f2, int i3, int i4, int i5) {
        System.out.println("sub " + eclSubPack.subName + " start by ins_301");
        FightScreen.instence.boss = new Enemy();
        FightScreen.instence.boss.init(new Vector2(275, 450), 10, 700, new Task[]{new TaskMoveTo(193, 250)});
        enemy = FightScreen.instence.boss;
        // EclManager.toAddSubs.add(eclFile.eclManager.getSubPack(s0).setManager(laser).startByIns11(false));
    }

    private void _302(int i0) {

    }

    private void _303(int i0, int i1) {

    }

    private void _304(String s0, float f1, float f2, int i3, int i4, int i5) {

    }

    private void _305(String s0, float f1, float f2, int i3, int i4, int i5) {

    }

    private void _306(int i0, int i1) {

    }

    private void _307(int i0, int i1) {

    }

    private void _308(int i0, int i1) {

    }

    private void _309(String s0, float f1, float f2, int i3, int i4, int i5) {

    }

    private void _310(String s0, float f1, float f2, int i3, int i4, int i5) {

    }

    private void _311(String s0, float f1, float f2, int i3, int i4, int i5) {

    }

    private void _312(String s0, float f1, float f2, int i3, int i4, int i5) {

    }

    private void _313(int i0) {

    }

    private void _314(int i0, int i1) {

    }

    private void _315(int i0, int i1, float f2) {

    }

    private void _316(int i0, int i1) {

    }

    private void _317(int i0, int i1) {

    }

    private void _318() {

    }

    private void _319(int i0, float f1) {

    }

    private void _320(int i0, float f1, float f2) {

    }

    private void _321(String s0, int i1, int i2, int i3, int i4, int i5) {

    }

    private void _322(int i0, int i1) {

    }

    private void _323(int i0, int i1) {

    }

    private void _324(int i0) {

    }

    private void _325(int i0, int i1, int i2, int i3) {

    }

    private void _326(int i0, int i1, int i2, int i3, int i4, int i5) {

    }

    private void _327(int i0, int i1) {

    }

    private void _328(int i0, int i1, int i2, int i3) {

    }

    private void _329(int i0, float f1, float f2) {

    }

    private void _330(int i0, int i1, int i2, float f3, float f4) {

    }

    private void _331(int i0, int i1) {

    }

    private void _332(int i0, int i1, int i2, int i3) {

    }

    private void _333(int i0, int i1, int i2, float f3, float f4) {

    }

    private void _334(int i0) {

    }

    private void _335(int i0, float f1, float f2) {

    }

    private void _336(int i0, int i1) {

    }

    private void _337(int i0, int i1) {

    }

    private void _338(int i0, int i1, float f2, float f3, float f4) {

    }

    private void _339(int i0, int i1, int i2) {

    }

    private void _340(int i0) {

    }

    private void _400(float f0, float f1) {

    }

    private void _401(int i0, int i1, float f2, float f3) {

    }

    private void _402(float f0, float f1) {

    }

    private void _403(int i0, int i1, float f2, float f3) {

    }

    private void _404(float f0, float f1) {

    }

    private void _405(int i0, int i1, float f2, float f3) {

    }

    private void _406(float f0, float f1) {

    }

    private void _407(int i0, int i1, float f2, float f3) {

    }

    private void _408(float f0, float f1, float f2, float f3) {

    }

    private void _409(int i0, int i1, float f2, float f3, float f4) {

    }

    private void _410(float f0, float f1, float f2, float f3) {

    }

    private void _411(int i0, int i1, float f2, float f3, float f4) {

    }

    private void _412(int i0, int i1, float f2) {

    }

    private void _413(int i0, int i1, float f2) {

    }

    private void _414() {

    }

    private void _415() {

    }

    private void _416(float f0, float f1, float f2) {

    }

    private void _417(float f0, float f1, float f2) {

    }

    private void _418(float f0, float f1) {

    }

    private void _419(float f0, float f1) {

    }

    private void _420(float f0, float f1, float f2, float f3, float f4, float f5) {

    }

    private void _421(int i0, int i1, float f2, float f3, float f4, float f5, float f6) {

    }

    private void _422(float f0, float f1, float f2, float f3, float f4, float f5) {

    }

    private void _423(int i0, int i1, float f2, float f3, float f4, float f5, float f6) {

    }

    private void _424(int i0) {

    }

    private void _425(int i0, float f1, float f2, float f3, float f4, float f5, float f6) {

    }

    private void _426(int i0, float f1, float f2, float f3, float f4, float f5, float f6) {

    }

    private void _427() {

    }

    private void _428(float f0, float f1) {

    }

    private void _429(int i0, int i1, float f2, float f3) {

    }

    private void _430(float f0, float f1) {

    }

    private void _431(int i0, int i1, float f2, float f3) {

    }

    private void _432(int i0) {

    }

    private void _433(int i0) {

    }

    private void _434(int i0, int i1, int i2, float f3, float f4) {

    }

    private void _435(int i0, int i1, int i2, float f3, float f4) {

    }

    private void _436(int i0, int i1, float f2, float f3) {

    }

    private void _437(int i0, int i1, float f2, float f3) {

    }

    private void _438(int i0, int i1, int i2, float f3, float f4) {

    }

    private void _439(int i0, int i1, int i2, float f3, float f4) {

    }

    private void _440(float f0) {

    }

    private void _441(int i0, int i1, float f2) {

    }

    private void _442(float f0) {

    }

    private void _443(int i0, int i1, float f2) {

    }

    private void _444(float f0) {

    }

    private void _445(int i0, int i1, float f2) {

    }

    private void _446(float f0) {

    }

    private void _447(int i0, int i1, float f2) {

    }

    private void _500(float f0, float f1) {

    }

    private void _501(float f0, float f1) {

    }

    private void _502(int i0) {

    }

    private void _503(int i0) {

    }

    private void _504(float f0, float f1, float f2, float f3) {

    }

    private void _505() {

    }

    private void _506() {

    }

    private void _507(int i0, int i1) {

    }

    private void _508(float f0, float f1) {

    }

    private void _509() {

    }

    private void _510(int i0) {

    }

    private void _511(int i0) {
        enemy.hp = i0;
    }

    private void _512(int i0) {
        if (i0 == -1) {
            FightScreen.instence.onBoss = false;
        } else {
            FightScreen.instence.onBoss = true;
        }
        _301("", 0, 0, 0, 0, 0);
    }

    private void _513() {

    }

    private void _514(int i0, int i1, int i2, String s3) {
        EclManager.nextSub = EclManager.getSubPack("BossCard1").setManager(enemy);
    }

    private void _515(int i0) {

    }

    private void _516(int i0) {

    }

    private void _517(int i0, int i1, int i2) {

    }

    private void _518(int i0) {

    }

    private void _519() {

    }

    private void _520() {

    }

    private void _521(int i0, String s1) {

    }

    private void _522(int i0, int i1, int i2, String s3) {

    }

    private void _523() {

    }

    private void _524(int i0) {

    }

    private void _525() {

    }

    private void _526(float f0) {

    }

    private void _527(int i0, float f1, int i2) {

    }

    private void _528(int i0, int i1, int i2, String s3) {

    }

    private void _529(float f0, float f1, float f2, float f3) {

    }

    private void _530(float f0, float f1, float f2, float f3, float f4, float f5) {

    }

    private void _531(float f0, float f1, float f2) {

    }

    private void _532(int i0, int i1, int i2, int i3) {

    }

    private void _533(int i0, int i1, int i2, int i3, int i4, int i5) {

    }

    private void _534(int i0, int i1, int i2) {

    }

    private void _535(int i0, int i1, int i2, int i3, int i4) {
        switch (GameMain.difficulty) {
            case 0:
                stack.putGlobal(i0, i1);
                break;
            case 1:
                stack.putGlobal(i0, i2);
                break;
            case 2:
                stack.putGlobal(i0, i3);
                break;
            case 3:
                stack.putGlobal(i0, i4);
                break;
        }
    }

    private void _536(float f0, float f1, float f2, float f3, float f4) {
        switch (GameMain.difficulty) {
            case 0:
                stack.putGlobal((int) f0, f1);
                break;
            case 1:
                stack.putGlobal((int) f0, f2);
                break;
            case 2:
                stack.putGlobal((int) f0, f3);
                break;
            case 3:
                stack.putGlobal((int) f0, f4);
                break;
        }
    }

    private void _537(int i0, int i1, int i2, String s3) {

    }

    private void _538(int i0, int i1, int i2, String s3) {

    }

    private void _539(int i0, int i1, int i2, String s3) {

    }

    private void _540(int i0) {

    }

    private void _541(int i0) {

    }

    private void _542() {

    }

    private void _543() {

    }

    private void _544(int i0) {

    }

    private void _545() {

    }

    private void _546(int i0, int i1) {

    }

    private void _547(float f0) {

    }

    private void _548(int i0, int i1, int i2, int i3) {

    }

    private void _549(int i0) {

    }

    private void _550(int i0) {

    }

    private void _551(int i0) {

    }

    private void _552(int i0) {

    }

    private void _553(int i0) {

    }

    private void _554() {

    }

    private void _555(int i0, int i1) {

    }

    private void _556(String s0) {

    }

    private void _557(int i0, int i1, int i2, float f3, float f4) {

    }

    private void _558(int i0) {

    }

    private void _559(int i0) {

    }

    private void _560(float f0, float f1) {

    }

    private void _561() {

    }

    private void _562() {

    }

    private void _563(int i0) {

    }

    private void _564(float f0) {

    }

    private void _565(float f0) {

    }

    private void _566() {

    }

    private void _567(int i0) {

    }

    private void _568(int i0) {

    }

    private void _569(int i0) {

    }

    private void _570() {

    }

    private void _571() {

    }

    private void _572(int i0) {

    }

    private void _600(int i0) {
        EclBulletShooter eclBulletShooter = new EclBulletShooter().init(enemy);
        eclBulletShooters.put(i0, eclBulletShooter);
    }

    private void _601(int danmakuNum) {
        eclBulletShooters.get(danmakuNum).shoot();
    }

    private void _602(int danmakuNum, int form, int color) {
        eclBulletShooters.get(danmakuNum).setFormAndColor(form, color);
    }

    private void _603(int danmakuNum, float offsetX, float offsetY) {
        eclBulletShooters.get(danmakuNum).setOffsetCartesian(offsetX, offsetY);
    }

    private void _604(int danmakuNum, float direct, float r) {
        eclBulletShooters.get(danmakuNum).setDirectionAndSub(direct - 1.5707963267948966f, r);
    }

    private void _605(int danmakuNum, float speed, float slowestSpeed) {
        eclBulletShooters.get(danmakuNum).setSpeed(speed, slowestSpeed);
    }

    private void _606(int danmakuNum, int way, int ceng) {
        eclBulletShooters.get(danmakuNum).setWaysAndOverlap(way, ceng);
    }

    private void _607(int danmakuNum, int style) {
        eclBulletShooters.get(danmakuNum).setStyle(style);
    }

    private void _608(int danmakuNum, int voiceOnShoot, int voiceOnChange) {

    }

    private void _609(int danmakuNum, int num, int way, int mode, int inta, int intb, float floatr, float floats) {
        eclBulletShooters.get(danmakuNum).addChange(num, new ChangeTask(way == 0, mode, inta, intb, 0, 0, floatr, floats, 0, 0));
    }

    private void _610(int danmakuNum, int num, int way, int mode, int inta, int intb, int intc, int intd, float floatr,
                      float floats, float floatm, float floatn) {
        eclBulletShooters.get(danmakuNum).addChange(num, new ChangeTask(way == 0, mode, inta, intb, intc, intd, floatr, floats, floatm, floatn));
    }

    private void _611(int danmakuNum, int way, int mode, int inta, int intb, float floatr, float floats) {
        eclBulletShooters.get(danmakuNum).addChange(new ChangeTask(way == 0, mode, inta, intb, 0, 0, floatr, floats, 0, 0));
    }

    private void _612(int danmakuNum, int way, int mode, int inta, int intb, int intc, int intd, float floatr,
                      float floats, float floatm, float floatn) {
    }

    private void _613() {

    }

    private void _614(int danmakuA, int danmakuB) {
        eclBulletShooters.put(danmakuA, eclBulletShooters.get(danmakuB).clone());
    }

    private void _615(float floatR) {

    }

    private void _616(float floatR) {

    }

    private void _617(int i0, float f1, float f2, float f3, float f4, float f5, float f6) {

    }

    private void _618(int i0, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9,
                      float f10) {

    }

    private void _619(int i0, float f1, float f2, float f3, float f4) {

    }

    private void _620(int i0, int i1, int i2, int i3, int i4, int i5, int i6) {

    }

    private void _621(int i0, int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {

    }

    private void _622(int i0, int i1, int i2, int i3, int i4) {

    }

    private void _623(float floatVarName, float floatX, float floatY) {

    }

    private void _624(int danmakuNum, float floatA, float b, float c, float d, float e, float f, float g, float h) {

    }

    private void _625(int danmakuNum, int intA, int b, int c, int d, int e, int f, int g, int h) {

    }

    private void _626(int danmakuNum, float floatAngel, float r) {
        Vector2 cart = MathHelper.polarToCartesian(floatAngel, r);
        Random random = new Random();
        eclBulletShooters.get(danmakuNum).setOffsetCartesian(random.nextFloat() * r * (random.nextBoolean() ? 1 : -1),
                random.nextFloat() * r * (random.nextBoolean() ? 1 : -1));
    }

    private void _627(int danmakuNum, float r) {
    }

    private void _628(int danmakuNum, float floatX, float y) {
        eclBulletShooters.get(danmakuNum).setCenter(floatX + GameMain.width / 2f, GameMain.height - y);
    }

    private void _629(float floatR, int intRgb) {

    }

    // int
    private void _630(int a) {

    }

    private void _631(int a) {

    }

    private void _632(int a) {

    }

    private void _633(int a) {

    }

    private void _634(int a) {

    }

    // float
    private void _635(float a) {

    }

    private void _636(float a) {

    }

    // int
    private void _637(int a) {

    }

    private void _638(int a) {

    }

    private void _639(int a) {

    }

    // mode=16777216 danmakuNum=2
    private void _640(int danmakuNum, int intMode, String sub) {
    }

    private void _641(int danmakuNum) {

    }

    private void _700(int i0, float f1, float f2, float f3, float f4) {

    }

    private void _701(int i0, int i1, int i2, int i3, int i4, int i5) {

    }

    private void _702(int i0) {

    }

    private void _703(int i0, int i1) {

    }

    private void _704(int i0, float f1, float f2) {

    }

    private void _705(int i0, float f1, float f2) {

    }

    private void _706(int i0, float f1) {

    }

    private void _707(int i0, float f1) {

    }

    private void _708(int i0, float f1) {

    }

    private void _709(int i0, float f1) {

    }

    private void _710(int i0) {

    }

    private void _711(int i0) {

    }

    private void _712(float f0, float f1) {

    }

    private void _713(int i0) {

    }

    private void _714(int i0, int i1) {

    }

    private void _800(int i0, String s1) {

    }

    private void _801(float f0, float f1, int i2) {

    }

    private void _802(int i0) {

    }

    private void _900(int i0) {

    }

    private void _901() {

    }

    private void _902() {

    }

    private void _1000(int i0) {

    }

    private void _1001(int i0) {

    }

    private void _1002(int i0) {

    }

    public void invoke(EclIns ins) {
        switch (ins.id) {

            case 0:
                _0();
                break;
            case 1:
                _1();
                break;
            case 10:
                _10();
                break;
            case 11:
                String subName11 = ins.readString();
                int[] intBytes11 = new int[ins.param_count - 1];
                boolean[] isInt11 = new boolean[ins.param_count - 1];
                for (int i = 0; i < intBytes11.length; ++i) {
                    int type = ins.readInt();
                    switch (type) {
					case 26985: //0x69 0x69
                        isInt11[i] = true;
                        intBytes11[i] = ins.readInt();
						break; 
						case 26214://0x66 0x66
	                        isInt11[i] = false;
	                        intBytes11[i] =ins.readInt();
	                        break;
	                        case 26217://0x69 0x66
	                            isInt11[i] = true;
		                        intBytes11[i] = (int) ins.readFloat();
		                        break;
		                        case 26982://0x66 0x69
			                        isInt11[i] = false;
			                        intBytes11[i] = Float.floatToIntBits(ins.readInt());
			                        break;
			                        default:
			                        	throw new RuntimeException("unexpect value:"+type);
		                        	
					}
                }
                _11(subName11, intBytes11, isInt11, ins.param_mask);
                break;
            case 12:
                _12((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 13:
                _13((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 14:
                _14((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 15:
                String subName15 = ins.readString();
                int[] intBytes15 = new int[ins.param_count - 1];
                boolean[] isInt15 = new boolean[ins.param_count - 1];
                for (int i = 0; i < intBytes15.length; ++i) {
                    int type = ins.readInt();
                    switch (type) {
					case 26985: //0x69 0x69
                        isInt15[i] = true;
                        intBytes15[i] = ins.readInt();
						break; 
						case 26214://0x66 0x66
	                        isInt15[i] = false;
	                        intBytes15[i] =ins.readInt();
	                        break;
	                        case 26217://0x69 0x66
	                            isInt15[i] = true;
		                        intBytes15[i] = (int) ins.readFloat();
		                        break;
		                        case 26982://0x66 0x69
			                        isInt15[i] = false;
			                        intBytes15[i] = Float.floatToIntBits(ins.readInt());
			                        break;
			                        default:
			                        	throw new RuntimeException("unexpect value:"+type);
					}
                }
                _15(subName15, intBytes15, isInt15, ins.param_mask);
                break;
            case 16:
                _16(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 17:
                _17((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 22:
                _22((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 23:
                // _23((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) :
                // ins.readInt());
                break;
            case 30:
                _30(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 40:
                _40((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 42:
                _42(((ins.param_mask & 1) == 1) ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 43:
                _43(ins.readInt());
                break;
            case 44:
                _44((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 45:
                // _45((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) :
                // ins.readFloat());
                _45(ins.readFloat());
                break;
            case 50:
                _50();
                break;
            case 51:
                _51();
                break;
            case 52:
                _52();
                break;
            case 53:
                _53();
                break;
            case 54:
                _54();
                break;
            case 55:
                _55();
                break;
            case 56:
                _56();
                break;
            case 57:
                _57();
                break;
            case 58:
                _58();
                break;
            case 59:
                _59();
                break;
            case 60:
                _60();
                break;
            case 61:
                _61();
                break;
            case 62:
                _62();
                break;
            case 63:
                _63();
                break;
            case 64:
                _64();
                break;
            case 65:
                _65();
                break;
            case 66:
                _66();
                break;
            case 67:
                _67();
                break;
            case 68:
                _68();
                break;
            case 69:
                _69();
                break;
            case 70:
                _70();
                break;
            case 71:
                _71();
                break;
            case 72:
                _72();
                break;
            case 73:
                _73();
                break;
            case 74:
                _74();
                break;
            case 75:
                _75();
                break;
            case 76:
                _76();
                break;
            case 77:
                _77();
                break;
            case 78:
                int varPos = ins.readInt();
                _78((ins.param_mask & 1) == 1 ? stack.getInt(varPos) : ins.readInt());
                stack.dec(varPos);
                break;
            case 81:
                _81((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 82:
                _82((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 83:
                _83();
                break;
            case 84:
                _84();
                break;
            case 85:
                _85((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 86:
                _86((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 87:
                _87((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 88:
                _88((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 89:
                _89((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 90:
                _90((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 91:
                _91((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 92:
                _92((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 93:
                _93((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 300:
                _300(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 301:
                _301(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 302:
                _302((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 303:
                _303((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 304:
                _304(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 305:
                _305(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 306:
                _306((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 307:
                _307((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 308:
                _308((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 309:
                _309(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 310:
                _310(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 311:
                _311(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 312:
                _312(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 313:
                _313((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 314:
                _314((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 315:
                _315((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 316:
                _316((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 317:
                _317((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 318:
                _318();
                break;
            case 319:
                _319((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 320:
                _320((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 321:
                _321(ins.readString(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 322:
                _322((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 323:
                _323((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 324:
                _324((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 325:
                _325((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 326:
                _326((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 327:
                _327((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 328:
                _328((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 329:
                _329((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 330:
                _330((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 331:
                _331((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 332:
                _332((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 333:
                _333((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 334:
                _334((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 335:
                _335((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 336:
                _336((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 337:
                _337((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 338:
                _338((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 339:
                _339((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 340:
                _340((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 400:
                _400((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 401:
                _401((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 402:
                _402((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 403:
                _403((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 404:
                _404((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 405:
                _405((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 406:
                _406((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 407:
                _407((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 408:
                _408((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 409:
                _409((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 410:
                _410((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 411:
                _411((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 412:
                _412((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 413:
                _413((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 414:
                _414();
                break;
            case 415:
                _415();
                break;
            case 416:
                _416((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 417:
                _417((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 418:
                _418((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 419:
                _419((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 420:
                _420((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 421:
                _421((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 422:
                _422((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 423:
                _423((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 424:
                _424((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 425:
                _425((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 426:
                _426((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 427:
                _427();
                break;
            case 428:
                _428((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 429:
                _429((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 430:
                _430((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 431:
                _431((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 432:
                _432((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 433:
                _433((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 434:
                _434((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 435:
                _435((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 436:
                _436((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 437:
                _437((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 438:
                _438((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 439:
                _439((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 440:
                _440((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 441:
                _441((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 442:
                _442((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 443:
                _443((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 444:
                _444((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 445:
                _445((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 446:
                _446((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 447:
                _447((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 500:
                _500((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 501:
                _501((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 502:
                _502((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 503:
                _503((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 504:
                _504((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 505:
                _505();
                break;
            case 506:
                _506();
                break;
            case 507:
                _507((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 508:
                _508((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 509:
                _509();
                break;
            case 510:
                _510((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 511:
                _511((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 512:
                _512((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 513:
                _513();
                break;
            case 514:
                _514((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 515:
                _515((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 516:
                _516((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 517:
                _517((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 518:
                _518((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 519:
                _519();
                break;
            case 520:
                _520();
                break;
            case 521:
                _521((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 522:
                _522((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 523:
                _523();
                break;
            case 524:
                _524((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 525:
                _525();
                break;
            case 526:
                _526((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 527:
                _527((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 528:
                _528((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 529:
                _529((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 530:
                _530((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 531:
                _531((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 532:
                _532((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 533:
                _533((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 534:
                _534((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 535:
                _535(ins.readInt(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 536:
                _536(ins.readFloat(), ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 537:
                _537((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 538:
                _538((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 539:
                _539((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 540:
                _540((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 541:
                _541((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 542:
                _542();
                break;
            case 543:
                _543();
                break;
            case 544:
                _544((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 545:
                _545();
                break;
            case 546:
                _546((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 547:
                _547((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 548:
                _548((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 549:
                _549((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 550:
                _550((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 551:
                _551((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 552:
                _552((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 553:
                _553((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 554:
                _554();
                break;
            case 555:
                _555((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 556:
                _556(ins.readString());
                break;
            case 557:
                _557((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 558:
                _558((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 559:
                _559((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 560:
                _560((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 561:
                _561();
                break;
            case 562:
                _562();
                break;
            case 563:
                _563((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 564:
                _564((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 565:
                _565((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 566:
                _566();
                break;
            case 567:
                _567((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 568:
                _568((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 569:
                _569((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 570:
                _570();
                break;
            case 571:
                _571();
                break;
            case 572:
                _572((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 600:
                _600((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 601:
                _601((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 602:
                _602((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 603:
                _603((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 604:
                _604((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 605:
                int p1 = (ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt();
                float p2 = ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat();
                float p3 = ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat();
                _605(p1, p2, p3);
                break;
            case 606:
                _606((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 607:
                _607((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 608:
                _608((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 609:
                _609((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 7) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 610:
                _610((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 7) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 8) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 9) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 10) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 11) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 611:
                _611((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 612:
                _612((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 7) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 8) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 9) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 10) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 613:
                _613();
                break;
            case 614:
                _614((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 615:
                _615((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 616:
                _616((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 617:
                _617((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 618:
                _618((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 7) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 8) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 9) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 10) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 619:
                _619((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 620:
                _620((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 621:
                _621((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 7) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 8) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 9) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 10) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 622:
                _622((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 623:
                _623((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 624:
                _624((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 7) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 8) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 625:
                _625((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 6) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 7) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 8) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 626:
                _626((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 627:
                _627((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 628:
                _628((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 629:
                _629((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 630:
                _630((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 631:
                _631((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 632:
                _632((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 633:
                _633((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 634:
                _634((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 635:
                _635((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 636:
                _636((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 637:
                _637((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 638:
                _638((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 639:
                _639((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 640:
                _640((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 641:
                _641((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 700:
                _700((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 701:
                _701((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 3) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 4) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 5) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 702:
                _702((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 703:
                _703((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 704:
                _704((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 705:
                _705((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 706:
                _706((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 707:
                _707((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 708:
                _708((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 709:
                _709((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 710:
                _710((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 711:
                _711((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 712:
                _712((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat());
                break;
            case 713:
                _713((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 714:
                _714((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 800:
                _800((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt(), ins.readString());
                break;
            case 801:
                _801((ins.param_mask & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 1) & 1) == 1 ? stack.getFloat(ins.readInt()) : ins.readFloat(),
                        ((ins.param_mask >> 2) & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 802:
                _802((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 900:
                _900((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 901:
                _901();
                break;
            case 902:
                _902();
                break;
            case 1000:
                _1000((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 1001:
                _1001((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
            case 1002:
                _1002((ins.param_mask & 1) == 1 ? stack.getInt(ins.readInt()) : ins.readInt());
                break;
        }

    }
}
