package com.InsProcess.parse.beans;

import java.util.*;

import com.badlogic.gdx.math.Vector2;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.Junko;
import com.meng.TaiHunDanmaku.ui.*;
import com.InsProcess.parse.*;

/**
 * @author Administrator th10_sub_t
 */
public class EclSub {
    public byte[] magic = new byte[4];
    public int data_offset;/* sizeof(th10_sub_t) */
    public int[] zero = new int[2];
    public byte[] data;
    private ArrayList<EclIns> inses = new ArrayList<>();
    private int dataPosition = 0;
    private int eclPosition = 0;

    private EclFile eclFile;
    public EclSubPack eclSubPack;
    private EclSub parentSub;

    private Junko master;

    private HashMap<Integer,byte[]> varMap=new HashMap<>();
	private EclStack eclStack=new EclStack();
    public HashMap<Integer, BulletShooter> bulletShooters = new HashMap<>();

    private int waitFrams = 0;

    private boolean startByIns11 = false;

    public EclSub(EclFile file, EclSubPack eclSubPack) {
        eclFile = file;
        this.eclSubPack = eclSubPack;
    }

    public EclIns nowIns() {
        return inses.get(eclPosition);
    }

    public EclIns nextIns() {
        return inses.get(++eclPosition);
    }

    public EclIns preIns() {
        return inses.get(--eclPosition);
    }

    public void readIns() {
        while (dataPosition < data.length) {
            EclIns eclIns = new EclIns(this);
            eclIns.time = readInt();
            eclIns.id = readShort();
            eclIns.size = readShort();
            eclIns.param_mask = readShort();
            eclIns.rank_mask = readByte();
            eclIns.param_count = readByte();
            eclIns.zero = readInt();
            int bound = eclIns.size - 16;
            eclIns.data = new byte[bound];
            for (int i = 0; i < bound; ++i) {
                eclIns.data[i] = data[dataPosition];
                ++dataPosition;
            }
            inses.add(eclIns);
        }
    }

    public EclSub setPartent(EclSub parent) {
        parentSub = parent;
        return this;
    }

    public EclSub startByIns11(boolean b) {
        eclPosition = 0;
        startByIns11 = b;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (EclIns eclIns : inses) {
            stringBuilder.append("\n").append(eclIns.toString());
        }
        return stringBuilder.toString();
    }

    private byte readByte() {
        return readByte(dataPosition++);
    }

    private short readShort() {
        short s = readShort(dataPosition);
        dataPosition += 2;
        return s;
    }

    private int readInt() {
        int i = readInt(dataPosition);
        dataPosition += 4;
        return i;
    }

    private byte readByte(int pos) {
        return data[pos];
    }

    private short readShort(int pos) {
        return (short) (data[pos] & 0xff | (data[pos + 1] & 0xff) << 8);
    }

    private int readInt(int pos) {
        return (data[pos] & 0xff) | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
    }

    private void moveToNextInt() {
        if ((dataPosition & 0b11) == 0) {
            return;
        }
        dataPosition |= 0b11;
        ++dataPosition;
    }

    private void gotoIns(int length) {
        int nowSkipLength = 0;
        if (length < 0) {
            while (nowSkipLength > length) {
                EclIns ins = preIns();
                nowSkipLength += ins.size;
            }
        } else {
            while (nowSkipLength < length) {
                EclIns ins = nextIns();
                nowSkipLength += ins.size;
            }
        }
    }

    public void update() {
        if (eclPosition < inses.size()) {
            EclIns ins = inses.get(eclPosition);
			FightScreen.nowins="now:"+ins.id+"\n"+FightScreen.nowins;
            if (((ins.rank_mask >> GameMain.difficulty) & 0b1) == 0) {
                ++eclPosition;
                return;
            }
            if (ins.id == 23) {
                if (ins.readInt() <= ++waitFrams) {
                    waitFrams = 0;
                    ++eclPosition;
                }
                return;
            }
            invoke(ins);
            ++eclPosition;
            update();
        }
    }

    private void _0() {

    }

    private void _1() {

    }

    private void _10() {
        System.out.println("sub "+eclSubPack.subName+" exit");
        if (startByIns11) {
            EclManager.toAddSubs.add(parentSub);
            EclManager.onPauseSubs.remove(parentSub);
            EclManager.toDeleteSubs.add(this);
        } else {
            EclManager.toDeleteSubs.add(this);
        }
    }

