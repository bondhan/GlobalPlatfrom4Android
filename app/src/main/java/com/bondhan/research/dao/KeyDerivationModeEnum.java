package com.bondhan.research.dao;

/**
 * Created by bondhan on 03/08/17.
 */

public enum KeyDerivationModeEnum {

    NONE(0), EMV(1), VISA(2);

    final int key_derivation_mode;

    KeyDerivationModeEnum(int key_derivation_mode){
        this.key_derivation_mode = key_derivation_mode;
    }

    public static String getStringValue(KeyDerivationModeEnum enumVal)
    {
        String inStrVal = "";
        switch (enumVal)
        {
            case NONE:
                inStrVal =  "NONE";
                break;
            case EMV:
                inStrVal = "EMV";
                break;
            case VISA:
                inStrVal = "VISA";
                break;
            default:
                inStrVal = null;
        }

        return inStrVal;
    }
}
