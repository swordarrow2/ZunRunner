package com.InsProcess.parse.beans;

import java.util.*;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.ui.*;
import com.InsProcess.helper.*;
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
	private int eclPosition=0;
	
	private EclFile eclFile;
    private EclSubPack eclSubPack;
	private EclSub parentSub;
	
	
	public HashMap<Integer, BulletShooter> bulletShooters = new HashMap<>();
	
	private int waitFrams=0;
	
	private boolean startByIns11=false;

    public EclSub(EclFile file,EclSubPack eclSubPack) {
	  eclFile=file;
        this.eclSubPack = eclSubPack;
    }
	public EclIns nowIns(){
	  return inses.get(eclPosition);
	}
	
    public EclIns nextIns(){
	  return inses.get(++eclPosition);
	}
	
	public EclIns preIns(){
	  return inses.get(--eclPosition);
	}
    
    public void insList() {
        while (dataPosition < data.length) {
            EclIns eclIns = new EclIns();
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
	
	public EclSub setPartent(EclSub parent){
	  parentSub=parent;
	  return this;
	}
	
	public EclSub startByIns11(boolean b){
	  eclPosition=0;
	  startByIns11=b;
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
	
	private void gotoIns(int length){
	  int nowSkipLength=0;
	  int nowSkipIns=0;
	  if(length<0){
		while(nowSkipLength>length){
		  EclIns ins=preIns();
		  nowSkipLength+=ins.size;
		  ++nowSkipIns;
		}
	//	eclPosition-=nowSkipIns;
	  }else{
		  while(nowSkipLength<length){
			  EclIns ins=nextIns();
			  nowSkipLength+=ins.size;
			  ++nowSkipIns;
			}
	//	eclPosition+=nowSkipIns;
	  }
	}

	public void update() {
        if (eclPosition < inses.size()) {
            EclIns ins = inses.get(eclPosition);
			if (ins.id == 23) {FightScreen.nowins="eclUpdate:"+ins.id+" param:"+ins.readInt(0)+" waited:"+waitFrams;
				
                if (ins.readInt(0) > ++waitFrams) {
                    return;
				  }else{
					waitFrams=0;
					++eclPosition;
					return;
				  }
			}
            invoke(ins);
            ++eclPosition;
            update();
		  }
	  }
	  
	public void invoke(EclIns ins) {
      //  EclVar[] a = ins.args;
	  
        switch (ins.id) {
            case 10:
			  System.out.println("sub exit");
			  if(startByIns11){
				Ecl.toAddSubs.add(parentSub);
				Ecl.onPauseSubs.remove(parentSub);
				Ecl.toDeleteSubs.add(this);
			  }else{
				Ecl.toDeleteSubs.add(this);
			  }
		//	  nowIns = 999;
		//	  Ecl.toDeleteSubs.add(this);
			  break;
		//	  case 11:
		//	  Ecl.toAddSubs.add(eclFile.eclManager.getSubPack(new String(ins.data)).startByIns11(true).setPartent(this));
		//	  Ecl.onPauseSubs.add(this);
		//	  Ecl.toDeleteSubs.add(this);
		//	  break;
		//	  case 15:
		//	  Ecl.toAddSubs.add(eclFile.eclManager.getSubPack(new String(ins.data)).startByIns11(false));
		//	  break;
      /*      case 11:
			  _11(a);
			  break;
      */     case 12:
	         case 13:
		     case 14:
			  gotoIns(ins.readInt(0));
		     break;
      /*      case 13:
			  EclVar eclVar13 = a[0];
			  String gotoFlag13 = eclVar13.s.substring(eclVar13.s.indexOf("goto") + 4);
			  String expression13 = eclVar13.s.substring(eclVar13.s.indexOf("unless") + 6, eclVar13.s.indexOf("goto"));
			  if (!invokeExpression(expression13)) {
				  nowIns = getLable(gotoFlag13);
                }
			  break;
            case 14:
			  EclVar eclVar14 = a[0];
			  String gotoFlag14 = eclVar14.s.substring(eclVar14.s.indexOf("goto") + 4);
			  String expression14 = eclVar14.s.substring(eclVar14.s.indexOf("if") + 2, eclVar14.s.indexOf("goto"));
			  if (invokeExpression(expression14)) {
				  nowIns = getLable(gotoFlag14);
                }
			  break;
            case 15:
			  _15(a);
			  break;
            case 50:
			  _50(a);
			  break;
            case 401:
			  _401(a[0].i, a[1].i, a[2].f, a[3].f);
			  break;
            case 535:
			  _535(a[0], a[1].i, a[2].i, a[3].i, a[4].i);
            case 600:
			  _600(a[0].i);
			  break;
            case 601:
			  _601(a[0].i);
			  break;
            case 602:
			  _602(a[0].i, a[1].i, a[2].i);
			  break;
            case 603:
			  _603(a[0].i, a[1].f, a[2].f);
			  break;
            case 604:
			  _604(a[0].i, a[1].f, a[2].f);
			  break;
            case 605:
			  _605(a[0].i, a[1].f, a[2].f);
			  break;
            case 606:
			  _606(a[0].i, a[1].i, a[2].i);
			  break;
            case 607:
			  _607(a[0].i, a[1].i);
			  break;
            case 608:
			  _608(a[0].i, a[1].i, a[2].i);
			  break;
            case 609:
			  _609(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].i, a[6].f, a[7].f);
			  break;
            case 610:
			  _610(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].i, a[6].i, a[7].i, a[8].f, a[9].f, a[10].f, a[11].f);
			  break;
            case 611:
			  _611(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].f, a[6].f);
			  break;
            case 612:
			  _612(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].f, a[6].f, a[7].f, a[8].f, a[9].f, a[10].f);
			  break;
            case 613:
			  _613();
			  break;
            case 614:
			  _614(a[0].i, a[1].i);
			  break;
            case 615:
			  _615(a[0].f);
			  break;
            case 616:
			  _616(a[0].f);
			  break;
            case 623:
			  _623(a[0].s, a[1].f, a[2].f);
			  break;
            case 624:
			  _624(a[0].i, a[1].f, a[2].f, a[3].f, a[4].f, a[5].f, a[6].f, a[7].f, a[8].f);
			  break;
            case 625:
			  _625(a[0].i, a[1].i, a[2].i, a[3].i, a[4].i, a[5].i, a[6].i, a[7].i, a[8].i);
			  break;
            case 626:
			  _626(a[0].i, a[1].f, a[2].f);
			  break;
            case 627:
			  _627(a[0].i, a[1].f);
			  break;
            case 628:
			  _628(a[0].i, a[1].f, a[2].f);
			  break;
            case 629:
			  _629(a[0].f, a[1].i);
			  break;
            case 630:
			  _630(a[0].i);
			  break;
            case 631:
			  _631(a[0].i);
			  break;
            case 632:
			  _632(a[0].i);
			  break;
            case 633:
			  _633(a[0].i);
			  break;
            case 634:
			  _634(a[0].i);
			  break;
            case 635:
			  _635(a[0].f);
			  break;
            case 636:
			  _636(a[0].f);
			  break;
            case 637:
			  _637(a[0].i);
			  break;
            case 638:
			  _638(a[0].i);
			  break;
            case 639:
			  _639(a[0].i);
			  break;
            case 640:
			  _640(a[0].i, a[1].i, a[2].s);
			  break;
            case 641:
			  _641(a[0].i);
			  break;
            default:

			  break;*/
		  }
	  }

}
