using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;

namespace sht {
    class SHTFILE {

        public SHTFILE() { 
        }

        public void init() {
            public playerArg args;
        public List<position> posotions = new List<position>();
        public List<offsets> posotions = new List<offsets>();
        public List<eachShot> shots = new List<eachShot>();


    }

    #region structs
    struct position {
            public float x;
            public float y;
        };

        struct eachShot {
            public byte rate;          //发弹间隔
            public byte delay;         //发弹时间点
            public short power;         //每发子弹的威力
            public position pos;       //发弹点相对坐标
            public position hitBox;    //子弹判定的长和宽
            public float angle;        //发射方向
            public float speed;        //发射速度
            public Int32 unknown1;     //fsl之后加入
            public byte option;        //子机号,0=main
            public byte unknown2;
            public Int16 ANM;           //自机弹贴图
            public Int16 SE;            //音效
            public byte rate2;         //rate2
            public byte delay2;            //delay2
            [MarshalAs(UnmanagedType.ByValArray,SizeConst = 12,ArraySubType = UnmanagedType.I4)]
            public Int32[] flags;
        }
        struct playerArg {
            public Int16 maxPower;                                  //默认4
            public Int16 totalOffset;                               //最大offset数量
            public float hitBox;                                   //判定				(无用)
            public float powerAttractSpeed;                        //收p点的速度		(无用)
            public float powerAttractHitBox;                       //收p点的判定(高速)	(无用)
            public float normalSpeed;                              //高速 速度
            public float forcedSpeed;                              //低速速度
            public float normalDiagonalSpeed;                      //斜向高速(指分配到xy的速度)
            public float forcedDiagonalSpeed;                      //斜向低速
            public Int16 maxPowerLevel;                         //最大的子机数(power的最大百位值)	
            public Int16 unknown1;                                  //不知道
            public Int32 powerVar;                                 //power要修改的内容(40)
            public Int32 maxDamage;                                //单帧子弹最高伤害
            [MarshalAs(UnmanagedType.ByValArray,SizeConst = 5,ArraySubType = UnmanagedType.I4)]
            public Int32[] unknown;                               //不知道
        }
        #endregion
    }
}
