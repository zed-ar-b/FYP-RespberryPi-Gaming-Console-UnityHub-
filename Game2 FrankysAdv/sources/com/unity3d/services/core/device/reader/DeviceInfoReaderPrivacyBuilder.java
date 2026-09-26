package com.unity3d.services.core.device.reader;

import com.unity3d.services.core.configuration.ConfigurationReader;
import com.unity3d.services.core.configuration.InitRequestType;
import com.unity3d.services.core.configuration.PrivacyConfigStorage;
import com.unity3d.services.core.device.Storage;
import com.unity3d.services.core.device.StorageManager;
import com.unity3d.services.core.misc.JsonFlattenerRules;
import java.util.Arrays;
import java.util.Collections;

public class DeviceInfoReaderPrivacyBuilder extends DeviceInfoReaderBuilder {
    public DeviceInfoReaderPrivacyBuilder(ConfigurationReader configurationReader, PrivacyConfigStorage privacyConfigStorage, IGameSessionIdReader iGameSessionIdReader) {
        super(configurationReader, privacyConfigStorage, iGameSessionIdReader);
    }

    public IDeviceInfoReader build() {
        Storage storage = StorageManager.getStorage(StorageManager.StorageType.PRIVATE);
        Storage storage2 = StorageManager.getStorage(StorageManager.StorageType.PUBLIC);
        DeviceInfoReaderFilterProvider deviceInfoReaderFilterProvider = new DeviceInfoReaderFilterProvider(storage);
        return new DeviceInfoReaderWithFilter(new DeviceInfoReaderWithStorageInfo(buildWithRequestType(InitRequestType.PRIVACY), getPrivacyRequestStorageRules(), storage, storage2), deviceInfoReaderFilterProvider.getFilterList());
    }

    private JsonFlattenerRules getPrivacyRequestStorageRules() {
        return new JsonFlattenerRules(Arrays.asList(new String[]{"privacy", "gdpr", "unity", "pipl"}), Collections.singletonList("value"), Arrays.asList(new String[]{"ts", "exclude", "mode"}));
    }
}
