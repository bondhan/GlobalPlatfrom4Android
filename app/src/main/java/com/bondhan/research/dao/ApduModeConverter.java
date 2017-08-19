package com.bondhan.research.dao;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by bondhan on 31/07/17.
 */

public class ApduModeConverter implements PropertyConverter<ApduModeEnum, Integer>{
    /**
     * Created by bondhan on 31/07/17.
     */

    @Override
    public ApduModeEnum convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        for (ApduModeEnum apduMode : ApduModeEnum.values()) {
            if (apduMode.apdu_mode == databaseValue) {
                return apduMode;
            }
        }
        return ApduModeEnum.CLR;
    }

    @Override
    public Integer convertToDatabaseValue(ApduModeEnum entityProperty) {
        return entityProperty == null ? null : entityProperty.apdu_mode;
    }
}

