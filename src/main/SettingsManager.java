package main;

import javax.microedition.rms.*;

public class SettingsManager {

    private static final String STORE_NAME = "BytecodeEditor Setting";


    public static void saveSetting(Setting settings) {
        byte[] record = settings.toByteArray();
        RecordStore recordStore = null;
        try {
            recordStore = RecordStore.openRecordStore(STORE_NAME, true);
            if (recordStore.getNumRecords() == 0) {
                recordStore.addRecord(record, 0, record.length);
            }
            else {
                recordStore.setRecord(1, record, 0, record.length);
            }
            recordStore.closeRecordStore();
        } catch (RecordStoreException e) {
        }
    }


    public static Setting getSetting() {
        if (RecordStore.listRecordStores() == null) {
            return new Setting();
        }
        else {
            RecordStore recordStore = null;
            try {
                recordStore = RecordStore.openRecordStore(STORE_NAME, false);
                Setting settings = new Setting(recordStore.getRecord(1));
                recordStore.closeRecordStore();
                return settings;
            } catch (RecordStoreException e) {
                return new Setting();
            }
        }
    }
}