    private void _11(String s0, byte[] i1) {
        EclManager.toAddSubs.add(eclFile.eclManager.getSubPack(s0).startByIns11(true).setPartent(this));
        EclManager.onPauseSubs.add(this);
        EclManager.toDeleteSubs.add(this);
    }

    private void _12(int i0, int i1) { //goto
        gotoIns(i0);
    }

    private void _13(int i0, int i1) { //unless  goto
	    if(eclStack.popInt()==0){
	      gotoIns(i0);
	    }
    }

    private void _14(int i0, int i1) { //if   goto
	    if(eclStack.popInt()!=0){
	      gotoIns(i0);
	    }
    }

    private void _15(String s0, byte[] i1) {
        EclManager.toAddSubs.add(eclFile.eclManager.getSubPack(s0).startByIns11(false));
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
	  eclStack.initVarSize(i0);
    }

    private void _42(int i0) {
	    eclStack.push(i0);
    }

    private void _43(int i0) {
	    eclStack.putGlobal(i0,eclStack.popInt());
    }

    private void _44(float f0) {
	    eclStack.push(f0);
    }

    private void _45(float f0) {
	    eclStack.putGlobal(Float.floatToIntBits(f0),eclStack.popFloat());
    }

    private void _50() {
		eclStack.push(eclStack.popInt()+eclStack.popInt());
    }

    private void _51() {
		eclStack.push(eclStack.popFloat()+eclStack.popFloat());
    }

    private void _52() {
		int b=eclStack.popInt();
		int a=eclStack.popInt();
		eclStack.push(a-b);
    }

    private void _53() {
		float b=eclStack.popFloat();
		float a=eclStack.popFloat();
		eclStack.push(a-b);
    }

    private void _54() {
		eclStack.push(eclStack.popInt()*eclStack.popInt());
    }

    private void _55() {
		eclStack.push(eclStack.popFloat()*eclStack.popFloat());
    }

    private void _56() {
		int b=eclStack.popInt();
		int a=eclStack.popInt();
		eclStack.push(a/b);
    }

    private void _57() {
		float b=eclStack.popFloat();
		float a=eclStack.popFloat();
		eclStack.push(a/b);
    }

    private void _58() {
	  int b=eclStack.popInt();
	  int a=eclStack.popInt();
	  eclStack.push(a%b);
    }

