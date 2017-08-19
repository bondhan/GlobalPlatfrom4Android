package com.bondhan.research.dao;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by bondhan on 31/07/17.
 */

public class ScpModeConverter implements PropertyConverter<ScpModeEnum, Integer>
{
    @Override
    public ScpModeEnum convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        for (ScpModeEnum role : ScpModeEnum.values()) {
            if (role.scp_mode == databaseValue) {
                return role;
            }
        }
        return ScpModeEnum.SCP_ANY;
    }

    @Override
    public Integer convertToDatabaseValue(ScpModeEnum entityProperty) {
        return entityProperty == null ? null : entityProperty.scp_mode;
    }
}