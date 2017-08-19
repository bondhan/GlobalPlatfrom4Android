package com.bondhan.research.dao;

/**
 * Created by bondhan on 31/07/17.
 */

public enum ApduModeEnum {

    CLR(0x00), MAC(0x01), ENC(0x02), RMAC(0x10);

    final int apdu_mode;

    ApduModeEnum(int apdu_mode) {this.apdu_mode = apdu_mode;}

    public static String getStringValue(ApduModeEnum enumVal)
    {
        String inStrVal = "";
        switch (enumVal)
        {
            case CLR:
                inStrVal =  "CLR";
                break;
            case MAC:
                inStrVal = "MAC";
                break;
            case ENC:
                inStrVal = "ENC";
                break;
            case RMAC:
                inStrVal = "RMAC";
                break;
            default:
                inStrVal = null;
        }

        return inStrVal;
    }
}