    private void _59() {
		if(eclStack.popInt()==eclStack.popInt()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _60() {
		if(eclStack.popFloat()==eclStack.popFloat()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _61() {
		if(eclStack.popInt()!=eclStack.popInt()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _62() {
		if(eclStack.popFloat()!=eclStack.popFloat()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _63() {
		if(eclStack.popInt()>eclStack.popInt()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _64() {
		if(eclStack.popFloat()>eclStack.popFloat()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _65() {
		if(eclStack.popInt()>=eclStack.popInt()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _66() {
		if(eclStack.popFloat()>=eclStack.popFloat()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _67() {
		if(eclStack.popInt()<eclStack.popInt()){
		  eclStack.push(1);
		}else{
		  eclStack.push(0);
		}
    }

    private void _68() {
		if(eclStack.popFloat()<eclStack.popFloat()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _69() {
		if(eclStack.popInt()<=eclStack.popInt()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _70() {
		if(eclStack.popFloat()<=eclStack.popFloat()){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _71() {
	    eclStack.push(eclStack.popInt()==0?1:0);
    }

    private void _72() {
		eclStack.push(eclStack.popFloat()==0?1:0);
    }

    private void _73() {
		if(eclStack.popInt()!=0||eclStack.popInt()!=0){
	  	  eclStack.push(1);
		}else{
		  eclStack.push(0);
		}
    }

    private void _74() {
		if(eclStack.popInt()!=0&&eclStack.popInt()!=0){
			eclStack.push(1);
		  }else{
			eclStack.push(0);
		  }
    }

    private void _75() {
		eclStack.push(eclStack.popInt()^eclStack.popInt());
    }

    private void _76() {
		eclStack.push(eclStack.popInt()|eclStack.popInt());
    }

    private void _77() {
		eclStack.push(eclStack.popInt()&eclStack.popInt());
    }

    private void _78(int i0) {
	  eclStack.push(i0==0?0:1);
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

    }

    private void _512(int i0) {

    }

    private void _513() {

    }

    private void _514(int i0, int i1, int i2, String s3) {

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

    }

    private void _536(float f0, float f1, float f2, float f3, float f4) {

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

    private void _600(int danmakuNum) {
        BulletShooter bShooter = new BulletShooter().init(FightScreen.instence.boss);
        bulletShooters.put(danmakuNum, bShooter);
    }

    private void _601(int danmakuNum) {
        bulletShooters.get(danmakuNum).shoot();
    }

    private void _602(int danmakuNum, int form, int color) {
        bulletShooters.get(danmakuNum).setBulletColor(color).setBulletForm(form);
    }

    private void _603(int danmakuNum, float offsetX, float offsetY) {
        bulletShooters.get(danmakuNum).setShootCenterOffset(new Vector2(offsetX, offsetY));
    }

    private void _604(int danmakuNum, float direct, float r) {
        bulletShooters.get(danmakuNum).setBulletWaysDegree((float) Math.toDegrees(direct));
    }

    private void _605(int danmakuNum, float speed, float slowlestSpeed) {
        bulletShooters.get(danmakuNum).setBulletVelocity(new Vector2(0, -speed));
    }

    private void _606(int danmakuNum, int way, int ceng) {
        bulletShooters.get(danmakuNum).setBulletWays(way).setBulletCengShu(ceng);
    }

    private void _607(int danmakuNum, int style) {
        bulletShooters.get(danmakuNum).setBulletStyle(style);
    }

    private void _608(int i0, int i1, int i2) {

    }

    private void _609(int i0, int i1, int i2, int i3, int i4, int i5, float f6, float f7) {

    }

    private void _610(int i0, int i1, int i2, int i3, int i4, int i5, int i6, int i7, float f8, float f9, float f10, float f11) {

    }

    private void _611(int i0, int i1, int i2, int i3, int i4, float f5, float f6) {

    }

    private void _612(int i0, int i1, int i2, int i3, int i4, int i5, int i6, float f7, float f8, float f9, float f10) {

    }

    private void _613() {

    }

    private void _614(int i0, int i1) {

    }

    private void _615(float f0) {

    }

    private void _616(float f0) {

    }

    private void _617(int i0, float f1, float f2, float f3, float f4, float f5, float f6) {

    }

    private void _618(int i0, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {

    }

    private void _619(int i0, float f1, float f2, float f3, float f4) {

    }

    private void _620(int i0, int i1, int i2, int i3, int i4, int i5, int i6) {

    }

    private void _621(int i0, int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {

    }

    private void _622(int i0, int i1, int i2, int i3, int i4) {

    }

    private void _623(float f0, float f1, float f2) {

    }

    private void _624(int i0, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {

    }

    private void _625(int i0, int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {

    }

    private void _626(int i0, float f1, float f2) {

    }

    private void _627(int i0, float f1) {

    }

    private void _628(int i0, float f1, float f2) {

    }

    private void _629(float f0, int i1) {

    }

    private void _630(int i0) {

    }

    private void _631(int i0) {

    }

    private void _632(int i0) {

    }

    private void _633(int i0) {

    }

    private void _634(int i0) {

    }

    private void _635(float f0) {

    }

    private void _636(float f0) {

    }

    private void _637(int i0) {

    }

    private void _638(int i0) {

    }

    private void _639(int i0) {

    }

    private void _640(int i0, int i1, String s2) {

    }

    private void _641(int i0) {

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
                _11(ins.readString(), ins.readParams());
                break;
            case 12:
			  _12((ins.param_mask&1)==1?eclStack.getInt(ins.readInt()):ins.readInt(), ins.readInt());
                break;
            case 13:
			  _13((ins.param_mask&1)==1?eclStack.getInt(ins.readInt()):ins.readInt(), ins.readInt());
                break;
            case 14:
			  _14((ins.param_mask&1)==1?eclStack.getInt(ins.readInt()):ins.readInt(), ins.readInt());
                break;
            case 15:
                _15(ins.readString(), ins.readParams());
                break;
            case 16:
                _16(ins.readString(), ins.readInt(), ins.readInt());
                break;
            case 17:
                _17(ins.readInt());
                break;
            case 22:
                _22(ins.readInt(), ins.readString());
                break;
            case 23:
                _23(ins.readInt());
                break;
            case 30:
                _30(ins.readString(), ins.readInt());
                break;
            case 40:
                _40(ins.readInt());
                break;
            case 42:
			 _42((ins.param_mask&1)==1?eclStack.getInt(ins.readInt()):ins.readInt());
                _42(ins.readInt());
                break;
            case 43:
                _43(ins.readInt());
                break;
            case 44:
                _44(ins.readFloat());
                break;
            case 45:
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
                _78(ins.readInt());
                break;
            case 81:
                _81(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 82:
                _82(ins.readFloat());
                break;
            case 83:
                _83();
                break;
            case 84:
                _84();
                break;
            case 85:
                _85(ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 86:
                _86(ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 87:
                _87(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 88:
                _88(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 89:
                _89(ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 90:
                _90(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 91:
                _91(ins.readInt(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 92:
                _92(ins.readInt(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 93:
                _93(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 300:
                _300(ins.readString(), ins.readFloat(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 301:
                _301(ins.readString(), ins.readFloat(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 302:
                _302(ins.readInt());
                break;
            case 303:
                _303(ins.readInt(), ins.readInt());
                break;
            case 304:
                _304(ins.readString(), ins.readFloat(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 305:
                _305(ins.readString(), ins.readFloat(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 306:
                _306(ins.readInt(), ins.readInt());
                break;
            case 307:
                _307(ins.readInt(), ins.readInt());
                break;
            case 308:
                _308(ins.readInt(), ins.readInt());
                break;
            case 309:
                _309(ins.readString(), ins.readFloat(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 310:
                _310(ins.readString(), ins.readFloat(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 311:
                _311(ins.readString(), ins.readFloat(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 312:
                _312(ins.readString(), ins.readFloat(), ins.readFloat(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 313:
                _313(ins.readInt());
                break;
            case 314:
                _314(ins.readInt(), ins.readInt());
                break;
            case 315:
                _315(ins.readInt(), ins.readInt(), ins.readFloat());
                break;
            case 316:
                _316(ins.readInt(), ins.readInt());
                break;
            case 317:
                _317(ins.readInt(), ins.readInt());
                break;
            case 318:
                _318();
                break;
            case 319:
                _319(ins.readInt(), ins.readFloat());
                break;
            case 320:
                _320(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 321:
                _321(ins.readString(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 322:
                _322(ins.readInt(), ins.readInt());
                break;
            case 323:
                _323(ins.readInt(), ins.readInt());
                break;
            case 324:
                _324(ins.readInt());
                break;
            case 325:
                _325(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 326:
                _326(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 327:
                _327(ins.readInt(), ins.readInt());
                break;
            case 328:
                _328(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 329:
                _329(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 330:
                _330(ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 331:
                _331(ins.readInt(), ins.readInt());
                break;
            case 332:
                _332(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 333:
                _333(ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 334:
                _334(ins.readInt());
                break;
            case 335:
                _335(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 336:
                _336(ins.readInt(), ins.readInt());
                break;
            case 337:
                _337(ins.readInt(), ins.readInt());
                break;
            case 338:
                _338(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 339:
                _339(ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 340:
                _340(ins.readInt());
                break;
            case 400:
                _400(ins.readFloat(), ins.readFloat());
                break;
            case 401:
                _401(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 402:
                _402(ins.readFloat(), ins.readFloat());
                break;
            case 403:
                _403(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 404:
                _404(ins.readFloat(), ins.readFloat());
                break;
            case 405:
                _405(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 406:
                _406(ins.readFloat(), ins.readFloat());
                break;
            case 407:
                _407(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 408:
                _408(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 409:
                _409(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 410:
                _410(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 411:
                _411(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 412:
                _412(ins.readInt(), ins.readInt(), ins.readFloat());
                break;
            case 413:
                _413(ins.readInt(), ins.readInt(), ins.readFloat());
                break;
            case 414:
                _414();
                break;
            case 415:
                _415();
                break;
            case 416:
                _416(ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 417:
                _417(ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 418:
                _418(ins.readFloat(), ins.readFloat());
                break;
            case 419:
                _419(ins.readFloat(), ins.readFloat());
                break;
            case 420:
                _420(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 421:
                _421(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 422:
                _422(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 423:
                _423(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 424:
                _424(ins.readInt());
                break;
            case 425:
                _425(ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 426:
                _426(ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 427:
                _427();
                break;
            case 428:
                _428(ins.readFloat(), ins.readFloat());
                break;
            case 429:
                _429(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 430:
                _430(ins.readFloat(), ins.readFloat());
                break;
            case 431:
                _431(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 432:
                _432(ins.readInt());
                break;
            case 433:
                _433(ins.readInt());
                break;
            case 434:
                _434(ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 435:
                _435(ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 436:
                _436(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 437:
                _437(ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 438:
                _438(ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 439:
                _439(ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 440:
                _440(ins.readFloat());
                break;
            case 441:
                _441(ins.readInt(), ins.readInt(), ins.readFloat());
                break;
            case 442:
                _442(ins.readFloat());
                break;
            case 443:
                _443(ins.readInt(), ins.readInt(), ins.readFloat());
                break;
            case 444:
                _444(ins.readFloat());
                break;
            case 445:
                _445(ins.readInt(), ins.readInt(), ins.readFloat());
                break;
            case 446:
                _446(ins.readFloat());
                break;
            case 447:
                _447(ins.readInt(), ins.readInt(), ins.readFloat());
                break;
            case 500:
                _500(ins.readFloat(), ins.readFloat());
                break;
            case 501:
                _501(ins.readFloat(), ins.readFloat());
                break;
            case 502:
                _502(ins.readInt());
                break;
            case 503:
                _503(ins.readInt());
                break;
            case 504:
                _504(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 505:
                _505();
                break;
            case 506:
                _506();
                break;
            case 507:
                _507(ins.readInt(), ins.readInt());
                break;
            case 508:
                _508(ins.readFloat(), ins.readFloat());
                break;
            case 509:
                _509();
                break;
            case 510:
                _510(ins.readInt());
                break;
            case 511:
                _511(ins.readInt());
                break;
            case 512:
                _512(ins.readInt());
                break;
            case 513:
                _513();
                break;
            case 514:
                _514(ins.readInt(), ins.readInt(), ins.readInt(), ins.readString());
                break;
            case 515:
                _515(ins.readInt());
                break;
            case 516:
                _516(ins.readInt());
                break;
            case 517:
                _517(ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 518:
                _518(ins.readInt());
                break;
            case 519:
                _519();
                break;
            case 520:
                _520();
                break;
            case 521:
                _521(ins.readInt(), ins.readString());
                break;
            case 522:
                _522(ins.readInt(), ins.readInt(), ins.readInt(), ins.readString());
                break;
            case 523:
                _523();
                break;
            case 524:
                _524(ins.readInt());
                break;
            case 525:
                _525();
                break;
            case 526:
                _526(ins.readFloat());
                break;
            case 527:
                _527(ins.readInt(), ins.readFloat(), ins.readInt());
                break;
            case 528:
                _528(ins.readInt(), ins.readInt(), ins.readInt(), ins.readString());
                break;
            case 529:
                _529(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 530:
                _530(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 531:
                _531(ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 532:
                _532(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 533:
                _533(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 534:
                _534(ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 535:
                _535(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 536:
                _536(ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 537:
                _537(ins.readInt(), ins.readInt(), ins.readInt(), ins.readString());
                break;
            case 538:
                _538(ins.readInt(), ins.readInt(), ins.readInt(), ins.readString());
                break;
            case 539:
                _539(ins.readInt(), ins.readInt(), ins.readInt(), ins.readString());
                break;
            case 540:
                _540(ins.readInt());
                break;
            case 541:
                _541(ins.readInt());
                break;
            case 542:
                _542();
                break;
            case 543:
                _543();
                break;
            case 544:
                _544(ins.readInt());
                break;
            case 545:
                _545();
                break;
            case 546:
                _546(ins.readInt(), ins.readInt());
                break;
            case 547:
                _547(ins.readFloat());
                break;
            case 548:
                _548(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 549:
                _549(ins.readInt());
                break;
            case 550:
                _550(ins.readInt());
                break;
            case 551:
                _551(ins.readInt());
                break;
            case 552:
                _552(ins.readInt());
                break;
            case 553:
                _553(ins.readInt());
                break;
            case 554:
                _554();
                break;
            case 555:
                _555(ins.readInt(), ins.readInt());
                break;
            case 556:
                _556(ins.readString());
                break;
            case 557:
                _557(ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 558:
                _558(ins.readInt());
                break;
            case 559:
                _559(ins.readInt());
                break;
            case 560:
                _560(ins.readFloat(), ins.readFloat());
                break;
            case 561:
                _561();
                break;
            case 562:
                _562();
                break;
            case 563:
                _563(ins.readInt());
                break;
            case 564:
                _564(ins.readFloat());
                break;
            case 565:
                _565(ins.readFloat());
                break;
            case 566:
                _566();
                break;
            case 567:
                _567(ins.readInt());
                break;
            case 568:
                _568(ins.readInt());
                break;
            case 569:
                _569(ins.readInt());
                break;
            case 570:
                _570();
                break;
            case 571:
                _571();
                break;
            case 572:
                _572(ins.readInt());
                break;
            case 600:
                _600(ins.readInt());
                break;
            case 601:
                _601(ins.readInt());
                break;
            case 602:
                _602(ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 603:
                _603(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 604:
                _604(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 605:
                _605(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 606:
                _606(ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 607:
                _607(ins.readInt(), ins.readInt());
                break;
            case 608:
                _608(ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 609:
                _609(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 610:
                _610(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 611:
                _611(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 612:
                _612(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 613:
                _613();
                break;
            case 614:
                _614(ins.readInt(), ins.readInt());
                break;
            case 615:
                _615(ins.readFloat());
                break;
            case 616:
                _616(ins.readFloat());
                break;
            case 617:
                _617(ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 618:
                _618(ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 619:
                _619(ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 620:
                _620(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 621:
                _621(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 622:
                _622(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 623:
                _623(ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 624:
                _624(ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 625:
                _625(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 626:
                _626(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 627:
                _627(ins.readInt(), ins.readFloat());
                break;
            case 628:
                _628(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 629:
                _629(ins.readFloat(), ins.readInt());
                break;
            case 630:
                _630(ins.readInt());
                break;
            case 631:
                _631(ins.readInt());
                break;
            case 632:
                _632(ins.readInt());
                break;
            case 633:
                _633(ins.readInt());
                break;
            case 634:
                _634(ins.readInt());
                break;
            case 635:
                _635(ins.readFloat());
                break;
            case 636:
                _636(ins.readFloat());
                break;
            case 637:
                _637(ins.readInt());
                break;
            case 638:
                _638(ins.readInt());
                break;
            case 639:
                _639(ins.readInt());
                break;
            case 640:
                _640(ins.readInt(), ins.readInt(), ins.readString());
                break;
            case 641:
                _641(ins.readInt());
                break;
            case 700:
                _700(ins.readInt(), ins.readFloat(), ins.readFloat(), ins.readFloat(), ins.readFloat());
                break;
            case 701:
                _701(ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt(), ins.readInt());
                break;
            case 702:
                _702(ins.readInt());
                break;
            case 703:
                _703(ins.readInt(), ins.readInt());
                break;
            case 704:
                _704(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 705:
                _705(ins.readInt(), ins.readFloat(), ins.readFloat());
                break;
            case 706:
                _706(ins.readInt(), ins.readFloat());
                break;
            case 707:
                _707(ins.readInt(), ins.readFloat());
                break;
            case 708:
                _708(ins.readInt(), ins.readFloat());
                break;
            case 709:
                _709(ins.readInt(), ins.readFloat());
                break;
            case 710:
                _710(ins.readInt());
                break;
            case 711:
                _711(ins.readInt());
                break;
            case 712:
                _712(ins.readFloat(), ins.readFloat());
                break;
            case 713:
                _713(ins.readInt());
                break;
            case 714:
                _714(ins.readInt(), ins.readInt());
                break;
            case 800:
                _800(ins.readInt(), ins.readString());
                break;
            case 801:
                _801(ins.readFloat(), ins.readFloat(), ins.readInt());
                break;
            case 802:
                _802(ins.readInt());
                break;
            case 900:
                _900(ins.readInt());
                break;
            case 901:
                _901();
                break;
            case 902:
                _902();
                break;
            case 1000:
                _1000(ins.readInt());
                break;
            case 1001:
                _1001(ins.readInt());
                break;
            case 1002:
                _1002(ins.readInt());
                break;
        }
    }


}
