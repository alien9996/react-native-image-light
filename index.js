
import { NativeModules } from 'react-native';

const { RNImageLight } = NativeModules;

const DEFAULT_OPTIONS = {
    imageSource1: null,
    imageSource2: null,
    dataType1: "Path",
    dataType2: "Path",
    overlayType: 0,
    isAccsets: true
}

module.exports = {
    ...RNImageLight,
    getResourcesImageLight: function getResourcesImageLight(options, callback) {
        if (typeof options === 'function') {
            callback = options;
            options = {};
        }
        return RNImageLight.getResourcesImageLight({ ...DEFAULT_OPTIONS, ...options }, callback)
    }
}
