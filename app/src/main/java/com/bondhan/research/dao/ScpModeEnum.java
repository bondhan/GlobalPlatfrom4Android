package com.bondhan.research.dao;

/**
 * Created by bondhan on 31/07/17.
 */

public enum ScpModeEnum
{
    SCP_ANY(0),
    SCP_01_05(1),
    SCP_01_15(2),
    SCP_02_04(3),
    SCP_02_05(4),
    SCP_02_0A(5),
    SCP_02_0B(6),
    SCP_02_14(7),
    SCP_02_15(8),
    SCP_02_1A(9),
    SCP_02_1B(10);

    final int scp_mode;

    ScpModeEnum(int scp_mode){
        this.scp_mode = scp_mode;
    }

    public static String getStringValue(ScpModeEnum enumVal)
    {
        String inStrVal = "";
        switch (enumVal)
        {
            case SCP_ANY:
                inStrVal =  "SCP_ANY";
                break;
            case SCP_01_05:
                inStrVal = "SCP_01_05";
                break;
            case SCP_01_15:
                inStrVal = "SCP_01_15";
                break;
            case SCP_02_04:
                inStrVal = "SCP_02_04";
                break;
            case SCP_02_05:
                inStrVal =  "SCP_02_05";
                break;
            case SCP_02_0A:
                inStrVal = "SCP_02_0A";
                break;
            case SCP_02_0B:
                inStrVal = "SCP_02_0B";
                break;
            case SCP_02_14:
                inStrVal = "SCP_02_14";
                break;
            case SCP_02_15:
                inStrVal =  "SCP_02_15";
                break;
            case SCP_02_1A:
                inStrVal = "SCP_02_1A";
                break;
            case SCP_02_1B:
                inStrVal = "SCP_02_1B";
                break;
            default:
                inStrVal = null;
        }

        return inStrVal;
    }
}