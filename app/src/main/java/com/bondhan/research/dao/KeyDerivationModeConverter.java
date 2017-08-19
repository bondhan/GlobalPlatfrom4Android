package com.bondhan.research.dao;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by bondhan on 31/07/17.
 */

public class KeyDerivationModeConverter implements PropertyConverter<KeyDerivationModeEnum, Integer>{
    /**
     * Created by bondhan on 31/07/17.
     */

    @Override
    public KeyDerivationModeEnum convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        for (KeyDerivationModeEnum kdvMode : KeyDerivationModeEnum.values()) {
            if (kdvMode.key_derivation_mode == databaseValue) {
                return kdvMode;
            }
        }
        return KeyDerivationModeEnum.NONE;
    }

    @Override
    public Integer convertToDatabaseValue(KeyDerivationModeEnum entityProperty) {
        return entityProperty == null ? null : entityProperty.key_derivation_mode;
    }
}

