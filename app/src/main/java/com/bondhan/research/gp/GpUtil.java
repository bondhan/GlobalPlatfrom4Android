package com.bondhan.research.gp;

import com.bondhan.research.common.ByteUtils;

/**
 * Created by bondhan on 08/08/17.
 */

public class GpUtil {

    //6F' File Control Information (FCI template) Mandatory
    //'84' Application / file AID Mandatory
    //'A5' Proprietary data Mandatory
    //'73' Security Domain Management Data (see Appendix
    //F for detailed coding) Optional
    //'9F6E' Application production life cycle data Optional
    //'9F65' Maximum length of data field in command message Mandatory
    public static String parseSelectCM(byte[] data)
    {
        int index = 0;

        String dataStr = ByteUtils.htos2(data);
        if (dataStr.length() <= 0x04)
            return null;

        String Tag6F = dataStr.substring(index, 2);
        if (!Tag6F.equalsIgnoreCase("6F"))
            return null;

        index = 2;
        String lenTag6F = dataStr.substring(index, index + 2);

        index = index + 2;

        String Tag84 = dataStr.substring(index, index + 2);

        index = index + 2;

        String LenTag84 = dataStr.substring(index, index + 2);

        index = index + 2;

        String CmAid = dataStr.substring(index, index + (Integer.parseInt(LenTag84, 16) * 2));

        return CmAid;
    }
}
