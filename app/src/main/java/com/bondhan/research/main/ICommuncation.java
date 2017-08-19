package com.bondhan.research.main;

/**
 * Created by bondhan on 12/08/17.
 */

public interface  ICommuncation {
    void appendLogMessage(String msg);
    void apduCommLog(String cmd, String resp);
}
